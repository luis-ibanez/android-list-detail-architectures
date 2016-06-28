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
package com.luisibanez.mvp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luisibanez.mvp.R;
import com.luisibanez.mvp.datasource.model.Game;

import java.util.List;

import static com.luisibanez.mvp.util.Preconditions.checkNotNull;

public class ListAdapter extends BaseAdapter {

    private List<Game> games;
    private GameItemListener itemListener;

    public ListAdapter(List<Game> games, GameItemListener itemListener) {
        setList(games);
        this.itemListener = itemListener;
    }

    public void replaceData(List<Game> games) {
        setList(games);
        notifyDataSetChanged();
    }

    private void setList(List<Game> games) {
        this.games = checkNotNull(games);
    }

    @Override
    public int getCount() {
        return this.games.size();
    }

    @Override
    public Game getItem(int i) {
        return this.games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.game_item_list, viewGroup, false);
        }

        final Game game = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.game_item_title);
        titleTV.setText(game.getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onGameClick(game);
            }
        });

        return rowView;
    }
}

