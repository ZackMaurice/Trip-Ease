package com.example.myapplication.ui.checklist;

import androidx.annotation.NonNull;


//Checklist object
//Text value to represent item name
//Boolean to represent checkbox status (not currently used, will be used in future)
public class ChecklistViewModel {
    private String text;
    private boolean isChecked;

    public ChecklistViewModel() {
        setText("");
        isChecked = false;
    }
    public ChecklistViewModel(@NonNull final String newText) {
        setText(newText);
        isChecked = false;
    }



    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull final String simpleText) {
        this.text = simpleText;
    }

    public boolean getChecked() { return isChecked; }

    public void check() {
        isChecked = !isChecked;
    }
}