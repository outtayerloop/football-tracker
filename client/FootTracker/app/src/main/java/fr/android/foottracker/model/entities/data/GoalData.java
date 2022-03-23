package fr.android.foottracker.model.entities.data;


import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class GoalData extends AbstractIdentifiedData {

    private final long playerId;

    private final String moment;

    private final long gameId;

    public GoalData(long gameId, long playerId, String moment) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.moment = moment;
    }

    public String getMoment() {
        return moment;
    }

    public long getGameId() {
        return gameId;
    }
    public long getPlayerId() {
        return playerId;
    }
}