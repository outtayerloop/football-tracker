package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class CardDao extends AbstractDao {

    public CardDao(@NonNull SQLiteDatabase db) {
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
        final ContentValues valuesToInsert = getGameContentValuesToInsert((CardData)data);
        return db.insert(DbContract.CardEntry.TABLE_NAME, null, valuesToInsert);
    }

    @Override
    public long update(@NonNull AbstractIdentifiedData data) {return 0;}


    public List<CardData> getPlayerCards(long playerId){
        final SQLiteDatabase db = getDatabase();
        final String cardQuery = "SELECT " + DbContract.CardEntry.COLUMN_NAME_CARD_TYPE + ", " + DbContract.CardEntry.COLUMN_NAME_CARD_PLAYER_ID + ", " + DbContract.CardEntry.COLUMN_NAME_CARD_GAME_ID + ", " + DbContract.CardEntry.COLUMN_NAME_CARD_MOMENT + " FROM " + DbContract.CardEntry.TABLE_NAME + " INNER JOIN " + DbContract.PlayerEntry.TABLE_NAME + " ON " + DbContract.CardEntry.COLUMN_NAME_CARD_PLAYER_ID + " = " + DbContract.PlayerEntry._ID + " WHERE " + DbContract.PlayerEntry._ID + " = ?";
        final Cursor cardCursor = db.rawQuery(cardQuery, new String[]{"" + playerId});
        final List<CardData> cards = new ArrayList<>();
        while (cardCursor.moveToNext())
            addNewCardToListFromCursor(cardCursor, cards);
        cardCursor.close();
        return cards;
    }

    private ContentValues getGameContentValuesToInsert(@NonNull CardData currentData){
        final ContentValues values = new ContentValues();
        values.put(DbContract.CardEntry.COLUMN_NAME_CARD_TYPE, currentData.getType());
        values.put(DbContract.CardEntry.COLUMN_NAME_CARD_GAME_ID, currentData.getGameId());
        values.put(DbContract.CardEntry.COLUMN_NAME_CARD_PLAYER_ID, currentData.getPlayerId());
        values.put(DbContract.CardEntry.COLUMN_NAME_CARD_MOMENT, currentData.getMoment());
        return values;
    }

    private void addNewCardToListFromCursor(@NonNull Cursor cursor, @NonNull List<CardData> cards){
        final String cardType = cursor.getString(cursor.getColumnIndex(DbContract.CardEntry.COLUMN_NAME_CARD_TYPE));
        final long playerId = cursor.getLong(cursor.getColumnIndex(DbContract.CardEntry.COLUMN_NAME_CARD_PLAYER_ID));
        final long gameId = cursor.getLong(cursor.getColumnIndex(DbContract.CardEntry.COLUMN_NAME_CARD_GAME_ID));
        final String moment = cursor.getString(cursor.getColumnIndex(DbContract.CardEntry.COLUMN_NAME_CARD_MOMENT));
        final CardData card = new CardData(cardType, playerId, gameId, moment);
        cards.add(card);
    }
}
