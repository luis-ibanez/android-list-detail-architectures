package com.luisibanez.mvp.datasource.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by libanez on 22/06/2016.
 */
public class GamesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Games.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GamesPersistenceContract.GameEntry.TABLE_NAME + " (" +
                    GamesPersistenceContract.GameEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME + TEXT_TYPE + COMMA_SEP +
                    GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE + INTEGER_TYPE + COMMA_SEP +
                    GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT + INTEGER_TYPE +
                    " )";

    public GamesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}

