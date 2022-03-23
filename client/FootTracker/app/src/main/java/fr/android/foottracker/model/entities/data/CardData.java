package fr.android.foottracker.model.entities.data;


import fr.android.foottracker.model.entities.CardType;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class CardData extends AbstractIdentifiedData {

    private String type;

    private final long playerId;

    private final String moment;

    private final long gameId;

    public CardData(String type, long playerId, long gameId, String moment){
        this.gameId = gameId;
        this.type = type;
        this.playerId = playerId;
        this.moment = moment;
    }

    public String getMoment() {
        return moment;
    }

    public boolean isGameYellowCard(long gameId){
        return type.equals(CardType.YELLOW)
                && this.gameId == gameId;
    }

    public boolean isGameRedCard(long gameId){
        return type.equals(CardType.RED)
                && this.gameId == gameId;
    }
    public long getGameId() {
        return gameId;
    }
    public String getType() {
        return type;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setType(String type){
        this.type = type;
    }
}