package fr.android.foottracker.model.entities.dao.common;

import androidx.annotation.Nullable;

import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public interface IdentityDao {

    @Nullable
    AbstractIdentifiedData getById(long id);

}
