package com.luisibanez.mvp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

/**
 * Created by libanez on 22/06/2016.
 */
public class GamesRepositoryImpl implements GamesRepository {

    private static GamesRepositoryImpl INSTANCE = null;

    private final GamesDataSource gamesRemoteDataSource;
    private final GamesDataSource gamesLocalDataSource;

    Map<String, Game> cachedGames;

    boolean cacheIsDirty = false;

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

        if(refreshFromApi){
            cacheIsDirty = true;
        }

        // Respond immediately with cache if available and not dirty
        if (cachedGames != null && !cacheIsDirty) {
            callback.onGamesLoaded(new ArrayList<>(cachedGames.values()));
            return;
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getGamesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            gamesLocalDataSource.getGames(new GamesDataSource.LoadGamesCallback() {
                @Override
                public void onGamesLoaded(List<Game> games) {
                    refreshCache(games);
                    callback.onGamesLoaded(new ArrayList<>(cachedGames.values()));
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

        Game cachedGame = getGameWithName(gameName);

        // Respond immediately with cache if available
        if (cachedGame != null) {
            callback.onGameLoaded(cachedGame);
            return;
        }

        // Load from server/persisted if needed.

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

    private void deleteAllGames() {
        gamesRemoteDataSource.deleteAllGames();
        gamesLocalDataSource.deleteAllGames();

        if (cachedGames == null) {
            cachedGames = new LinkedHashMap<>();
        }
        cachedGames.clear();
    }

    private void deleteGame(@NonNull String gameName) {
        gamesRemoteDataSource.deleteGame(checkNotNull(gameName));
        gamesLocalDataSource.deleteGame(checkNotNull(gameName));

        cachedGames.remove(gameName);
    }

    private void refreshGames() {
        cacheIsDirty = true;
    }

    private void getGamesFromRemoteDataSource(@NonNull final LoadGamesCallback callback) {
        gamesRemoteDataSource.getGames(new GamesDataSource.LoadGamesCallback() {
            @Override
            public void onGamesLoaded(List<Game> games) {
                refreshCache(games);
                refreshLocalDataSource(games);
                callback.onGamesLoaded(new ArrayList<>(cachedGames.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Game> games) {
        if (cachedGames == null) {
            cachedGames = new LinkedHashMap<>();
        }
        cachedGames.clear();
        for (Game game : games) {
            cachedGames.put(game.getName(), game);
        }
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Game> games) {
        gamesLocalDataSource.deleteAllGames();
        for (Game game : games) {
            gamesLocalDataSource.saveGame(game);
        }
    }

    @Nullable
    private Game getGameWithName(@NonNull String name) {
        checkNotNull(name);
        if (cachedGames == null || cachedGames.isEmpty()) {
            return null;
        } else {
            return cachedGames.get(name);
        }
    }
}

