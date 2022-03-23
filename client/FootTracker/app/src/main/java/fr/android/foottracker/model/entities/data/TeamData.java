package fr.android.foottracker.model.entities.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.entities.data.common.AbstractData;

public class TeamData extends AbstractData {

    private final List<PlayerData> players;

    public TeamData(long id, @NonNull String name, @NonNull List<PlayerData> players) {
        super(id, name);
        this.players = players;
    }

    public TeamData(@NonNull String name) {
        super(name);
        players = new ArrayList<>();
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public boolean hasPlayers(){
        return players != null && !players.isEmpty();
    }
}
