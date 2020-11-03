package com.example.myapplication.ui.itinerary;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Calendar;

public class Task {

    private String thing;
    private Calendar date;


    public Task(String s){
        thing = s.split(":::")[0];
        Timestamp temp = new Timestamp(Long.parseLong(s.split(":::")[1]));
        date = Calendar.getInstance();
        date.setTime(temp);

    }

    public Task(String t, Calendar d){
        thing = t;
        date = d;

    }

    public Calendar getCalendar(){
        return date;
    }

    public String getThing() {
        return thing;
    }

    @NonNull
    @Override
    public String toString(){

        return thing+":::"+date.getTimeInMillis();
    }
}
