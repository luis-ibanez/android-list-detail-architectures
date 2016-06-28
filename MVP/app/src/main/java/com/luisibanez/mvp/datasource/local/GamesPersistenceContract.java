package com.luisibanez.mvp.datasource.local;

import android.provider.BaseColumns;

/**
 * Created by libanez on 22/06/2016.
 *
 * The contract used for the db to save the tasks locally.
 */
public final class GamesPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public GamesPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class GameEntry implements BaseColumns {
        public static final String TABLE_NAME = "game";
        public static final String COLUMN_NAME_GAME_NAME = "name";
        public static final String COLUMN_NAME_GAME_DATE = "date";
        public static final String COLUMN_NAME_GAME_JACKPOT = "jackpot";
    }
}
