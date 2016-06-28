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
import android.support.annotation.Nullable;

import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.interactor.GetGameByName;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

public class DetailPresenter {

    private final GetGameByName getGameByNameInteractor;
    private final View gameDetailView;


    @Nullable
    private String gameName;

    public DetailPresenter(@Nullable String gameName,
                           @NonNull GetGameByName getGameByNameInteractor, 
                           @NonNull View gameDetailView) {
        
        this.gameName = gameName;
        this.getGameByNameInteractor = checkNotNull(getGameByNameInteractor, "getGameByNameInteractor cannot be null!");
        this.gameDetailView = checkNotNull(gameDetailView, "gamesView cannot be null!");

        this.gameDetailView.setPresenter(this);
    }

    public void initialize() {
        openGame();
    }

    private void openGame() {
        if (null == gameName || gameName.isEmpty()) {
            gameDetailView.showMissingGame();
            return;
        }

        gameDetailView.setLoadingIndicator(true);
        getGameByNameInteractor.execute(gameName, new GetGameByName.Callback() {
            @Override
            public void onGameLoaded(Game game) {
                // The view may not be able to handle UI updates anymore
                if (!gameDetailView.isActive()) {
                    return;
                }
                gameDetailView.setLoadingIndicator(false);
                if (null == game) {
                    gameDetailView.showMissingGame();
                } else {
                    showGame(game);
                }
            }

            @Override
            public void onError() {
                if (!gameDetailView.isActive()) {
                    return;
                }
                gameDetailView.showMissingGame();
            }
        });
    } 

    private void showGame(Game game) {
        String title = game.getName();
        String jackpot = game.getFormattedJackpot();
        String date = game.getFormattedDate();

        if (title != null && title.isEmpty()) {
            gameDetailView.hideTitle();
        } else {
            gameDetailView.showTitle(title);
        }

        if (jackpot != null && jackpot.isEmpty()) {
            gameDetailView.hideJackpot();
        } else {
            gameDetailView.showJackpot(jackpot);
        }

        if (date != null && date.isEmpty()) {
            gameDetailView.hideDate();
        } else {
            gameDetailView.showDate(date);
        }
    }

    public interface View {

        void setPresenter(DetailPresenter presenter);

        void setLoadingIndicator(boolean active);

        void showMissingGame();

        boolean isActive();

        void hideJackpot();

        void hideDate();

        void hideTitle();

        void hideEmptyCase();

        void showJackpot(String jackpot);

        void showDate(String date);

        void showTitle(String title);

        void showEmptyCase();
    }
}
