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
package com.luisibanez.mvp.view.presenter;

import android.support.annotation.NonNull;

import com.luisibanez.mvp.datasource.GamesDataSource;
import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.interactor.GetGames;

import java.util.List;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

public class ListPresenter {

    private final GetGames getGamesInteractor;

    private final View listView;

    public ListPresenter(@NonNull GetGames getGamesInteractor, @NonNull View gamesView) {
        this.getGamesInteractor = checkNotNull(getGamesInteractor, "getGamesInteractor cannot be null");
        listView = checkNotNull(gamesView, "gamesView cannot be null!");

        listView.setPresenter(this);
    }

    public void initialize() {
        loadGames(false);
    }

    public void loadGames(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadGames(forceUpdate, true);
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link GamesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadGames(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            listView.setLoadingIndicator(true);
        }

        getGamesInteractor.execute(forceUpdate, new GetGames.Callback() {
            @Override
            public void onGamesLoaded(List<Game> games) {
                if (!listView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    listView.setLoadingIndicator(false);
                }
                processGames(games);
            }

            @Override
            public void onError() {
                if (!listView.isActive()) {
                    return;
                }
                listView.showLoadingGamesError();
            }
        });
    }

    private void processGames(List<Game> games) {
        if (games.isEmpty()) {
            // Show a message indicating there are no games
            processEmptyGames();
        } else {
            listView.showGames(games);
        }
    }


    private void processEmptyGames() {
        listView.showEmptyState();
    }

    public void openGameDetails(@NonNull Game requestedGame) {
        checkNotNull(requestedGame, "requestedGame cannot be null!");
        listView.showGameDetailsUi(requestedGame.getName());
    }

    public interface View {

        void setPresenter(ListPresenter presenter);

        void setLoadingIndicator(boolean active);

        void showGames(List<Game> games);

        void showGameDetailsUi(String gameName);

        void showLoadingGamesError();

        void showEmptyState();

        boolean isActive();

    }
}
