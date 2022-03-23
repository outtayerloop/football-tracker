-- Avant d'executer ce trigger, mettre le delimiteur à "$$".

-- Legende enum de position des joueurs : 
-- 0 : striker : avant-centre
-- 1 : left midfielder : ailier gauche
-- 2 : right midfielder : ailier droit
-- 3 : attacking midfielder : milieu gauche
-- 4 : central midfielder : milieu droit
-- 5 : defending midfielder : milieu central
-- 6 : left fullback : arriere gauche
-- 7 : right fullback : arriere droit
-- 8 : left center-back : defenseur central gauche
-- 9 : right center-back : defenseur central droit
-- 10 : goalkeeper : gardien de but

USE foot_tracker_db;$$

-- Trigger de verification de la position donnee à un joueur.
DROP TRIGGER IF EXISTS position_checker;$$

CREATE TRIGGER position_checker
BEFORE INSERT 
ON MEMBERSHIPS FOR EACH ROW
BEGIN

	DECLARE found_player_count INTEGER;
	DECLARE inserted_player_position VARCHAR(50);
	
	select position into inserted_player_position from PLAYERS where player_id = new.membership_player_id;
	
	SELECT COUNT(membership_player_id) into found_player_count 
		from MEMBERSHIPS inner join PLAYERS on (PLAYERS.player_id = MEMBERSHIPS.membership_player_id)
		where position = inserted_player_position and membership_team_id = new.membership_team_id;
	
	IF found_player_count > 0 THEN
			SIGNAL SQLSTATE '45000' set message_text = 'Position can be occupied by only 1 team member.';
	END IF;
END;$$

-- Trigger de verification du type de carton donne à un joueur.
DROP TRIGGER IF EXISTS card_type_checker;$$

CREATE TRIGGER card_type_checker
BEFORE INSERT 
ON CARDS FOR EACH ROW
BEGIN

	DECLARE found_card_count INTEGER;
	DECLARE red_card_count INTEGER;
	DECLARE err_msg VARCHAR(150);
	
	SELECT COUNT(card_player_id) 
		into found_card_count 
		from CARDS
		where card_player_id = new.card_player_id 
		and card_game_id = new.card_game_id
		and card_type = new.card_type;
		
	SELECT COUNT(card_player_id) 
		into red_card_count 
		from CARDS
		where card_player_id = new.card_player_id 
		and card_game_id = new.card_game_id
		and card_type = '1';
	
	IF (new.card_type = '1') THEN
	
		IF found_card_count > 0 THEN
			SET err_msg = 'Can not deliver more than 1 red card per player per game.';
			SIGNAL SQLSTATE '45000' set message_text = err_msg;
		END IF;
	ELSE
		IF found_card_count > 2 THEN
			SIGNAL SQLSTATE '45000' set message_text = 'Can not deliver more than 3 yellow cards per player per game.';
		ELSEIF red_card_count > 0 THEN
			SIGNAL SQLSTATE '45000' set message_text = 'Can not deliver yellow card to player with red card in this game.';
		END IF;
	END IF;
END;$$

-- Trigger de verification du joueur auquel on veut donner le carton actuel.
DROP TRIGGER IF EXISTS card_owner_checker;$$

CREATE TRIGGER card_owner_checker
BEFORE INSERT 
ON CARDS FOR EACH ROW
BEGIN

	DECLARE found_player_id INT;
	DECLARE validated_game BOOLEAN;
	
	SELECT membership_player_id, validated
		into found_player_id, validated_game
		from MEMBERSHIPS m, GAMES g
		where m.membership_player_id = new.card_player_id
		and (m.membership_team_id = g.tracked_team_id or m.membership_team_id = g.opponent_team_id)
		and g.game_id = new.card_game_id;
	
	IF (found_player_id is null) THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not deliver card to player who did not play in this game.';
	ELSEIF validated_game = true THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not deliver card to player on validated game.';
	END IF;
END;$$

-- Trigger de verification du joueur auquel on veut donner le but actuel.
DROP TRIGGER IF EXISTS goal_owner_checker;$$

CREATE TRIGGER goal_owner_checker
BEFORE INSERT 
ON GOALS FOR EACH ROW
BEGIN

	DECLARE found_player_id INT;
	DECLARE validated_game BOOLEAN;
	
	SELECT membership_player_id, validated
		into found_player_id, validated_game
		from MEMBERSHIPS m, GAMES g
		where m.membership_player_id = new.goal_player_id
		and (m.membership_team_id = g.tracked_team_id or m.membership_team_id = g.opponent_team_id)
		and g.game_id = new.goal_game_id;
	
	IF (found_player_id is null) THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not assign goal to player who did not play in this game.';
	ELSEIF validated_game = true THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not assign goal to player on validated game.';
	END IF;
END;$$

-- Trigger de verification des equipes du match qu'on veut creer :
-- equipes de 11 joueurs chacune, 
-- match non valide,
-- pas 2 fois la meme equipe,
-- pas 2 equipes avec des joueurs qui appartiennent en meme temps aux 2 equipes.
DROP TRIGGER IF EXISTS game_creation_checker;$$

CREATE TRIGGER game_creation_checker
BEFORE INSERT 
ON GAMES FOR EACH ROW
BEGIN

	DECLARE opponent_team_player_count INT;
	DECLARE common_player_id INT;
		
	SELECT COUNT(membership_team_id)
		into opponent_team_player_count
		from MEMBERSHIPS m
		where m.membership_team_id = new.opponent_team_id;
		
	SELECT m.membership_player_id
		into common_player_id
		from MEMBERSHIPS m
		where m.membership_team_id = new.tracked_team_id
		and m.membership_player_id in (
			select m2.membership_player_id
			from MEMBERSHIPS m2
			where m2.membership_team_id = new.opponent_team_id)
	limit 1;
	
	IF new.validated = true THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not create immediatly validated game.';
	ELSEIF new.tracked_team_id = new.opponent_team_id THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Same team can not confront itself in a game.';
	ELSEIF common_player_id is not null THEN
		SIGNAL SQLSTATE '45000' set message_text = 'Can not create game with at least 1 player belonging to both teams.';
	END IF;
END;$$