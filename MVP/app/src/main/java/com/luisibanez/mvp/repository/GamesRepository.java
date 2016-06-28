package com.luisibanez.mvp.repository;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.model.Game;

import java.util.List;

/**
 * Created by libanez on 28/06/2016.
 */
public interface GamesRepository {

    interface LoadGamesCallback {

        void onGamesLoaded(List<Game> games);

        void onDataNotAvailable();
    }

    interface GetGameCallback {

        void onGameLoaded(Game game);

        void onDataNotAvailable();
    }

    void getGames(boolean refreshFromApi, @NonNull LoadGamesCallback callback);

    void getGame(@NonNull String taskId, @NonNull GetGameCallback callback);
}