package com.luisibanez.mvp.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luisibanez.mvp.R;
import com.luisibanez.mvp.view.presenter.DetailPresenter;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

/**
 * Created by libanez on 22/06/2016.
 */
public class DetailFragment extends Fragment implements DetailPresenter.View{
    public static final String ARGUMENT_GAME_NAME = "GAME_NAME";

    private DetailPresenter presenter;

    private TextView title;
    private TextView jackpot;
    private TextView date;

    private TextView emptyCase;

    private ProgressBar loadingBar;

    public static DetailFragment newInstance(String gameName) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_GAME_NAME, gameName);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.initialize();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        loadingBar = (ProgressBar) root.findViewById(R.id.detailProgressBar);
        title = (TextView) root.findViewById(R.id.detailName);
        jackpot = (TextView) root.findViewById(R.id.detailJackpot);
        date = (TextView) root.findViewById(R.id.detailDate);
        emptyCase = (TextView) root.findViewById(R.id.detailEmptyCase);


        return root;
    }

    @Override
    public void setPresenter(@NonNull DetailPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        title.setVisibility(active?View.GONE:View.VISIBLE);
        jackpot.setVisibility(active?View.GONE:View.VISIBLE);
        date.setVisibility(active?View.GONE:View.VISIBLE);
        loadingBar.setVisibility(active?View.VISIBLE:View.GONE);
    }

    @Override
    public void hideJackpot() {
        jackpot.setVisibility(View.GONE);
    }

    @Override
    public void hideDate() {
        date.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        title.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyCase() {
        emptyCase.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyCase() {
        this.emptyCase.setVisibility(View.VISIBLE);
    }

    @Override
    public void showJackpot(String jackpot) {
        this.jackpot.setVisibility(View.VISIBLE);
        this.jackpot.setText(jackpot);
    }

    @Override
    public void showDate(String date) {
        this.date.setVisibility(View.VISIBLE);
        this.date.setText(date);
    }

    @Override
    public void showTitle(String title) {
        this.title.setVisibility(View.VISIBLE);
        this.title.setText(title);
    }

    @Override
    public void showMissingGame() {
        hideDate();
        hideJackpot();
        hideTitle();

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
