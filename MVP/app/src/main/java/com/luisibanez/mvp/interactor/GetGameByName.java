package com.luisibanez.mvp.interactor;

import com.luisibanez.mvp.datasource.model.Game;

import java.util.List;

/**
 * Created by libanez on 27/06/2016.
 */
public interface GetGameByName {
    interface Callback {
        void onGameLoaded(final Game game);
        void onError();
    }

    void execute(String gameName, final Callback callback);
}
