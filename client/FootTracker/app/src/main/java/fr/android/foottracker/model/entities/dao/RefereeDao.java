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
import fr.android.foottracker.model.entities.data.RefereeData;

// DAO d'interaction avec la table REFEREES de la base de donnees SQLite.
public class RefereeDao extends AbstractDao
                        implements IdentityDao {

    public RefereeDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    /***
     * Recupere l'ensemble des arbitres.
     * @return La liste de tous les arbitres.
     */
    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.RefereeEntry._ID, DbContract.RefereeEntry.COLUMN_NAME_REFEREE_FULL_NAME};
        final Cursor cursor = db.query(DbContract.RefereeEntry.TABLE_NAME, projection, null, null, null, null, null);
        final List<AbstractIdentifiedData> referees = new ArrayList<>();
        while (cursor.moveToNext())
            addNewRefereeToListFromCursor(cursor, referees);
        cursor.close();
        return referees;
    }

    /***
     * Cree un nouvel arbitre.
     * @param data Donnees de creation de l'arbitre.
     * @return L'identifiant de l'arbitre cree.
     */
    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues values = new ContentValues();
        if (data.getId() > 0)
            values.put(DbContract.RefereeEntry._ID, data.getId());
        values.put(DbContract.RefereeEntry.COLUMN_NAME_REFEREE_FULL_NAME, ((RefereeData) data).getName());
        return db.insert(DbContract.RefereeEntry.TABLE_NAME, null, values);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0;}




    /***
     * Retourne l'arbitre dont l'identifiant est egal à celui fourni.
     * @param id Identifiant de l'arbitre recherchee.
     * @return L'arbitre dont l'identifiant correspond à celui fourni.
     */
    @Nullable
    @Override
    public AbstractIdentifiedData getById(long id) {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.RefereeEntry._ID, DbContract.RefereeEntry.COLUMN_NAME_REFEREE_FULL_NAME};
        final Cursor cursor = db.query(DbContract.RefereeEntry.TABLE_NAME, projection, DbContract.RefereeEntry._ID + "= ?", new String[]{Long.toString(id)}, null, null, null);
        final AbstractIdentifiedData foundReferee = getFoundRefereeFromCursor(cursor);
        cursor.close();
        return foundReferee;
    }

    /**
     * Parcourt le curseur et pour chaque arbitre de celui-ci, l'ajoute à la liste.
     * @param cursor Curseur contenant les arbitres recuperes.
     * @param referees Liste des arbitres qu'on veut remplir.
     */
    private void addNewRefereeToListFromCursor(@NonNull Cursor cursor, @NonNull List<AbstractIdentifiedData> referees) {
        final int idColumnIndex = cursor.getColumnIndex(DbContract.RefereeEntry._ID);
        if (idColumnIndex != -1) {
            final AbstractIdentifiedData referee = getRefereeFromData(cursor, idColumnIndex);
            referees.add(referee);
        }
    }

    /***
     * Recupere un arbitre depuis un curseur de recherche d'arbitre par identifiant.
     * @param cursor Curseur de recherche d'arbitre par identifiant.
     * @return L'arbitre contenu dans le curseur ou null si le curseur ne contient aucune ligne.
     */
    private AbstractIdentifiedData getFoundRefereeFromCursor(@NonNull Cursor cursor){
        AbstractIdentifiedData foundReferee = null;
        if (cursor.moveToNext()){
            final int idColumnIndex = cursor.getColumnIndex(DbContract.RefereeEntry._ID);
            if (idColumnIndex != -1)
                foundReferee = getRefereeFromData(cursor, idColumnIndex);
        }
        return foundReferee;
    }

    /***
     * Recupere un arbitre depuis une ligne du curseur contenant plusieurs arbitres.
     * @param cursor Curseur ayant recupere tous les arbitres.
     * @param idColumnIndex Index de la colonne "ID" de la table REFEREES.
     * @return L'arbitre construit depuis les donnees de la ligne du curseur.
     */
    @NonNull
    private AbstractIdentifiedData getRefereeFromData(@NonNull Cursor cursor, int idColumnIndex) {
        final long refereeId = cursor.getLong(idColumnIndex);
        final String refereeFullName = cursor.getString(cursor.getColumnIndex(DbContract.RefereeEntry.COLUMN_NAME_REFEREE_FULL_NAME));
        return new RefereeData(refereeId, refereeFullName);
    }
}
