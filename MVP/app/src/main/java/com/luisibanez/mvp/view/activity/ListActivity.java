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
package com.luisibanez.mvp.view.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.luisibanez.mvp.R;
import com.luisibanez.mvp.datasource.local.GamesLocalDataSource;
import com.luisibanez.mvp.datasource.remote.GamesRemoteDataSource;
import com.luisibanez.mvp.executor.Executor;
import com.luisibanez.mvp.executor.MainThread;
import com.luisibanez.mvp.executor.MainThreadImpl;
import com.luisibanez.mvp.executor.ThreadExecutor;
import com.luisibanez.mvp.interactor.GetGames;
import com.luisibanez.mvp.interactor.GetGamesInteractor;
import com.luisibanez.mvp.repository.GamesRepository;
import com.luisibanez.mvp.repository.GamesRepositoryImpl;
import com.luisibanez.mvp.util.ActivityUtils;
import com.luisibanez.mvp.view.fragment.ListFragment;
import com.luisibanez.mvp.view.presenter.ListPresenter;

public class ListActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout drawerLayout;

    private ListPresenter listPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListFragment listFragment =
                (ListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (listFragment == null) {
            // Create the fragment
            listFragment = ListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), listFragment, R.id.contentFrame);
        }

        //create the repository
        GamesRepository gamesRepositoryImpl = GamesRepositoryImpl.getInstance(
                GamesRemoteDataSource.getInstance(),
                GamesLocalDataSource.getInstance(getApplicationContext()));

        //Create the interactor
        MainThread mainThread = new MainThreadImpl();
        Executor executor = ThreadExecutor.getInstance();
        GetGames getGamesInteractor = new GetGamesInteractor(executor, mainThread, gamesRepositoryImpl);

        // Create the presenter
        listPresenter = new ListPresenter(getGamesInteractor, listFragment);
    }
}