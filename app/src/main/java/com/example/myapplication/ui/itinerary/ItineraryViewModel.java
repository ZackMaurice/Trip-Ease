package com.example.myapplication.ui.itinerary;

import androidx.annotation.NonNull;

//Checklist object
//Text value to represent item name
//Boolean to represent checkbox status (not currently used, will be used in future)
public class ItineraryViewModel {
    private String text;
    private String notes;
    private long date;

    public ItineraryViewModel() {   // redundant empty constructor

    }

    // constructor to set values upon birth
    public ItineraryViewModel(@NonNull final String newText, final String newNote, final long time) {
        this.text = newText;
        this.notes = newNote;
        this.date = time;
    }


    @NonNull
    public String getText() {
        return this.text;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setTime(long time) {
        this.date = time;
    } // for some reason this needs to exist even though its never called, otherwise importing data from firebase doesn't work

    public long getTime() {
        return this.date;
    }

}