package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.dao.common.DaoFactory;
import fr.android.foottracker.model.entities.dao.common.IdentityDao;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;

// DAO d'interaction avec la table GAMES de la base de donnees SQLite.
public class GameDao extends AbstractDao {

    public GameDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    /***
     * Retourne les 5 derniers matchs uniquement. L'ensemble des matchs est stocke sur la BD externe.
     * @return Les 5 derniers matchs.
     */
    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        final SQLiteDatabase db = getDatabase();
        final String[] projection = getAllGamesProjection();
        final Cursor cursor = db.query(DbContract.GameEntry.TABLE_NAME, projection, null, null, null, null, null);
        final List<AbstractIdentifiedData> games = new ArrayList<>();
        while (cursor.moveToNext())
            addNewGameToListFromCursor(cursor, games);
        cursor.close();
        return games;
    }

    /**
     * S'il y avait deja 5 matchs dans la BD alors celui d'ID minimal est supprimé
     * puis le match actuel est inséré dans la BD.
     * @param data Match que l'on veut inserer.
     */
    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final int MAX_STORED_GAME_COUNT = 5;
        final SQLiteDatabase db = getDatabase();
        final long gameCount = DatabaseUtils.queryNumEntries(db, DbContract.GameEntry.TABLE_NAME);
        if(gameCount == MAX_STORED_GAME_COUNT)
            deleteOldestGame();
        return createGame((GameData)data);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues valuesToUpdate = getGameContentValuesToUpdate((GameData)data);
        db.update(DbContract.GameEntry.TABLE_NAME, valuesToUpdate, DbContract.GameEntry._ID + " = ?", new String[]{Long.toString(data.getId())});
        return data.getId();
    }


    /***
     * Retourne la projection SQL (SELECT) permettant de recuperer l'ensemble des matchs.
     * @return La projection SQL (SELECT) permettant de recuperer l'ensemble des matchs.
     */
    private String[] getAllGamesProjection(){
        return new String[]{
                DbContract.GameEntry._ID,
                DbContract.GameEntry.COLUMN_NAME_GAME_DATETIME,
                DbContract.GameEntry.COLUMN_NAME_GAME_PLACE,
                DbContract.GameEntry.COLUMN_NAME_GAME_VALIDATION_STATE,
                DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_SCORE,
                DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_SCORE,
                DbContract.GameEntry.COLUMN_NAME_GAME_OUT_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_PENALTY_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_FREE_KICK_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_CORNER_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_OCCASION_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_FAULT_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_STOP_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_OFFSIDE_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_OVERTIME,
                DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_POSSESSION_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_POSSESSION_COUNT,
                DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_ID,
                DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_ID,
                DbContract.GameEntry.COLUMN_NAME_GAME_CHAMPIONSHIP_ID,
                DbContract.GameEntry.COLUMN_NAME_GAME_REFEREE_ID
        };
    }

    /***
     * Parcourt le curseur et pour chaque match de celui-ci, l'ajoute à la liste.
     * @param cursor Curseur contenant les matchs recuperes.
     * @param games Liste des matchs qu'on veut remplir.
     */
    private void addNewGameToListFromCursor(@NonNull Cursor cursor, @NonNull List<AbstractIdentifiedData> games) {
        final int idColumnIndex = cursor.getColumnIndex(DbContract.GameEntry._ID);
        if (idColumnIndex != -1) {
            final GameData game = getInitializedGame(cursor, idColumnIndex);
            final boolean isValidated = getValidationStatus(cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_VALIDATION_STATE)));
            game.setValidationState(isValidated);
            setGameStatisticsData(game, cursor);
            games.add(game);
        }
    }

    /***
     * Dans SQLite les booleens etant stockes comme des entiers (0 pour false, 1 pour true),
     * on retourne le mapping correspondant ici d'entier SQLite vers booleen de statut de validation
     * d'un match.
     * @param isValidated Determine si le match est valide ou non.
     * @return Le statut de validation d'un match.
     */
    private boolean getValidationStatus(int isValidated){
        if(isValidated == 0)
            return false;
        else if(isValidated == 1)
            return true;
        else
            throw new IllegalArgumentException("Game validation status stored in the SQLite database must be 0 or 1.");
    }

    /***
     * Recupere un match depuis une ligne du curseur actuel.
     * @param cursor Curseur contenant l'ensemble des matchs recuperes.
     * @param idColumnIndex Index de la colonne "ID" de la table GAMES.
     * @return Le match construit depuis les donnees de la ligne du curseur.
     */
    private GameData getInitializedGame(@NonNull Cursor cursor, int idColumnIndex){
        final long gameId = cursor.getLong(idColumnIndex);
        final String dateTime = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_DATETIME));
        final String address = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_PLACE));
        final TeamData trackedTeam = getGameDataById(cursor, DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_ID, TeamData.class);
        final TeamData opponentTeam = getGameDataById(cursor, DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_ID, TeamData.class);
        final ChampionshipData championship = getGameDataById(cursor, DbContract.GameEntry.COLUMN_NAME_GAME_CHAMPIONSHIP_ID, ChampionshipData.class);
        final RefereeData referee = getGameDataById(cursor, DbContract.GameEntry.COLUMN_NAME_GAME_REFEREE_ID, RefereeData.class);
        return new GameData(gameId, dateTime, address, trackedTeam, opponentTeam, referee, championship);
    }

    /***
     * Recupere un des attributs objet d'un match depuis une ligne du curseur
     * contenant entre autres l'ID de l'attribut. Invoque le DAO de l'attribut pour le recuperer par son ID.
     * @param cursor Curseur contenant les donnees de tous les matchs recuperes.
     * @param idColumn Index de la colonne "ID" de la table GAMES.
     * @param dataClass Classe de l'attribut objet à recuperer depuis la ligne.
     * @param <TData> Type de donnee de l'attribut objet.
     * @return L'attribut objet construit depuis la ligne du curseur.
     */
    @SuppressWarnings("unchecked")
    private <TData extends AbstractIdentifiedData> TData getGameDataById(@NonNull Cursor cursor, @NonNull String idColumn, @NonNull Class<TData> dataClass){
        final int gameDataId = cursor.getInt(cursor.getColumnIndex(idColumn));
        final IdentityDao dao = (IdentityDao)DaoFactory.create(getDatabase(), dataClass);
        final TData foundGameData = (TData)dao.getById(gameDataId);
        if(foundGameData == null)
            throw new IllegalArgumentException("Provided ID " + gameDataId + " does not exist in database.");
        else
            return foundGameData;
    }

    /***
     * Recupere les donnees d'un match depuis une ligne du curseur.
     * @param game Match à construire.
     * @param cursor Curseur.
     */
    private void setGameStatisticsData(@NonNull GameData game, @NonNull Cursor cursor){
        final int trackedTeamScore = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_SCORE));
        game.setTrackedTeamScore(trackedTeamScore);
        final int opponentTeamScore = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_SCORE));
        game.setOpponentScore(opponentTeamScore);
        final int outCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OUT_COUNT));
        game.setOutCount(outCount);
        final int penaltyCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_PENALTY_COUNT));
        game.setPenaltyCount(penaltyCount);
        final int freeKickCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_FREE_KICK_COUNT));
        game.setFreeKickCount(freeKickCount);
        final int cornerCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_CORNER_COUNT));
        game.setCornerCount(cornerCount);
        final int occasionCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OCCASION_COUNT));
        game.setOccasionCount(occasionCount);
        final int faultCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_FAULT_COUNT));
        game.setFaultCount(faultCount);
        final int stopCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_STOP_COUNT));
        game.setStopCount(stopCount);
        final int offsideCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OFFSIDE_COUNT));
        game.setOffsideCount(offsideCount);
        final String overtime = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OVERTIME));
        game.setOverTime(overtime);
        final int trackedTeamPossessionCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_POSSESSION_COUNT));
        game.setTrackedTeamPossessionCount(trackedTeamPossessionCount);
        final int opponentTeamPossessionCount = cursor.getInt(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_POSSESSION_COUNT));
        game.setOpponentTeamPossessionCount(opponentTeamPossessionCount);
    }

    // Suppression du match d'ID minimal parmi ceux existants dans la table GAMES.
    private void deleteOldestGame(){
        final SQLiteDatabase db = getDatabase();
        long gameIdToDelete = getOldestGameId();
        final int deletedRowsCount = db.delete(DbContract.GameEntry.TABLE_NAME, DbContract.GameEntry._ID + " = ?", new String[]{Long.toString(gameIdToDelete)});
        if(deletedRowsCount != 1)
            throw new SQLException("Oldest game could not be deleted in SQLite database.");
    }

    // Recupere l'identifiant le plus petit de la table GAMES.
    private long getOldestGameId(){
        final SQLiteDatabase db = getDatabase();
        final String minGameIdQuery = "SELECT MIN(" + DbContract.GameEntry._ID + ") FROM " + DbContract.GameEntry.TABLE_NAME;
        final Cursor minGameIdCursor = db.rawQuery(minGameIdQuery, null);
        return minGameIdCursor.moveToNext()
                ? getGameIdFromCursor(minGameIdCursor)
                : handleEmptyGameIdCursor(minGameIdCursor);
    }

    /***
     * Recupere l'identifiant du match de plus petit identifiant (que l'on veut supprimer).
     * @param cursor Curseur contenant le resultat de la requete de selection du plus petit identifiant de
     * la table GAMES.
     * @return l'identifiant le plus petit de la table GAMES.
     */
    private long getGameIdFromCursor(@NonNull Cursor cursor){
        final long gameToDeleteId = cursor.getLong(0);
        cursor.close();
        return gameToDeleteId;
    }

    /***
     * Gestion du cas où on veut supprimer le match de plus petit identifiant de la table GAMES mais que la
     * table GAMES ne contient aucun match.
     * @param cursor Curseur vide (selection du match de plus petit identifiant de la table GAMES).
     * @return Leve systematiquement une exception signifiant l'impossibilite d'effectuer l'action.
     */
    private long handleEmptyGameIdCursor(@NonNull Cursor cursor) {
        cursor.close();
        throw new UnsupportedOperationException("Can not delete from empty game table in SQLite database.");
    }

    /**
     * Insertion des donnees de creation d'un match dans la table GAMES.
     * @param newGame Donnees de creation d'un match.
     * @return L'identifiant du match cree.
     */
    private long createGame(@NonNull GameData newGame){
        final SQLiteDatabase db = getDatabase();
        final ContentValues values = getGameContentValuesToInsert(newGame);
        long createdGameId = db.insert(DbContract.GameEntry.TABLE_NAME, null, values);
        if(newGame.getTrackedTeam().getId() > 0)
            ensureTrackedTeamPlayersInsertion(newGame.getTrackedTeam());
        return createdGameId;
    }

    /***
     * Retourne l'ensemble des parametres de creation d'un nouveau match dans la table GAMES.
     * @param newGame Donnees de creation du match.
     * @return Un dictionnaire clé/valeur contenant les parametres de creation du match.
     */
    private ContentValues getGameContentValuesToInsert(@NonNull GameData newGame){
        final ContentValues values = new ContentValues();
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_DATETIME, newGame.getDateTime());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_PLACE, newGame.getAddress());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_VALIDATION_STATE, getValidationStatusToInsert(newGame.isValidated()));
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_SCORE, newGame.getTrackedTeamScore());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_SCORE, newGame.getOpponentScore());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OUT_COUNT, newGame.getOutCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_PENALTY_COUNT, newGame.getPenaltyCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_FREE_KICK_COUNT, newGame.getFreeKickCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_CORNER_COUNT, newGame.getCornerCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OCCASION_COUNT, newGame.getOccasionCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_FAULT_COUNT, newGame.getFaultCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_STOP_COUNT, newGame.getStopCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OFFSIDE_COUNT, newGame.getOffsideCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OVERTIME, newGame.getOvertimeToInsertInDatabase());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_POSSESSION_COUNT, newGame.getTrackedTeamPossessionCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_POSSESSION_COUNT, newGame.getOpponentTeamPossessionCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_ID, getNewGameDataIdToInsert(newGame.getTrackedTeam(), TeamData.class));
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_ID, getNewGameDataIdToInsert(newGame.getOpponentTeam(), TeamData.class));
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_CHAMPIONSHIP_ID, getNewGameDataIdToInsert(newGame.getChampionship(), ChampionshipData.class));
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_REFEREE_ID, getNewGameDataIdToInsert(newGame.getReferee(), RefereeData.class));
        return values;
    }

    /***
     * Retourne le mapping du statut de validation d'un match (ici de booleen vers entier SQLite).
     * @param isValidated Determine si le match est valide ou pas.
     * @return L'entier SQLite de statut de validation d'un match.
     */
    private int getValidationStatusToInsert(boolean isValidated){
        return isValidated ? 1 : 0;
    }

    /***
     * Retourne l'identifiant de l'attribut objet d'un nouveau match en cours de creation.
     * @param newGameData Donnees de creation du match.
     * @param dataClass Classe de donnees de l'attribut objet cible.
     * @param <TData> Type de donnees de l'attribut objet cible.
     * @return L'identifiant de l'attribut objet d'un nouveau match en cours de creation.
     */
    private <TData extends AbstractIdentifiedData> long getNewGameDataIdToInsert(@NonNull TData newGameData, @NonNull Class<TData> dataClass){
        if(newGameData.getId() > 0)
            return newGameData.getId();
        final AbstractDao dao = DaoFactory.create(getDatabase(), dataClass);
        return dao.insert(newGameData);
    }

    /***
     * Comme les equipes adverses n'ont pas de remplissage de membres d'equipes, un decalage peut
     * survenir si le coach selectionne une des equipes precedemment adverses presentes dans la base
     * comme equpe suivie. En effet celle-ci n'aura aucun joueur. Pour remedier à ça, on verifie si l'equipe
     * suivie existante actuelle a des joueurs dans la BD. Si elle n'en a pas, on les met à jour.
     * @param trackedTeam Equipe suivie (doit avoir des joueurs).
     */
    private void ensureTrackedTeamPlayersInsertion(@NonNull TeamData trackedTeam){
        final PlayerDao playerDao = (PlayerDao) DaoFactory.create(getDatabase(), PlayerData.class);
        final List<AbstractIdentifiedData> teamPlayers = playerDao.getTeamPlayers(trackedTeam.getId());
        if(teamPlayers.isEmpty()){
            final TeamDao teamDao = (TeamDao) DaoFactory.create(getDatabase(), TeamData.class);
            teamDao.updateTeamPlayers(trackedTeam.getId(), trackedTeam);
        }
    }
    /***
     * Retourne l'ensemble des parametres à actualiser d'un match, sous forme clé/valeur, dans la table GAMES.
     * @param currentGame Donnees du match courant.
     * @return Un dictionnaire clé/valeur contenant les parametres de creation du match.
     */
    private ContentValues getGameContentValuesToUpdate(@NonNull GameData currentGame){
        final ContentValues values = new ContentValues();
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_VALIDATION_STATE, getValidationStatusToInsert(currentGame.isValidated()));
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_SCORE, currentGame.getTrackedTeamScore());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_SCORE, currentGame.getOpponentScore());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OUT_COUNT, currentGame.getOutCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_PENALTY_COUNT, currentGame.getPenaltyCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_FREE_KICK_COUNT, currentGame.getFreeKickCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_CORNER_COUNT, currentGame.getCornerCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OCCASION_COUNT, currentGame.getOccasionCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_FAULT_COUNT, currentGame.getFaultCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_STOP_COUNT, currentGame.getStopCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OFFSIDE_COUNT, currentGame.getOffsideCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OVERTIME, currentGame.getOvertimeToInsertInDatabase());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_TRACKED_TEAM_POSSESSION_COUNT, currentGame.getTrackedTeamPossessionCount());
        values.put(DbContract.GameEntry.COLUMN_NAME_GAME_OPPONENT_TEAM_POSSESSION_COUNT, currentGame.getOpponentTeamPossessionCount());
        return values;
    }
}


