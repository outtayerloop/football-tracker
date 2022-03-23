package fr.android.foottracker.model.repositories.common;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.dao.common.FootTrackerDao;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class GenericLocalDataSource {

    @SuppressWarnings("unchecked")
    public <TData extends AbstractIdentifiedData> List<TData> getAll(@NonNull SQLiteDatabase db, @NonNull Class<TData> dataClass) {
        final FootTrackerDao dao = DaoFactory.create(db, dataClass);
        final List<AbstractIdentifiedData> dataList = dao.getAll();
        return dataList
                .stream()
                .map(data -> ((TData) data))
                .collect(Collectors.toList());
    }

}
