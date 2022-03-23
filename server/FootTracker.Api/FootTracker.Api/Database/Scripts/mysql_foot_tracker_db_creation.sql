DROP DATABASE IF EXISTS foot_tracker_db;
CREATE DATABASE IF NOT EXISTS foot_tracker_db;
USE foot_tracker_db;

DROP TABLE IF EXISTS CHAMPIONSHIPS;
DROP TABLE IF EXISTS REFEREES;
DROP TABLE IF EXISTS TEAMS;
DROP TABLE IF EXISTS PLAYERS;
DROP TABLE IF EXISTS GAMES;
DROP TABLE IF EXISTS ATTACHMENTS;
DROP TABLE IF EXISTS CARDS;
DROP TABLE IF EXISTS MEMBERSHIPS;
DROP TABLE IF EXISTS GOALS;

CREATE TABLE CHAMPIONSHIPS (
  championship_id INT NOT NULL AUTO_INCREMENT, 
  championship_name VARCHAR(100) NOT NULL,
  CONSTRAINT PK_Championship_Id PRIMARY KEY (championship_id),
  CONSTRAINT UC_Championship_Name UNIQUE(championship_name)
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

CREATE TABLE REFEREES (
  referee_id INT NOT NULL AUTO_INCREMENT, 
  referee_full_name VARCHAR(300) NOT NULL,
  CONSTRAINT PK_Referee_Id PRIMARY KEY (referee_id),
  CONSTRAINT UC_Referee_Full_Name UNIQUE(referee_full_name)
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

-- team_name : nom club ou nom pays selon competition
CREATE TABLE TEAMS(
 team_id INT NOT NULL AUTO_INCREMENT, 
 team_name VARCHAR(100) NOT NULL,
 CONSTRAINT PK_Team_Id PRIMARY KEY (team_id),
 CONSTRAINT UC_Team_Name UNIQUE(team_name)
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

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
CREATE TABLE PLAYERS(
 player_id INT NOT NULL AUTO_INCREMENT, 
 player_full_name VARCHAR(300) NOT NULL,
 position VARCHAR(50) NOT NULL,
 CONSTRAINT PK_Player_Id PRIMARY KEY (player_id),
 CONSTRAINT CHK_Player_Position CHECK(position IN('Striker', 'LeftMidfielder', 'RightMidfielder', 'AttackingMidfielder', 'CentralMidfielder', 'DefendingMidfielder', 'LeftFullBack', 'RightFullBack', 'LeftCenterBack', 'RightCenterBack', 'Goalkeeper'))
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;


-- out_count : nb de sorties
-- free_kick_count : nb de coup-francs
-- offside_count : nb de hors-jeu
-- overtime : prolongations
-- possession_count : 1 possession par joueur
-- place : adresse complete issue de la geolocalisation google maps

CREATE TABLE GAMES (
  	game_id INT NOT NULL AUTO_INCREMENT,
  	game_datetime DATETIME NOT NULL, 
  	place VARCHAR(100) NOT NULL, 
  	validated BOOLEAN DEFAULT false,
  	tracked_team_score INT DEFAULT 0, 
	opponent_score INT DEFAULT 0,
  	out_count INT DEFAULT 0,
  	penalty_count INT DEFAULT 0, 
  	free_kick_count INT DEFAULT 0,
  	corner_count INT DEFAULT 0, 
  	occasion_count INT DEFAULT 0, 
	fault_count INT DEFAULT 0,
	stop_count INT DEFAULT 0,
	offside_count INT DEFAULT 0,
  	overtime TIME DEFAULT '00:00:00',
  	tracked_team_possession_count INT DEFAULT 0,
  	opponent_team_possession_count INT DEFAULT 0,
  	tracked_team_id INT NOT NULL,
	opponent_team_id INT NOT NULL,
	championship_id INT NOT NULL,
  	referee_id INT NOT NULL, 
  	CONSTRAINT PK_Game_Id PRIMARY KEY (game_id),
	CONSTRAINT FK_Game_Tracked_Team_Id FOREIGN KEY (tracked_team_id) REFERENCES TEAMS(team_id) ON DELETE CASCADE,
	CONSTRAINT FK_Game_Opponent_Team_Id FOREIGN KEY (opponent_team_id) REFERENCES TEAMS(team_id) ON DELETE CASCADE,
	CONSTRAINT FK_Game_Championship_Id FOREIGN KEY (championship_id) REFERENCES CHAMPIONSHIPS(championship_id) ON DELETE CASCADE,
	CONSTRAINT FK_Game_Referee_Id FOREIGN KEY (referee_id) REFERENCES REFEREES(referee_id) ON DELETE CASCADE,
	CONSTRAINT UC_Game_Occurrence UNIQUE(game_datetime, place)
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

CREATE TABLE ATTACHMENTS(
 attachment_id INT NOT NULL AUTO_INCREMENT, 
 attachment_path VARCHAR(300) NOT NULL,
 attachment_game_id INT NOT NULL,
 CONSTRAINT PK_Attachment_Id PRIMARY KEY (attachment_id),
 CONSTRAINT FK_Attachment_Game_Id FOREIGN KEY (attachment_game_id) REFERENCES GAMES(game_id) ON DELETE CASCADE,
 CONSTRAINT UC_Attachment_Path UNIQUE(attachment_path)
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

CREATE TABLE CARDS(
	card_type VARCHAR(15) NOT NULL CHECK(card_type IN('YELLOW', 'RED')),
	card_player_id INT NOT NULL,
	card_game_id INT NOT NULL,
	moment TIME NOT NULL,
	CONSTRAINT PK_Card_Id PRIMARY KEY(card_player_id, card_game_id, moment),
	CONSTRAINT FK_Card_Player_Id FOREIGN KEY (card_player_id) REFERENCES PLAYERS(player_id) ON DELETE CASCADE,
	CONSTRAINT FK_Card_Game_Id FOREIGN KEY (card_game_id) REFERENCES GAMES(game_id) ON DELETE CASCADE
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

CREATE TABLE MEMBERSHIPS(
	membership_player_id INT NOT NULL,
	membership_team_id INT NOT NULL,
	CONSTRAINT PK_Membership_Id PRIMARY KEY(membership_player_id, membership_team_id),
	CONSTRAINT FK_Membership_player_id FOREIGN KEY (membership_player_id) REFERENCES PLAYERS(player_id) ON DELETE CASCADE,
	CONSTRAINT FK_Membership_team_id FOREIGN KEY (membership_team_id) REFERENCES TEAMS(team_id) ON DELETE CASCADE
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;

CREATE TABLE GOALS(
	goal_game_id INT NOT NULL,
	goal_player_id INT NOT NULL,
	moment TIME NOT NULL,
	CONSTRAINT PK_Goal_Id PRIMARY KEY(goal_player_id, goal_game_id, moment),
	CONSTRAINT FK_Goal_Game_Id FOREIGN KEY (goal_game_id) REFERENCES GAMES(game_id) ON DELETE CASCADE,
	CONSTRAINT FK_Goal_Player_Id FOREIGN KEY (goal_player_id) REFERENCES PLAYERS(player_id) ON DELETE CASCADE
)ENGINE = 'InnoDB' DEFAULT CHARSET=utf8;