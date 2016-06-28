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

import android.util.Log;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Game {

    private static final String TAG = Game.class.getName();
    private static final String CURRENCY_CODE_GBP = "GBP";

    private static String currencyCode;

    private String name;
    private long jackpot;
    private String date;

    public Game(String name, long jackpot, String date) {
        this.name = name;
        this.jackpot = jackpot;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJackpot(long jackpot) {
        this.jackpot = jackpot;
    }

    public long getJackpot() {
        return jackpot;
    }

    public String getFormattedJackpot(){
        if(currencyCode == null && currencyCode.isEmpty()){
            currencyCode = CURRENCY_CODE_GBP;
        }
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(currency);
        return format.format(jackpot);
    }

    public String getDate() {
        return date;
    }

    public String getFormattedDate(){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
        input.setTimeZone(TimeZone.getDefault());
        Date dateObj = null;
        try {
            dateObj = input.parse(date);
            int datestyle = DateFormat.MEDIUM;
            int timestyle = DateFormat.MEDIUM;
            DateFormat df = DateFormat.getDateTimeInstance(datestyle, timestyle, Locale.getDefault());
            return df.format(dateObj);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date:\n" +
                    "Input: " + date );
        }

        return "";
    }

    public static void setCurrencyCode(String currencyCode){
        Game.currencyCode = currencyCode;
    }
}
