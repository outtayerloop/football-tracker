package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.dao.common.IdentityDao;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.entities.data.ChampionshipData;

// DAO d'interaction avec la table CHAMPIONSHIPS de la base de donnees SQLite.
public class ChampionshipDao extends AbstractDao
                             implements IdentityDao {

    public ChampionshipDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    /***
     * Recupere l'ensemble des competitions.
     * @return La liste de toutes les competitions.
     */
    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.ChampionshipEntry._ID, DbContract.ChampionshipEntry.COLUMN_NAME_CHAMPIONSHIP_NAME};
        final Cursor cursor = db.query(DbContract.ChampionshipEntry.TABLE_NAME, projection, null, null, null, null, null);
        final List<AbstractIdentifiedData> championships = new ArrayList<>();
        while (cursor.moveToNext())
            addNewChampionshipToListFromCursor(cursor, championships);
        cursor.close();
        return championships;
    }

    /***
     * Cree une nouvelle competition.
     * @param data Donnees de creation de la competition.
     * @return L'identifiant de la competition creee.
     */
    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues values = new ContentValues();
        if (data.getId() > 0)
            values.put(DbContract.ChampionshipEntry._ID, data.getId());
        values.put(DbContract.ChampionshipEntry.COLUMN_NAME_CHAMPIONSHIP_NAME, ((ChampionshipData) data).getName());
        return db.insert(DbContract.ChampionshipEntry.TABLE_NAME, null, values);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0;}


    /***
     * Retourne la competition dont l'identifiant est egal à celui fourni.
     * @param id Identifiant de la competition recherchee.
     * @return La competition dont l'identifiant correspond à celui fourni.
     */
    @Nullable
    @Override
    public AbstractIdentifiedData getById(long id) {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.ChampionshipEntry._ID, DbContract.ChampionshipEntry.COLUMN_NAME_CHAMPIONSHIP_NAME};
        final Cursor cursor = db.query(DbContract.ChampionshipEntry.TABLE_NAME, projection, DbContract.ChampionshipEntry._ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
        final AbstractIdentifiedData foundChampionship = getFoundChampionshipFromCursor(cursor);
        cursor.close();
        return foundChampionship;
    }

    /**
     * Parcourt le curseur et pour chaque competition de celui-ci, l'ajoute à la liste.
     * @param cursor Curseur contenant les competitions recuperees.
     * @param championships Liste des competitions qu'on veut remplir.
     */
    private void addNewChampionshipToListFromCursor(@NonNull Cursor cursor, @NonNull List<AbstractIdentifiedData> championships) {
        final int idColumnIndex = cursor.getColumnIndex(DbContract.ChampionshipEntry._ID);
        if (idColumnIndex != -1) {
            final AbstractIdentifiedData championship = getChampionshipFromData(cursor, idColumnIndex);
            championships.add(championship);
        }
    }

    /***
     * Recupere une competition depuis un curseur de recherche de competition par identifiant.
     * @param cursor Curseur de recherche de competition par identifiant.
     * @return La competition contenue dans le curseur ou null si le curseur ne contient aucune ligne.
     */
    private AbstractIdentifiedData getFoundChampionshipFromCursor(@NonNull Cursor cursor){
        AbstractIdentifiedData foundChampionship = null;
        if (cursor.moveToNext()){
            final int idColumnIndex = cursor.getColumnIndex(DbContract.ChampionshipEntry._ID);
            if (idColumnIndex != -1)
                foundChampionship = getChampionshipFromData(cursor, idColumnIndex);
        }
        return foundChampionship;
    }

    /***
     * Recupere une competition depuis une ligne du curseur contenant plusieurs competitions.
     * @param cursor Curseur ayant recupere toutes les competitions.
     * @param idColumnIndex Index de la colonne "ID" de la table CHAMPIONSHIPS.
     * @return La competition construite depuis les donnees de la ligne du curseur.
     */
    @NonNull
    private AbstractIdentifiedData getChampionshipFromData(@NonNull Cursor cursor, int idColumnIndex) {
        final long championshipId = cursor.getLong(idColumnIndex);
        final String championshipName = cursor.getString(cursor.getColumnIndex(DbContract.ChampionshipEntry.COLUMN_NAME_CHAMPIONSHIP_NAME));
        return new ChampionshipData(championshipId, championshipName);
    }
}
