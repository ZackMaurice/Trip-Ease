package com.example.myapplication.ui.budget;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class BudgetViewModel extends ViewModel implements Serializable {
	public String name;
	public float amount;

	public BudgetViewModel(final String newText, final float amount) {
		name = newText;
		this.amount = amount;
	}

	public BudgetViewModel() {
		this("", 0);
	}
}
