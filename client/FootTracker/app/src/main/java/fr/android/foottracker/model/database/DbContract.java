package fr.android.foottracker.model.database;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract() { }

    public static final class ChampionshipEntry implements BaseColumns {
        public static final String TABLE_NAME = "CHAMPIONSHIPS";
        public static final String COLUMN_NAME_CHAMPIONSHIP_NAME = "championship_name";

        private ChampionshipEntry() { }
    }

    public static final class RefereeEntry implements BaseColumns {
        public static final String TABLE_NAME = "REFEREES";
        public static final String COLUMN_NAME_REFEREE_FULL_NAME = "referee_full_name";

        private RefereeEntry() { }
    }

    public static final class TeamEntry implements BaseColumns {
        public static final String TABLE_NAME = "TEAMS";
        public static final String COLUMN_NAME_TEAM_NAME = "team_name";

        private TeamEntry() { }
    }

    public static final class PlayerEntry implements BaseColumns {
        public static final String TABLE_NAME = "PLAYERS";
        public static final String COLUMN_NAME_PLAYER_FULL_NAME = "player_full_name";
        public static final String COLUMN_NAME_PLAYER_POSITION = "position";

        private PlayerEntry() { }
    }

    public static final class MembershipEntry {
        public static final String TABLE_NAME = "MEMBERSHIPS";
        public static final String COLUMN_NAME_MEMBERSHIP_PLAYER_ID = "membership_player_id";
        public static final String COLUMN_NAME_MEMBERSHIP_TEAM_ID = "membership_team_id";

        private MembershipEntry() { }
    }

    public static final class AttachmentEntry implements BaseColumns{
        public static final String TABLE_NAME = "ATTACHMENTS";
        public static final String COLUMN_NAME_ATTACHMENT_PATH = "attachment_path";
        public static final String COLUMN_NAME_ATTACHMENT_GAME_ID = "attachment_game_id";

        private AttachmentEntry() { }
    }
    public static final class CardEntry{
        public static final String TABLE_NAME = "CARDS";
        public static final String COLUMN_NAME_CARD_TYPE="card_type";
        public static final String COLUMN_NAME_CARD_PLAYER_ID= "card_player_id";
        public static final String COLUMN_NAME_CARD_GAME_ID = "card_game_id";
        public static final String COLUMN_NAME_CARD_MOMENT = "moment";

        private CardEntry() { }
    }
    public static final class GoalEntry{
        public static final String TABLE_NAME = "GOALS";
        public static final String COLUMN_NAME_GOAL_GAME_ID ="goal_game_id";
        public static final String COLUMN_NAME_GOAL_PLAYER_ID = "goal_player_id";
        public static final String COLUMN_NAME_GOAL_MOMENT = "moment";

        private GoalEntry() { }
    }

    public static final class GameEntry implements BaseColumns {
        public static final String TABLE_NAME = "GAMES";
        public static final String COLUMN_NAME_GAME_DATETIME = "game_datetime";
        public static final String COLUMN_NAME_GAME_PLACE = "place";
        public static final String COLUMN_NAME_GAME_VALIDATION_STATE = "validated";
        public static final String COLUMN_NAME_GAME_TRACKED_TEAM_SCORE = "tracked_team_score";
        public static final String COLUMN_NAME_GAME_OPPONENT_TEAM_SCORE = "opponent_score";
        public static final String COLUMN_NAME_GAME_OUT_COUNT = "out_count";
        public static final String COLUMN_NAME_GAME_PENALTY_COUNT = "penalty_count";
        public static final String COLUMN_NAME_GAME_FREE_KICK_COUNT = "free_kick_count";
        public static final String COLUMN_NAME_GAME_CORNER_COUNT = "corner_count";
        public static final String COLUMN_NAME_GAME_OCCASION_COUNT = "occasion_count";
        public static final String COLUMN_NAME_GAME_FAULT_COUNT = "fault_count";
        public static final String COLUMN_NAME_GAME_STOP_COUNT = "stop_count";
        public static final String COLUMN_NAME_GAME_OFFSIDE_COUNT = "offside_count";
        public static final String COLUMN_NAME_GAME_OVERTIME = "overtime";
        public static final String COLUMN_NAME_GAME_TRACKED_TEAM_POSSESSION_COUNT = "tracked_team_possession_count";
        public static final String COLUMN_NAME_GAME_OPPONENT_TEAM_POSSESSION_COUNT = "opponent_team_possession_count";
        public static final String COLUMN_NAME_GAME_TRACKED_TEAM_ID = "tracked_team_id";
        public static final String COLUMN_NAME_GAME_OPPONENT_TEAM_ID = "opponent_team_id";
        public static final String COLUMN_NAME_GAME_CHAMPIONSHIP_ID = "championship_id";
        public static final String COLUMN_NAME_GAME_REFEREE_ID = "referee_id";

        private GameEntry() { }
    }
}
