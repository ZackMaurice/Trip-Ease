package com.example.myapplication.ui.itinerary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItineraryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ItineraryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is itinerary fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}