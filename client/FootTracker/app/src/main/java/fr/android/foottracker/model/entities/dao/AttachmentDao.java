package fr.android.foottracker.model.entities.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.List;

import fr.android.foottracker.model.database.DbContract;
import fr.android.foottracker.model.entities.dao.common.AbstractDao;
import fr.android.foottracker.model.entities.data.AttachmentData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class AttachmentDao extends AbstractDao {
    public AttachmentDao(@NonNull SQLiteDatabase db) {
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
        final ContentValues valuesToInsert = getGameContentValuesToInsert((AttachmentData)data);
        return db.insert(DbContract.AttachmentEntry.TABLE_NAME, null, valuesToInsert);
        }


    @Override
    public long update(@NonNull AbstractIdentifiedData data) {
        return 0;
    }


    private ContentValues getGameContentValuesToInsert(@NonNull AttachmentData currentAttachment){
        final ContentValues values = new ContentValues();
        values.put(DbContract.AttachmentEntry.COLUMN_NAME_ATTACHMENT_PATH, currentAttachment.getPath());
        values.put(DbContract.AttachmentEntry.COLUMN_NAME_ATTACHMENT_GAME_ID, currentAttachment.getGameId());
        return values;
    }
}
