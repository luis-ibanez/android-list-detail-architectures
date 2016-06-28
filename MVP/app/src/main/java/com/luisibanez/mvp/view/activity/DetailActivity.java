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

/**
 * Created by libanez on 22/06/2016.
 */
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
