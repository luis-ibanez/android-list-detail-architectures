package com.luisibanez.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.luisibanez.mvp.R;
import com.luisibanez.mvp.datasource.model.Game;
import com.luisibanez.mvp.view.activity.DetailActivity;
import com.luisibanez.mvp.view.adapter.GameItemListener;
import com.luisibanez.mvp.view.adapter.ListAdapter;
import com.luisibanez.mvp.view.custom.ScrollChildSwipeRefreshLayout;
import com.luisibanez.mvp.view.presenter.ListPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

/**
 * Created by libanez on 22/06/2016.
 */
public class ListFragment extends Fragment implements ListPresenter.View {

    private ListPresenter listPresenter;

    private ListAdapter listAdapter;

    private View emptyCase;

    private View gamesView;

    public ListFragment() {
        // Requires empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ListAdapter(new ArrayList<Game>(0), itemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        listPresenter.initialize();
    }

    @Override
    public void setPresenter(@NonNull ListPresenter presenter) {
        listPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        // Set up games view
        ListView listView = (ListView) root.findViewById(R.id.games_list);
        listView.setAdapter(listAdapter);
        gamesView = root.findViewById(R.id.games_list);

        // Set up  no games view
        emptyCase = root.findViewById(R.id.noGames);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listPresenter.loadGames(true);
            }
        });

        return root;
    }
    
    /**
     * Listener for clicks on gmaes in the ListView.
     */
    GameItemListener itemListener = new GameItemListener() {
        @Override
        public void onGameClick(Game clickedGame) {
            listPresenter.openGameDetails(clickedGame);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showGames(List<Game> games) {
        listAdapter.replaceData(games);

        gamesView.setVisibility(View.VISIBLE);
        emptyCase.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyState() {
        gamesView.setVisibility(View.GONE);
        emptyCase.setVisibility(View.VISIBLE);
    }


    @Override
    public void showGameDetailsUi(String gameName) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_GAME_NAME, gameName);
        startActivity(intent);
    }

    @Override
    public void showLoadingGamesError() {
        showMessage(getString(R.string.loading_games_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
