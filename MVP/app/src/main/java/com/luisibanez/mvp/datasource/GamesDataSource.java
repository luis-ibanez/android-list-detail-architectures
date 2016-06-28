package com.luisibanez.mvp.datasource;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.datasource.model.ResponseGame;

import java.util.List;

/**
 * Created by libanez on 22/06/2016.
 */
public interface GamesDataSource {

    interface LoadGamesCallback {

        void onGamesLoaded(ResponseGame responseGame);

        void onDataNotAvailable();
    }

    interface GetGameCallback {

        void onGameLoaded(Game game);

        void onDataNotAvailable();
    }

    void getGames(@NonNull LoadGamesCallback callback);

    void getGame(@NonNull String taskId, @NonNull GetGameCallback callback);

    void saveResponse(@NonNull ResponseGame responseGame);

    void deleteAllGames();

    boolean isValidData();
}
