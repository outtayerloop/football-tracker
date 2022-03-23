package fr.android.foottracker.model.entities.data;

        import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.entities.CardType;
import fr.android.foottracker.model.entities.data.common.AbstractData;

        import fr.android.foottracker.model.entities.data.common.AbstractData;
        import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class PlayerData extends AbstractData {

    private final String position;

    private List<CardData> cards;

    private List<GoalData> goals;

    public PlayerData(long id, @NonNull String fullName, @NonNull String position, List<CardData> cards, List<GoalData> goals) {
        super(id, fullName);
        this.position = position;
        this.cards = cards;
        this.goals = goals;
    }

    public PlayerData(long id, @NonNull String fullName, @NonNull String position) {
        super(id, fullName);
        this.position = position;
        cards = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public PlayerData(@NonNull String fullName, @NonNull String position) {
        super(fullName);
        this.position = position;
        cards = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public boolean hasPosition(@NonNull String position) {
        return this.position.equals(position);
    }

    public String getPosition() {
        return position;
    }

    public int getGameYellowCardCount(long gameId){
        if(cards == null)
            return 0;
        return (int) cards.stream()
                .filter(card -> card.isGameYellowCard(gameId))
                .count();
    }

    public int getGameRedCardCount(long gameId){
        if(cards == null)
            return 0;
        return (int) cards.stream()
                .filter(card -> card.isGameRedCard(gameId))
                .count();
    }

    public List<GoalData> getGoals(){
        return goals;
    }

    public List<CardData> getCards(){
        return cards;
    }

    public int getGameGoalCount(long gameId){
        if(goals == null)
            return 0;
        return (int) goals.stream()
                .filter(goal -> goal.getGameId() == gameId)
                .count();
    }

    public int getGameTotalCardCount(long gameId){
        return getGameYellowCardCount(gameId) + getGameRedCardCount(gameId);
    }

    public void addCardOrGoal(AbstractIdentifiedData data){
        if(data.getClass().equals(CardData.class)){
            if(cards == null)
                cards = new ArrayList<>();
            cards.add((CardData)data);
        }
        if(data.getClass().equals(GoalData.class)){
            if(goals == null)
                goals = new ArrayList<>();
            goals.add((GoalData)data);
        }
    }

    public void setCards(List<CardData> cards){
        this.cards = cards;
    }

    public void setGoals(List<GoalData> goals){
        this.goals = goals;
    }
}