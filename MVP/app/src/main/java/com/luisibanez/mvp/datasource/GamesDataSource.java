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
package com.luisibanez.mvp.datasource;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.datasource.model.ResponseGame;

import java.util.List;

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
