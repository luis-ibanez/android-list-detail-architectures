/*
 * Copyright (C) 2016 Luis Ibanez Alonso.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.luisibanez.mvp.datasource.remote;

import android.support.annotation.NonNull;
import android.util.Log;

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

public class GamesRemoteDataSource implements GamesDataSource {

    private static final String TAG = GamesRemoteDataSource.class.getName();
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
            callback.onGamesLoaded(response);
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
            Log.e(TAG, "Error parsing http response:\n" +
                    "url: " + ENDPOINT + "\n");
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
    public void saveResponse(@NonNull ResponseGame responseGame) {
        // Not required because this option is not available in the API
    }

    @Override
    public void deleteAllGames() {
        // Not required because this option is not available in the API
    }

    @Override
    public boolean isValidData() {
        // Data in API is never expired
        return true;
    }
}

