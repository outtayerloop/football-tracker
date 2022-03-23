CREATE TABLE IF NOT EXISTS CHAMPIONSHIPS(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    championship_name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS  REFEREES(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    referee_full_name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS  TEAMS(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    team_name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS  PLAYERS(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_full_name TEXT NOT NULL,
    position TEXT NOT NULL CHECK(position IN('Striker', 'LeftMidfielder', 'RightMidfielder', 'AttackingMidfielder', 'CentralMidfielder', 'DefendingMidfielder', 'LeftFullBack', 'RightFullBack', 'LeftCenterBack', 'RightCenterBack', 'Goalkeeper'))
);
CREATE TABLE IF NOT EXISTS  MEMBERSHIPS(
    membership_player_id INTEGER NOT NULL,
    membership_team_id INTEGER NOT NULL,
    PRIMARY KEY (membership_player_id, membership_team_id),
    FOREIGN KEY (membership_player_id) REFERENCES PLAYERS (_id) ON DELETE CASCADE,
    FOREIGN KEY (membership_team_id) REFERENCES TEAMS (_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS GAMES(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
  	game_datetime TEXT NOT NULL,
  	place TEXT NOT NULL,
  	validated INTEGER DEFAULT 0,
  	tracked_team_score INTEGER DEFAULT 0,
	opponent_score INTEGER DEFAULT 0,
  	out_count INTEGER DEFAULT 0,
  	penalty_count INTEGER DEFAULT 0,
  	free_kick_count INTEGER DEFAULT 0,
  	corner_count INTEGER DEFAULT 0,
  	occasion_count INTEGER DEFAULT 0,
	fault_count INTEGER DEFAULT 0,
	stop_count INTEGER DEFAULT 0,
	offside_count INTEGER DEFAULT 0,
  	overtime TEXT DEFAULT '00:00:00',
  	tracked_team_possession_count INTEGER DEFAULT 0,
  	opponent_team_possession_count INTEGER DEFAULT 0,
  	tracked_team_id INTEGER NOT NULL,
	opponent_team_id INTEGER NOT NULL,
	championship_id INTEGER NOT NULL,
  	referee_id INTEGER NOT NULL,
	FOREIGN KEY (tracked_team_id) REFERENCES TEAMS(_id) ON DELETE CASCADE,
	FOREIGN KEY (opponent_team_id) REFERENCES TEAMS(_id) ON DELETE CASCADE,
	FOREIGN KEY (championship_id) REFERENCES CHAMPIONSHIPS(_id) ON DELETE CASCADE,
	FOREIGN KEY (referee_id) REFERENCES REFEREES(_id) ON DELETE CASCADE,
	UNIQUE(game_datetime, place)
);

CREATE TABLE IF NOT EXISTS ATTACHMENTS(
     _id INTEGER PRIMARY KEY AUTOINCREMENT,
     attachment_path TEXT NOT NULL UNIQUE,
     attachment_game_id INTEGER NOT NULL,
     FOREIGN KEY (attachment_game_id) REFERENCES GAMES(_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CARDS(
	card_type TEXT NOT NULL CHECK(card_type IN('YELLOW', 'RED')),
	card_player_id INTEGER NOT NULL,
	card_game_id INTEGER NOT NULL,
	moment TIME NOT NULL,
	PRIMARY KEY(card_player_id,card_game_id,moment),
    FOREIGN KEY (card_player_id) REFERENCES PLAYERS(_id) ON DELETE CASCADE,
	FOREIGN KEY (card_game_id) REFERENCES GAMES(_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GOALS(
	goal_game_id INT NOT NULL,
	goal_player_id INT NOT NULL,
	moment TIME NOT NULL,
	PRIMARY KEY(goal_game_id,goal_player_id,moment),
	FOREIGN KEY (goal_game_id) REFERENCES GAMES(_id) ON DELETE CASCADE,
	FOREIGN KEY (goal_player_id) REFERENCES PLAYERS(_id) ON DELETE CASCADE
);