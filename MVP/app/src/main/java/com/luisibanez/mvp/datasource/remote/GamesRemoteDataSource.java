package com.luisibanez.mvp.datasource.remote;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.datasource.model.ResponseGame;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by libanez on 22/06/2016.
 */
public class GamesRemoteDataSource implements GamesDataSource {

    private static final String ENDPOINT = "https://dl.dropboxusercontent.com/u/49130683/nativeapp-test.json";
    private static GamesRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    public static GamesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GamesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private GamesRemoteDataSource() {}

    @Override
    public void getGames(final @NonNull LoadGamesCallback callback) {
        ResponseGame response = requestGamesFile();
        if (response != null){
            callback.onGamesLoaded(response.getGames());
        }else{
            callback.onDataNotAvailable();
        }
    }

    private ResponseGame requestGamesFile(){
        ResponseGame response = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(ENDPOINT);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            response = ResponseGame.fromJson(result.toString());

        } catch (IOException e) {
            //TODO: add more error responses
        }

        return response;
    }

    @Override
    public void getGame(@NonNull String gameName, final @NonNull GetGameCallback callback) {
        for(Game game : requestGamesFile().getGames()){
            if(game.getName().equals(gameName)){
                callback.onGameLoaded(game);
                return;
            }
        }
    }

    @Override
    public void saveGame(@NonNull Game game) {
        // Not required because this option is not available in the API
    }

    @Override
    public void deleteGame(@NonNull String gameName) {
        // Not required because this option is not available in the API

    }

    @Override
    public void deleteAllGames() {
        // Not required because this option is not available in the API
    }

    @Override
    public void refreshGames() {
        // Not required because the {@link GamesRepositoryImpl} handles the logic of refreshing the
        // games from all the available data sources.
    }
}

