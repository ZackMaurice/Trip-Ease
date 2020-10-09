package com.example.myapplication.ui.miscellaneous;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiscellaneousViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MiscellaneousViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is miscellaneous fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}