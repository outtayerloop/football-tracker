USE foot_tracker_db;
INSERT INTO CHAMPIONSHIPS(championship_name) VALUES ('Euro');
INSERT INTO CHAMPIONSHIPS(championship_name) VALUES ('Coupe De France');
INSERT INTO CHAMPIONSHIPS(championship_name) VALUES ('Coupe De La Ligue');
INSERT INTO CHAMPIONSHIPS(championship_name) VALUES ('Ligue 1');
INSERT INTO CHAMPIONSHIPS(championship_name) VALUES ('Ligue 2');

INSERT INTO REFEREES(referee_full_name) VALUES ('Carlos Da Silva');
INSERT INTO REFEREES(referee_full_name) VALUES ('Karim Abed');
INSERT INTO REFEREES(referee_full_name) VALUES ('Benoît Bastien');
INSERT INTO REFEREES(referee_full_name) VALUES ('Florent Batta');
INSERT INTO REFEREES(referee_full_name) VALUES ('Hakim Ben El Hadj');

INSERT INTO TEAMS(team_name) VALUES ('Irlande');
INSERT INTO TEAMS(team_name) VALUES ('France');
INSERT INTO TEAMS(team_name) VALUES ('Girondins De Bordeaux');
INSERT INTO TEAMS(team_name) VALUES ('Olympique De Marseille');
INSERT INTO TEAMS(team_name) VALUES ('Paris Saint-Germain');

-- Team Irlande
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Vinny Arkins', 'Striker');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Dessie Baker', 'LeftMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Jimmy Balfe', 'RightMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Eric Barber', 'AttackingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Robert Bayly', 'CentralMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Pat Bonham', 'DefendingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Joe Boyce', 'LeftFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Kevin Brady', 'RightFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Eddie Brookes', 'LeftCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Arthur Browne', 'RightCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('David Crawley', 'Goalkeeper');

-- Team France
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Hugo Lloris', 'Striker');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Antoine Griezmann', 'LeftMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Lionel Messi', 'RightMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Cristiano Ronaldo', 'AttackingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Kylian Mbappé', 'CentralMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Zinédine Zidane', 'DefendingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Paul Pogba', 'LeftFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Karim Benzema', 'RightFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Zlatan Ibrahimovic', 'LeftCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Didier Deschamps', 'RightCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Diego Maradona', 'Goalkeeper');

-- Team Girondins de Bordeaux
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Michel Platini', 'Striker');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Mohamed Salah', 'LeftMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Raphaël Varane', 'RightMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Franck Ribéry', 'AttackingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Robert Lewandowski', 'CentralMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Ousmane Dembélé', 'DefendingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Haaland Erling Braut', 'LeftFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Olivier Giroud', 'RightFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Riyad Mahrez', 'LeftCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Johan Cruyff', 'RightCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Marco Verratti', 'Goalkeeper');

-- Team Olympique de Marseille
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Raheem Sterling', 'Striker');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Thierry Henry', 'LeftMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Jadon Sancho', 'RightMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Samuel Umtiti', 'AttackingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Gareth Bale', 'CentralMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Pochettino Mauricio', 'DefendingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Wissam Ben Yedder', 'LeftFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Raymond Kopa', 'RightFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Pep Guardiola', 'LeftCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('NGolo Kanté', 'RightCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Manuel Neuer', 'Goalkeeper');

-- Team Paris Saint-Germain
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Eduardo Camavinga', 'Striker');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Blaise Matuidi', 'LeftMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Anthony Martial', 'RightMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Ferland Mendy', 'AttackingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Steve Mandanda', 'CentralMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('David Beckham', 'DefendingMidfielder');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Romelu Lukaku', 'LeftFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Mathieu Valbuena', 'RightFullBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Sadio Mané', 'LeftCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Osvaldo Ardiles', 'RightCenterBack');
INSERT INTO PLAYERS (player_full_name, position) VALUES ('Igor Belanof', 'Goalkeeper');

-- Team Irlande
INSERT INTO MEMBERSHIPS VALUES(1, 1);
INSERT INTO MEMBERSHIPS VALUES(2, 1);
INSERT INTO MEMBERSHIPS VALUES(3, 1);
INSERT INTO MEMBERSHIPS VALUES(4, 1);
INSERT INTO MEMBERSHIPS VALUES(5, 1);
INSERT INTO MEMBERSHIPS VALUES(6, 1);
INSERT INTO MEMBERSHIPS VALUES(7, 1);
INSERT INTO MEMBERSHIPS VALUES(8, 1);
INSERT INTO MEMBERSHIPS VALUES(9, 1);
INSERT INTO MEMBERSHIPS VALUES(10, 1);
INSERT INTO MEMBERSHIPS VALUES(11, 1);

-- Team France
INSERT INTO MEMBERSHIPS VALUES(12, 2);
INSERT INTO MEMBERSHIPS VALUES(13, 2);
INSERT INTO MEMBERSHIPS VALUES(14, 2);
INSERT INTO MEMBERSHIPS VALUES(15, 2);
INSERT INTO MEMBERSHIPS VALUES(16, 2);
INSERT INTO MEMBERSHIPS VALUES(17, 2);
INSERT INTO MEMBERSHIPS VALUES(18, 2);
INSERT INTO MEMBERSHIPS VALUES(19, 2);
INSERT INTO MEMBERSHIPS VALUES(20, 2);
INSERT INTO MEMBERSHIPS VALUES(21, 2);
INSERT INTO MEMBERSHIPS VALUES(22, 2);

-- Team Girondins de Bordeaux
INSERT INTO MEMBERSHIPS VALUES(23, 3);
INSERT INTO MEMBERSHIPS VALUES(24, 3);
INSERT INTO MEMBERSHIPS VALUES(25, 3);
INSERT INTO MEMBERSHIPS VALUES(26, 3);
INSERT INTO MEMBERSHIPS VALUES(27, 3);
INSERT INTO MEMBERSHIPS VALUES(28, 3);
INSERT INTO MEMBERSHIPS VALUES(29, 3);
INSERT INTO MEMBERSHIPS VALUES(30, 3);
INSERT INTO MEMBERSHIPS VALUES(31, 3);
INSERT INTO MEMBERSHIPS VALUES(32, 3);
INSERT INTO MEMBERSHIPS VALUES(33, 3);

-- Team Olympique de Marseille
INSERT INTO MEMBERSHIPS VALUES(34, 4);
INSERT INTO MEMBERSHIPS VALUES(35, 4);
INSERT INTO MEMBERSHIPS VALUES(36, 4);
INSERT INTO MEMBERSHIPS VALUES(37, 4);
INSERT INTO MEMBERSHIPS VALUES(38, 4);
INSERT INTO MEMBERSHIPS VALUES(39, 4);
INSERT INTO MEMBERSHIPS VALUES(40, 4);
INSERT INTO MEMBERSHIPS VALUES(41, 4);
INSERT INTO MEMBERSHIPS VALUES(42, 4);
INSERT INTO MEMBERSHIPS VALUES(43, 4);
INSERT INTO MEMBERSHIPS VALUES(44, 4);

-- Team Paris Saint-Germain
INSERT INTO MEMBERSHIPS VALUES(45, 5);
INSERT INTO MEMBERSHIPS VALUES(46, 5);
INSERT INTO MEMBERSHIPS VALUES(47, 5);
INSERT INTO MEMBERSHIPS VALUES(48, 5);
INSERT INTO MEMBERSHIPS VALUES(49, 5);
INSERT INTO MEMBERSHIPS VALUES(50, 5);
INSERT INTO MEMBERSHIPS VALUES(51, 5);
INSERT INTO MEMBERSHIPS VALUES(52, 5);
INSERT INTO MEMBERSHIPS VALUES(53, 5);
INSERT INTO MEMBERSHIPS VALUES(54, 5);
INSERT INTO MEMBERSHIPS VALUES(55, 5);