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
package com.luisibanez.mvp.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.datasource.model.ResponseGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

public class GamesLocalDataSource implements GamesDataSource {

    private static final String TAG = GamesLocalDataSource.class.getName();
    public static final String GAMES_FILE = "games.txt";

    private static GamesLocalDataSource INSTANCE;

    private final Context context;

    // Prevent direct instantiation.
    private GamesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        this.context = context;
    }

    public static GamesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GamesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getGames(@NonNull LoadGamesCallback callback) {
        ResponseGame responseGame = null;

        String fileContent = loadFromFile();

        if(fileContent != null && !fileContent.isEmpty()){
            responseGame = ResponseGame.fromJson(fileContent);
            if (!responseGame.getGames().isEmpty()) {
                callback.onGamesLoaded(responseGame);
                return;
            }
        }

        // This will be called if the table is new or just empty.
        callback.onDataNotAvailable();
    }

    @Override
    public void getGame(@NonNull String gameName, @NonNull GetGameCallback callback) {
        ResponseGame responseGame = null;

        String fileContent = loadFromFile();

        if(fileContent != null && !fileContent.isEmpty()){
            responseGame = ResponseGame.fromJson(fileContent);
            if (!responseGame.getGames().isEmpty()) {
                for(Game game : responseGame.getGames()){
                    if(game.getName().equals(gameName)){
                        callback.onGameLoaded(game);
                        return;
                    }
                }
            }
        }

        // This will be called if the table is new or just empty.
        callback.onDataNotAvailable();
    }

    @Override
    public void saveResponse(@NonNull ResponseGame responseGame) {
        responseGame.refreshCacheTime();
        saveToFile(ResponseGame.toJson(responseGame));
    }



    @Override
    public void deleteAllGames() {
        context.deleteFile(GAMES_FILE);
    }

    @Override
    public boolean isValidData() {
        ResponseGame responseGame = null;

        String fileContent = loadFromFile();

        if(fileContent != null && !fileContent.isEmpty()){
            responseGame = ResponseGame.fromJson(fileContent);
            return !responseGame.isExpired();
        }
        return false;
    }

    private void saveToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(GAMES_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Error saving file: \n" +
                    "file: " + GAMES_FILE + "\n" +
                    "data: " + data);
        }
    }

    private String loadFromFile() {

        String fileContent = "";

        try {
            InputStream inputStream = context.openFileInput(GAMES_FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                fileContent = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "Error loading file, the file doesn't exist: \n" +
                    "file: " + GAMES_FILE);
        } catch (IOException e) {
            Log.e(TAG, "Error loading file: \n" +
                    "file: " + GAMES_FILE);
        }

        return fileContent;
    }
}
