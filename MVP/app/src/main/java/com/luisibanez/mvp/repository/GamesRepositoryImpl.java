package com.luisibanez.mvp.repository;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.datasource.model.ResponseGame;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

/**
 * Created by libanez on 22/06/2016.
 */
public class GamesRepositoryImpl implements GamesRepository {

    private static GamesRepositoryImpl INSTANCE = null;

    private final GamesDataSource gamesRemoteDataSource;
    private final GamesDataSource gamesLocalDataSource;

    // Prevent direct instantiation.
    private GamesRepositoryImpl(@NonNull GamesDataSource gamesRemoteDataSource,
                                @NonNull GamesDataSource gamesLocalDataSource) {
        this.gamesRemoteDataSource = checkNotNull(gamesRemoteDataSource);
        this.gamesLocalDataSource = checkNotNull(gamesLocalDataSource);
    }

    public static GamesRepositoryImpl getInstance(GamesDataSource gamesRemoteDataSource,
                                                  GamesDataSource gamesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GamesRepositoryImpl(gamesRemoteDataSource, gamesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getGames(boolean refreshFromApi, @NonNull final LoadGamesCallback callback) {
        checkNotNull(callback);

        if (isLocalDirty() || refreshFromApi) {
            // If the local is dirty, or we are forced, we need to fetch new data from the network.
            getGamesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            gamesLocalDataSource.getGames(new GamesDataSource.LoadGamesCallback() {
                @Override
                public void onGamesLoaded(ResponseGame responseGame) {
                    callback.onGamesLoaded(responseGame.getGames());
                }

                @Override
                public void onDataNotAvailable() {
                    getGamesFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getGame(@NonNull final String gameName, @NonNull final GetGameCallback callback) {
        checkNotNull(gameName);
        checkNotNull(callback);

        // Is the game in the local data source? If not, query the network.
        gamesLocalDataSource.getGame(gameName, new GamesDataSource.GetGameCallback() {
            @Override
            public void onGameLoaded(Game game) {
                callback.onGameLoaded(game);
            }

            @Override
            public void onDataNotAvailable() {
                gamesRemoteDataSource.getGame(gameName, new GamesDataSource.GetGameCallback() {
                    @Override
                    public void onGameLoaded(Game game) {
                        callback.onGameLoaded(game);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void getGamesFromRemoteDataSource(@NonNull final LoadGamesCallback callback) {
        gamesRemoteDataSource.getGames(new GamesDataSource.LoadGamesCallback() {
            @Override
            public void onGamesLoaded(ResponseGame responseGame) {
                refreshLocalDataSource(responseGame);
                callback.onGamesLoaded(responseGame.getGames());
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(ResponseGame responseGame) {
        gamesLocalDataSource.deleteAllGames();
        gamesLocalDataSource.saveResponse(responseGame);
    }

    public boolean isLocalDirty() {
        return gamesLocalDataSource.isValidData();
    }
}

