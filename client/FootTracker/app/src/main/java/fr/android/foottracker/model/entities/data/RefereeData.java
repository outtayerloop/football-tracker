package fr.android.foottracker.model.entities.data;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.entities.data.common.AbstractData;

public class RefereeData extends AbstractData {

    public RefereeData(long id, @NonNull String fullName) {
        super(id, fullName);
    }

    public RefereeData(@NonNull String fullName) {
        super(fullName);
    }
}
