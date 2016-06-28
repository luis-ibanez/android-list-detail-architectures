package com.luisibanez.mvp.datasource;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.model.Game;

import java.util.List;

/**
 * Created by libanez on 22/06/2016.
 */
public interface GamesDataSource {

    void refreshGames();

    interface LoadGamesCallback {

        void onGamesLoaded(List<Game> games);

        void onDataNotAvailable();
    }

    interface GetGameCallback {

        void onGameLoaded(Game game);

        void onDataNotAvailable();
    }

    void getGames(@NonNull LoadGamesCallback callback);

    void getGame(@NonNull String taskId, @NonNull GetGameCallback callback);

    void saveGame(@NonNull Game game);

    void deleteGame(@NonNull String gameName);

    void deleteAllGames();
}
