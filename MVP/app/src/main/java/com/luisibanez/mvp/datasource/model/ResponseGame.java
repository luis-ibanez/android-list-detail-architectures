package com.luisibanez.mvp.datasource.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by libanez on 27/06/2016.
 */
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
