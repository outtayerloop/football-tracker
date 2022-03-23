package fr.android.foottracker.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "foot_tracker_db";

    private final static String CREATION_SQL_FILE_NAME = "foot_tracker_db_creation.sql";

    private final static String DELETION_SQL_FILE_NAME = "foot_tracker_db_deletion.sql";

    private final List<String> SQL_CREATE_ENTRIES;

    private final List<String> SQL_DELETE_ENTRIES;

    private final Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQL_CREATE_ENTRIES = getSqlEntries(CREATION_SQL_FILE_NAME);
        SQL_DELETE_ENTRIES = getSqlEntries(DELETION_SQL_FILE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQL_CREATE_ENTRIES.forEach(db::execSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQL_DELETE_ENTRIES.forEach(db::execSQL);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private List<String> getSqlEntries(String filename) {
        try {
            final InputStream creationInputStream = context.getAssets().open(filename);
            final String sqlEntries = IOUtils.toString(creationInputStream, StandardCharsets.UTF_8.name());
            return Arrays.asList(sqlEntries.split(";"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
