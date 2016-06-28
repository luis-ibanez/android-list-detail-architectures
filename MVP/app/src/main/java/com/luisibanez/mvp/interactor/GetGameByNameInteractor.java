package com.luisibanez.mvp.interactor;

import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.executor.Executor;
import com.luisibanez.mvp.executor.Interactor;
import com.luisibanez.mvp.executor.MainThread;
import com.luisibanez.mvp.repository.GamesRepository;

/**
 * Created by libanez on 27/06/2016.
 */
public class GetGameByNameInteractor implements GetGameByName, Interactor {

        private final Executor executor;
        private final MainThread mainThread;
        private final GamesRepository gamesRepository;

        private String gameName;

        private GetGameByName.Callback callback;

        public GetGameByNameInteractor(Executor executor, MainThread mainThread, GamesRepository gamesRepository){
        this.executor = executor;
        this.mainThread = mainThread;
        this.gamesRepository = gamesRepository;
    }

        @Override
        public void execute(String gameName, Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException(
                    "Callback can't be null, the client of this interactor needs to get the response "
                            + "in the callback");
        }
        this.callback = callback;
        this.executor.run(this);
        this.gameName = gameName;
    }

        @Override
        public void run() {
        gamesRepository.getGame(gameName, new GamesRepository.GetGameCallback() {
            @Override
            public void onGameLoaded(Game game) {
                notifySuccess(game);
            }

            @Override
            public void onDataNotAvailable() {
                notifyError();
            }
        });
    }

    private void notifySuccess(final Game game) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onGameLoaded(game);
            }
        });
    }

    private void notifyError() {
        mainThread.post(new Runnable() {
            @Override public void run() {
                callback.onError();
            }
        });
    }
}

