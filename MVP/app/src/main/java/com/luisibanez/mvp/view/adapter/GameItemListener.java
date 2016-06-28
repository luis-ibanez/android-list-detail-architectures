package com.luisibanez.mvp.view.adapter;

import com.luisibanez.mvp.datasource.model.Game;

/**
 * Created by libanez on 23/06/2016.
 */
public interface GameItemListener {

    void onGameClick(Game clickedGame);
}