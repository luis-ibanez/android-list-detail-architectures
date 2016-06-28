package com.luisibanez.mvp.datasource.model;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by libanez on 22/06/2016.
 */
public class Game {

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
        //TODO get instance from file
        Currency gbp = Currency.getInstance("GBP");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(gbp);
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
            //TODO: send error bad parse string
        }

        return "";
    }
}
