package fr.android.foottracker.model.repositories.gameupdate;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.dao.common.FootTrackerDao;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class GameUpdateLocalDataSource {
    public <TData extends AbstractIdentifiedData> List<TData> getAll(@NonNull SQLiteDatabase db, @NonNull Class<TData> dataClass) {
        final FootTrackerDao dao = DaoFactory.create(db, dataClass);
        final List<AbstractIdentifiedData> dataList = dao.getAll();
        return dataList
                .stream()
                .map(data -> ((TData) data))
                .collect(Collectors.toList());
    }

    // Retourne l'ID de la donnee inseree.
    public <TData extends AbstractIdentifiedData> long insert(@NonNull SQLiteDatabase db, @NonNull TData data) {
        final FootTrackerDao dao = DaoFactory.create(db, data.getClass());
        return dao.insert(data);
    }

    // Retourne le nombre de lignes affectees par l'update..
    public <TData extends AbstractIdentifiedData> long update(@NonNull SQLiteDatabase db, @NonNull TData data) {
        final FootTrackerDao dao = DaoFactory.create(db, data.getClass());
        return dao.update(data);
    }

}
