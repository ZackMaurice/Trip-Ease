package com.example.myapplication.ui.budget;

import androidx.lifecycle.ViewModel;

public class BudgetViewModel extends ViewModel {
	String name;
	float amount;

	public BudgetViewModel(final String newText, final float amount) {
		name = newText;
		this.amount = amount;
	}

	public BudgetViewModel() {
		this("", 0);
	}
}
