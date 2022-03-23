package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.data.GoalData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class GoalDao extends AbstractDao {

    public GoalDao(@NonNull SQLiteDatabase db) {
        super(db);
    }

    @NonNull
    @Override
    public List<AbstractIdentifiedData> getAll() {
        return null;
    }

    @Override
    public long insert(@NonNull AbstractIdentifiedData data) {
        final SQLiteDatabase db = getDatabase();
        final ContentValues valuesToInsert = getGameContentValuesToInsert((GoalData) data);
        return db.insert(DbContract.GoalEntry.TABLE_NAME, null, valuesToInsert);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0; }


    public List<GoalData> getPlayerGoals(long playerId){
        final SQLiteDatabase db = getDatabase();
        final String goalQuery = "SELECT " + DbContract.GoalEntry.COLUMN_NAME_GOAL_GAME_ID + ", " + DbContract.GoalEntry.COLUMN_NAME_GOAL_PLAYER_ID + ", " + DbContract.GoalEntry.COLUMN_NAME_GOAL_MOMENT + " FROM " + DbContract.GoalEntry.TABLE_NAME + " INNER JOIN " + DbContract.PlayerEntry.TABLE_NAME + " ON " + DbContract.GoalEntry.COLUMN_NAME_GOAL_PLAYER_ID + " = " + DbContract.PlayerEntry._ID + " WHERE " + DbContract.PlayerEntry._ID + " = ?";
        final Cursor goalCursor = db.rawQuery(goalQuery, new String[]{"" + playerId});
        final List<GoalData> goals = new ArrayList<>();
        while (goalCursor.moveToNext())
            addNewGoalToListFromCursor(goalCursor, goals);
        goalCursor.close();
        return goals;
    }

    private ContentValues getGameContentValuesToInsert(@NonNull GoalData currentGoal){
        final ContentValues values = new ContentValues();
        values.put(DbContract.GoalEntry.COLUMN_NAME_GOAL_GAME_ID, currentGoal.getGameId());
        values.put(DbContract.GoalEntry.COLUMN_NAME_GOAL_PLAYER_ID, currentGoal.getPlayerId());
        values.put(DbContract.GoalEntry.COLUMN_NAME_GOAL_MOMENT, currentGoal.getMoment().toString());
        return values;
    }

    private void addNewGoalToListFromCursor(@NonNull Cursor cursor, @NonNull List<GoalData> goals){
        final long gameId = cursor.getLong(cursor.getColumnIndex(DbContract.GoalEntry.COLUMN_NAME_GOAL_GAME_ID));
        final String moment = cursor.getString(cursor.getColumnIndex(DbContract.GoalEntry.COLUMN_NAME_GOAL_MOMENT));
        final long playerId = cursor.getLong(cursor.getColumnIndex(DbContract.GoalEntry.COLUMN_NAME_GOAL_PLAYER_ID));
        final GoalData goal = new GoalData(gameId, playerId, moment);
        goals.add(goal);
    }
}
