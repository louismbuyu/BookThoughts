package com.example.louisnelsonlevoride.bookthoughts.Singletons;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.louisnelsonlevoride.bookthoughts.Onboarding.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class ConstantsHelperSingleton {
    private static ConstantsHelperSingleton INSTANCE = new ConstantsHelperSingleton();

    // other instance variables can be here
    public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
    public final static long MILLIS_PER_WEEK = 24 * 60 * 60 * 1000L * 7;

    private ConstantsHelperSingleton() {}

    public static ConstantsHelperSingleton getInstance() {
        return(INSTANCE);
    }

    public Date formatMongodbDateToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public String formatDateToRecentString(Date date, Boolean withTime){

        Date currentDate = Calendar.getInstance().getTime();
        String finalDateAsString = "";
        if (Math.abs(date.getTime() - currentDate.getTime()) < MILLIS_PER_DAY){
            //Less than a day
            DateFormat df = new SimpleDateFormat("HH:mm aa");
            finalDateAsString = df.format(date.getTime());
        }else if ((Math.abs(date.getTime() - currentDate.getTime()) >= MILLIS_PER_DAY) && (Math.abs(date.getTime() - currentDate.getTime()) < MILLIS_PER_WEEK)){
            //More than or equal to a day AND less than a week
            if (withTime){
                DateFormat df = new SimpleDateFormat("HH:mm aa EEE");
                finalDateAsString = df.format(date.getTime());
            }else{
                DateFormat df = new SimpleDateFormat("EEE");
                finalDateAsString = df.format(date.getTime());
            }
        }else{
            //More than a week
            if (withTime){
                DateFormat df = new SimpleDateFormat("HH:mm aa dd.MM.yyyy");
                finalDateAsString = df.format(date.getTime());
            }else{
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                finalDateAsString = df.format(date.getTime());
            }
        }

        return finalDateAsString;
    }
}
