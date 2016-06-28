package com.luisibanez.mvp.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;

import java.util.ArrayList;
import java.util.List;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

/**
 * Created by libanez on 22/06/2016.
 */
public class GamesLocalDataSource implements GamesDataSource {

    private static GamesLocalDataSource INSTANCE;

    private GamesDbHelper dbHelper;

    // Prevent direct instantiation.
    private GamesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        dbHelper = new GamesDbHelper(context);
    }

    public static GamesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GamesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getGames(@NonNull LoadGamesCallback callback) {
        List<Game> games = new ArrayList<Game>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME,
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE,
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT
        };

        Cursor c = db.query(
                GamesPersistenceContract.GameEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME));
                int jackpot = c.getInt(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT));
                String date =
                        c.getString(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE));
                Game game = new Game(name, jackpot, date);
                games.add(game);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (games.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onGamesLoaded(games);
        }

    }

    @Override
    public void getGame(@NonNull String gameName, @NonNull GetGameCallback callback) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME,
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT,
                GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE
        };

        String selection = GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME + " LIKE ?";
        String[] selectionArgs = { gameName };

        Cursor c = db.query(
                GamesPersistenceContract.GameEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Game game = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String name = c.getString(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME));
            int jackpot = c.getInt(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT));
            String date = c.getString(c.getColumnIndexOrThrow(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE));

            game = new Game(name, jackpot, date);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (game != null) {
            callback.onGameLoaded(game);
        } else {
            callback.onDataNotAvailable();
        }
    }

    public void saveGame(@NonNull Game game) {
        checkNotNull(game);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME, game.getName());
        values.put(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_JACKPOT, game.getFormattedJackpot());
        values.put(GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_DATE, game.getDate());

        db.insert(GamesPersistenceContract.GameEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteGame(@NonNull String gameName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = GamesPersistenceContract.GameEntry.COLUMN_NAME_GAME_NAME + " LIKE ?";
        String[] selectionArgs = { gameName };

        db.delete(GamesPersistenceContract.GameEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteAllGames() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(GamesPersistenceContract.GameEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void refreshGames() {
        // Not required because the {@link GamesRepositoryImpl} handles the logic of refreshing the
        // tasks from all the available data sources.
    }
}
