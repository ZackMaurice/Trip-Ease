package com.example.myapplication.ui.checklist;

import androidx.annotation.NonNull;

public class ChecklistViewModelRecycle {
    private String text;
    private boolean isChecked;

    public ChecklistViewModelRecycle() {
        setText("");
        isChecked = false;
    }
    public ChecklistViewModelRecycle(@NonNull final String newText) {
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

    public boolean getChecked(){ return isChecked; }

    public void check(){
        isChecked = !isChecked;
    }
}
