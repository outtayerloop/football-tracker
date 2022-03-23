package fr.android.foottracker.model.entities.dao.common;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

// Classe mere de tous les DAO.
public abstract class AbstractDao implements FootTrackerDao {

    // Base de donnees SQLite.
    private final SQLiteDatabase db;

    public AbstractDao(@NonNull SQLiteDatabase db) {
        this.db = db;
    }

    protected SQLiteDatabase getDatabase() {
        return db;
    }
}
