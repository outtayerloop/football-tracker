package fr.android.foottracker.model.repositories.gameform;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.dao.common.FootTrackerDao;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.repositories.common.GenericLocalDataSource;

public class GameFormLocalDataSource extends GenericLocalDataSource {

    // Retourne l'ID de la donnee inseree.
    public <TData extends AbstractIdentifiedData> long insert(@NonNull SQLiteDatabase db, @NonNull TData data) {
        final FootTrackerDao dao = DaoFactory.create(db, data.getClass());
        return dao.insert(data);
    }
}
