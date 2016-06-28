package com.luisibanez.mvp.datasource.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by libanez on 27/06/2016.
 */
public class ResponseGame {
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

    public static ResponseGame fromJson(String s) {
        return new Gson().fromJson(s, ResponseGame.class);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
