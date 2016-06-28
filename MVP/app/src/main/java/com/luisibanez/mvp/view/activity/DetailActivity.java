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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.luisibanez.mvp.R;
import com.luisibanez.mvp.datasource.local.GamesLocalDataSource;
import com.luisibanez.mvp.datasource.remote.GamesRemoteDataSource;
import com.luisibanez.mvp.executor.Executor;
import com.luisibanez.mvp.executor.MainThread;
import com.luisibanez.mvp.executor.MainThreadImpl;
import com.luisibanez.mvp.executor.ThreadExecutor;
import com.luisibanez.mvp.interactor.GetGameByName;
import com.luisibanez.mvp.interactor.GetGameByNameInteractor;
import com.luisibanez.mvp.repository.GamesRepositoryImpl;
import com.luisibanez.mvp.util.ActivityUtils;
import com.luisibanez.mvp.view.fragment.DetailFragment;
import com.luisibanez.mvp.view.presenter.DetailPresenter;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_NAME = "GAME_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested task id
        String gameName = getIntent().getStringExtra(EXTRA_GAME_NAME);

        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (detailFragment == null) {
            detailFragment = DetailFragment.newInstance(gameName);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    detailFragment, R.id.contentFrame);
        }

        //create the repository
        GamesRepositoryImpl gamesRepositoryImpl = GamesRepositoryImpl.getInstance(
                GamesRemoteDataSource.getInstance(),
                GamesLocalDataSource.getInstance(getApplicationContext()));

        //Create the interactor
        MainThread mainThread = new MainThreadImpl();
        Executor executor = ThreadExecutor.getInstance();
        GetGameByName getGameByNameInteractor = new GetGameByNameInteractor(executor, mainThread, gamesRepositoryImpl);

        // Create the presenter
        new DetailPresenter(
                gameName,
                getGameByNameInteractor,
                detailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
