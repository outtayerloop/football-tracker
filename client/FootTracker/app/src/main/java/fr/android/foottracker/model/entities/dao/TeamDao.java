package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.dao.common.IdentityDao;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.TeamData;

// DAO d'interaction avec la table TEAMS de la base de donnees SQLite.
public class TeamDao extends AbstractDao
                     implements IdentityDao {

    public TeamDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    /***
     * Recupere l'ensemble des equipes avec pour chacune la liste de ses joueurs.
     * @return La liste de toutes les equipes avec pour chacune la liste de ses joueurs.
     */
    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.TeamEntry._ID, DbContract.TeamEntry.COLUMN_NAME_TEAM_NAME};
        final Cursor teamCursor = db.query(DbContract.TeamEntry.TABLE_NAME, projection, null, null, null, null, null);
        final List<AbstractIdentifiedData> teams = new ArrayList<>();
        while (teamCursor.moveToNext())
            addNewTeamToListFromCursor(teamCursor, teams);
        teamCursor.close();
        return teams;
    }

    /***
     * Cree une nouvelle equipe dans la table TEAMS. Insere egalement ses joueurs s'il y en a dans
     * la table MEMBERSHIPS. Si les joueurs n'existaient pas, les cree dans la table PLAYERS.
     * @param data Donnees de creation de l'equipe.
     * @return L'identifiant de l'equipe creee.
     */
    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues values = new ContentValues();
        if (data.getId() > 0)
            values.put(DbContract.TeamEntry._ID, data.getId());
        values.put(DbContract.TeamEntry.COLUMN_NAME_TEAM_NAME, ((TeamData) data).getName());
        final long newTeamId = db.insert(DbContract.TeamEntry.TABLE_NAME, null, values);
        if(((TeamData)data).hasPlayers())
            insertMemberships(newTeamId, db, ((TeamData)data).getPlayers());
        return newTeamId;
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0;}



    /***
     * Recupere une equipe (ainsi que ses joueurs) à partir de l'identifiant fourni.
     * @param id Identifiant de l'equipe recherchee.
     * @return L'equipe trouvee ou null si elle est introuvable.
     */
    @Nullable
    @Override
    public AbstractIdentifiedData getById(long id) {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = {DbContract.TeamEntry._ID, DbContract.TeamEntry.COLUMN_NAME_TEAM_NAME};
        final Cursor cursor = db.query(DbContract.TeamEntry.TABLE_NAME, projection, DbContract.TeamEntry._ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
        final AbstractIdentifiedData foundTeam = getFoundTeamFromCursor(cursor);
        cursor.close();
        return foundTeam;
    }

    /**
     * N'est destinée qu'à remplir les membres de l'equipe cible dans la table MEMBERSHIPS.
     * On suppose ici que cela n'a pas deja été fait, sinon une exception se produira par
     * violation de contrainte de clé primaire.
     * @param id Identifiant de l'equipe dont on veut remplir les joueurs.
     * @param data Donnees de mise à jour des joueurs de l'equipe.
     */
    public void updateTeamPlayers(long id, AbstractIdentifiedData data){
        final SQLiteDatabase db = getDatabase();
        if(!((TeamData)data).hasPlayers())
            throw new IllegalArgumentException("Can not update team from data with empty players list.");
        insertMemberships(id, db, ((TeamData)data).getPlayers());
    }

    /**
     * Parcourt le curseur et pour chaque equipe de celui-ci, l'ajoute à la liste.
     * @param cursor Curseur contenant les equipes recuperees.
     * @param teams Liste des equipes qu'on veut remplir.
     */
    private void addNewTeamToListFromCursor(@NonNull Cursor cursor, @NonNull List<AbstractIdentifiedData> teams) {
        final int idColumnIndex = cursor.getColumnIndex(DbContract.TeamEntry._ID);
        if (idColumnIndex != -1) {
            final AbstractIdentifiedData team = getTeamFromData(cursor, idColumnIndex);
            teams.add(team);
        }
    }

    /***
     * Gere l'insertion des joueurs d'une equipe dans la table MEMBERSHIPS.
     * Si les joueurs n'existaient pas deja, les cree egalement dans la table PLAYERS.
     * @param teamId Identifiant de l'equipe pour laquelle on veut inserer les joueurs.
     * @param db Base de données SQLite.
     * @param teamPlayers Joueurs de l'equipe qu'on veut inserer dans MEMBERSHIPS.
     */
    private void insertMemberships(long teamId, @NonNull SQLiteDatabase db, @NonNull List<PlayerData> teamPlayers) {
        teamPlayers.forEach(player -> {
            final ContentValues values = new ContentValues();
            long playerId = getPlayerToInsertId(player);
            values.put(DbContract.MembershipEntry.COLUMN_NAME_MEMBERSHIP_PLAYER_ID, playerId);
            values.put(DbContract.MembershipEntry.COLUMN_NAME_MEMBERSHIP_TEAM_ID, teamId);
            db.insert(DbContract.MembershipEntry.TABLE_NAME, null, values);
        });
    }

    /***
     * Recupere une equipe depuis un curseur de recherche d'equipe par identifiant.
     * @param cursor Curseur de recherche d'equipe par identifiant.
     * @return L'equipe contenue dans le curseur ou null si elle le curseur ne contient aucune ligne.
     */
    @Nullable
    private AbstractIdentifiedData getFoundTeamFromCursor(@NonNull Cursor cursor){
        AbstractIdentifiedData foundTeam = null;
        if (cursor.moveToNext()){
            final int idColumnIndex = cursor.getColumnIndex(DbContract.TeamEntry._ID);
            if (idColumnIndex != -1)
                foundTeam = getTeamFromData(cursor, idColumnIndex);
        }
        return foundTeam;
    }

    /***
     * Recupere une equipe depuis une ligne du curseur contenant plusieurs equipes.
     * @param cursor Curseur ayant recupere toutes les equipes.
     * @param idColumnIndex Index de la colonne "ID" de la table TEAMS.
     * @return L'equipe construite depuis les donnees de la ligne du curseur.
     */
    @NonNull
    private AbstractIdentifiedData getTeamFromData(@NonNull Cursor cursor, int idColumnIndex) {
        final long teamId = cursor.getLong(idColumnIndex);
        final List<PlayerData> teamPlayers = getTeamPlayers(teamId);
        final String teamName = cursor.getString(cursor.getColumnIndex(DbContract.TeamEntry.COLUMN_NAME_TEAM_NAME));
        return new TeamData(teamId, teamName, teamPlayers);
    }

    /***
     * Recupere la liste des joueurs d'une equipe dont on fournit l'identifiant.
     * @param teamId Identifiant de l'equipe dont on veut recuperer les joueurs.
     * @return La liste des joueurs de l'equipe cible.
     */
    @NonNull
    private List<PlayerData> getTeamPlayers(long teamId) {
        final PlayerDao playerDao = (PlayerDao) DaoFactory.create(getDatabase(), PlayerData.class);
        return playerDao.getTeamPlayers(teamId)
                .stream()
                .map(player -> (PlayerData) player)
                .collect(Collectors.toList());
    }

    /***
     * Recupere l'identifiant du joueur à insérer dans la table MEMBERSHIPS.
     * Si le joueur existe deja, retourne l'identifiant du joueur, sinon le cree à partir des donnees
     * et retourne l'identifiant du joueur cree.
     * @param player Donnees du joueur à insérer dans la table MEMBERSHIPS.
     * @return L'identifiant du joueur à insérer dans la table MEMBERSHIPS.
     */
    private long getPlayerToInsertId(PlayerData player){
        if(player.getId() == 0){
            final PlayerDao playerDao = (PlayerDao) DaoFactory.create(getDatabase(), PlayerData.class);
            return playerDao.insert(player);
        }
        return player.getId();
    }
}
