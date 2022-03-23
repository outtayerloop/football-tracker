package fr.android.foottracker.model.entities.data;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.entities.data.common.AbstractData;

public class ChampionshipData extends AbstractData {

    public ChampionshipData(long id, @NonNull String name) {
        super(id, name);
    }

    public ChampionshipData(@NonNull String name) {
        super(name);
    }
}
