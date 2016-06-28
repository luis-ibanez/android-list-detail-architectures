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
package com.luisibanez.mvp.datasource.model;

import com.google.gson.Gson;

import java.util.List;

public class ResponseGame {

    private long ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;

    public long cacheTime = 0;
    public String response;
    public String currency;
    public List<Game> data;

    public String getResponse() {
        return response;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Game> getGames() {
        return data;
    }

    public ResponseGame(String response, String currency, List<Game> data) {
        this.response = response;
        this.currency = currency;
        this.data = data;
    }

    public void refreshCacheTime(){
        this.cacheTime = System.currentTimeMillis();
    }

    public static ResponseGame fromJson(String s) {
        ResponseGame responseGame = new Gson().fromJson(s, ResponseGame.class);
        Game.setCurrencyCode(responseGame.getCurrency());
        return responseGame;
    }

    public static String toJson(ResponseGame responseGame) {
        return new Gson().toJson(responseGame);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean isExpired(){
        return System.currentTimeMillis() > cacheTime + ONE_HOUR_IN_MILLIS;
    }
}
