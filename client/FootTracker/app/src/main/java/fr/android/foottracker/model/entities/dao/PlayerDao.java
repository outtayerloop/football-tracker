package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GoalData;

// DAO d'interaction avec la table PLAYERS de la base de donnees SQLite.
public class PlayerDao extends AbstractDao {

    public PlayerDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    /***
     * Recupere l'ensemble des joueurs.
     * @return La liste de tous les joueurs.
     */
    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.PlayerEntry._ID, DbContract.PlayerEntry.COLUMN_NAME_PLAYER_FULL_NAME, DbContract.PlayerEntry.COLUMN_NAME_PLAYER_POSITION};
        final Cursor cursor = db.query(DbContract.PlayerEntry.TABLE_NAME, projection, null, null, null, null, null);
        final List<AbstractIdentifiedData> players = new ArrayList<>();
        while (cursor.moveToNext())
            addNewPlayerToListFromCursor(cursor, players);
        cursor.close();
        return players;
    }

    /***
     * Cree un nouveau joueur.
     * @param data Donnees de creation du joueur.
     * @return L'identifiant du joueur cree.
     */
    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues values = new ContentValues();
        if (data.getId() > 0)
            values.put(DbContract.PlayerEntry._ID, data.getId());
        values.put(DbContract.PlayerEntry.COLUMN_NAME_PLAYER_FULL_NAME, ((PlayerData) data).getName());
        values.put(DbContract.PlayerEntry.COLUMN_NAME_PLAYER_POSITION, ((PlayerData) data).getPosition());
        return db.insert(DbContract.PlayerEntry.TABLE_NAME, null, values);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0;}

    /***
     * Recupere la liste des joueurs de l'equipe dont l'identifiant est egal à celui passe en parametre.
     * @param teamId Identifiant de l'equipe dont on veut recuperer les joueurs.
     * @return La liste des joueurs de l'equipe (vide si aucun joueur).
     */
    @NonNull
    public List<AbstractIdentifiedData> getTeamPlayers(long teamId) {
        final SQLiteDatabase db = getDatabase();
        final String teamQuery = "SELECT " + DbContract.PlayerEntry._ID + ", " + DbContract.PlayerEntry.COLUMN_NAME_PLAYER_FULL_NAME + ", " + DbContract.PlayerEntry.COLUMN_NAME_PLAYER_POSITION + " FROM " + DbContract.PlayerEntry.TABLE_NAME + " INNER JOIN " + DbContract.MembershipEntry.TABLE_NAME + " ON " + DbContract.PlayerEntry._ID + " = " + DbContract.MembershipEntry.COLUMN_NAME_MEMBERSHIP_PLAYER_ID + " WHERE " + DbContract.MembershipEntry.COLUMN_NAME_MEMBERSHIP_TEAM_ID + " = ?";
        final Cursor playerCursor = db.rawQuery(teamQuery, new String[]{"" + teamId});
        final List<AbstractIdentifiedData> players = new ArrayList<>();
        while (playerCursor.moveToNext())
            addNewPlayerToListFromCursor(playerCursor, players);
        playerCursor.close();
        return players;
    }

    /***
     * Recupere l'id du joueur dont on possède le nom complet
     * @param playerName Nom complet du joueur
     * @return l'id du joueur.
     */
    public long getPlayerIdByName(String playerName){
        final SQLiteDatabase db = getDatabase();
        if(db==null)
            Log.d("PLAYERDAO: ", "DB NULL");
        final String playerIdQuery = "SELECT " + DbContract.PlayerEntry._ID + " FROM " + DbContract.PlayerEntry.TABLE_NAME + " WHERE " + DbContract.PlayerEntry.COLUMN_NAME_PLAYER_FULL_NAME + " = ?" ;
        final Cursor playerCursor = db.rawQuery(playerIdQuery, new String[]{"" + playerName});
        int idColumnIndex=0;
        if(playerCursor.getCount() == 1) {
            playerCursor.moveToFirst();
            idColumnIndex = playerCursor.getColumnIndex(DbContract.PlayerEntry._ID);
        }
        long result = playerCursor.getLong(idColumnIndex);
        playerCursor.close();
        return result;

    }

    /**
     * Parcourt le curseur et pour chaque joueur de celui-ci, l'ajoute à la liste.
     * @param cursor Curseur contenant les joueurs recuperes.
     * @param players Liste des joueurs qu'on veut remplir.
     */
    private void addNewPlayerToListFromCursor(@NonNull Cursor cursor, @NonNull List<AbstractIdentifiedData> players) {
        final int idColumnIndex = cursor.getColumnIndex(DbContract.PlayerEntry._ID);
        if (idColumnIndex != -1) {
            final long playerId = cursor.getLong(idColumnIndex);
            final String playerFullName = cursor.getString(cursor.getColumnIndex(DbContract.PlayerEntry.COLUMN_NAME_PLAYER_FULL_NAME));
            final String playerPosition = cursor.getString(cursor.getColumnIndex(DbContract.PlayerEntry.COLUMN_NAME_PLAYER_POSITION));
            final PlayerData player = new PlayerData(playerId, playerFullName, playerPosition);
            setPlayerCards(player);
            setPlayerGoals(player);
            players.add(player);
        }
    }

    private void setPlayerCards(PlayerData player){
        final CardDao cardDao = (CardDao) DaoFactory.create(getDatabase(), CardData.class);
        final List<CardData> cards = cardDao.getPlayerCards(player.getId());
        player.setCards(cards);
    }

    private void setPlayerGoals(PlayerData player){
        final GoalDao goalDao = (GoalDao) DaoFactory.create(getDatabase(), GoalData.class);
        final List<GoalData> goals = goalDao.getPlayerGoals(player.getId());
        player.setGoals(goals);
    }
}