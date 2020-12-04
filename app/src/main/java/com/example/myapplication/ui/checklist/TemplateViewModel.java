package com.example.myapplication.ui.checklist;

import androidx.annotation.NonNull;

import java.util.ArrayList;


//Template object
//Text value to represent item name
public class TemplateViewModel {
    private String text;
    private ArrayList<String> templates;

    public TemplateViewModel() {
        setText("");
        templates = new ArrayList<>();
    }

    public TemplateViewModel(@NonNull final String newText, ArrayList<String> newTemplates) {
        this.templates = newTemplates;
        this.text = newText;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public ArrayList<String> getTemplates(){
        return templates;
    }

    public void setText(@NonNull final String simpleText) {
        this.text = simpleText;
    }
}