package com.luisibanez.mvp.interactor;

import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.executor.Executor;
import com.luisibanez.mvp.executor.Interactor;
import com.luisibanez.mvp.executor.MainThread;
import com.luisibanez.mvp.repository.GamesRepository;

import java.util.List;

/**
 * Created by libanez on 27/06/2016.
 */
public class GetGamesInteractor implements GetGames, Interactor {

    private final Executor executor;
    private final MainThread mainThread;
    private final GamesRepository gamesRepository;

    private boolean refreshFromApi = false;

    private GetGames.Callback callback;

    public GetGamesInteractor(Executor executor, MainThread mainThread, GamesRepository gamesRepository){
        this.executor = executor;
        this.mainThread = mainThread;
        this.gamesRepository = gamesRepository;
    }

    @Override
    public void execute(boolean refreshFromApi, Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException(
                    "Callback can't be null, the client of this interactor needs to get the response "
                            + "in the callback");
        }
        this.callback = callback;
        this.executor.run(this);
        this.refreshFromApi = refreshFromApi;
    }

    @Override
    public void run() {
        gamesRepository.getGames(refreshFromApi, new GamesRepository.LoadGamesCallback() {
            @Override
            public void onGamesLoaded(List<Game> games) {
                notifySuccess(games);
            }

            @Override
            public void onDataNotAvailable() {
                notifyError();
            }
        });
    }

    private void notifySuccess(final List<Game> games) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onGamesLoaded(games);
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
