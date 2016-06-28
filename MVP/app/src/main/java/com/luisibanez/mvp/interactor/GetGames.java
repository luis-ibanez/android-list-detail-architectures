package com.luisibanez.mvp.interactor;

import com.luisibanez.mvp.datasource.model.Game;

import java.util.List;

/**
 * Created by libanez on 27/06/2016.
 */
public interface GetGames {
    interface Callback {
        void onGamesLoaded(final List<Game> games);
        void onError();
    }

    void execute(boolean refreshFromApi, final Callback callback);
}
