package com.example.myapplication.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.util.Currency;

public class BudgetFragment extends Fragment {

	private BudgetViewModel budgetViewModel;
	Spinner fromCurrency, toCurrency;
	TextView fromAmount, toAmount;
	Button newExpense, addExpense;
	ViewGroup expenseList, newExpensePrompt;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		budgetViewModel = ViewModelProviders.of(this).get(BudgetViewModel.class);
		View root = inflater.inflate(R.layout.fragment_budget, container, false);

		fromCurrency = root.findViewById(R.id.fromCurrency);
		fromAmount = root.findViewById(R.id.fromAmount);
		toCurrency = root.findViewById(R.id.toCurrency);
		toAmount = root.findViewById(R.id.toAmount);

		newExpense = root.findViewById(R.id.newExpense);
		addExpense = root.findViewById(R.id.addExpense);
		expenseList = root.findViewById(R.id.expenseList);
		newExpensePrompt = root.findViewById(R.id.newExpensePrompt);


		//Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getContext(),
				R.array.currencies,
				android.R.layout.simple_spinner_item);

		//Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//Apply the adapter to the spinners
		fromCurrency.setAdapter(adapter);
		toCurrency.setAdapter(adapter);

		//TODO: Make currency converter interactively calculate converted amounts.
		fromAmount.setOnEditorActionListener((v, actionId, event)->{
			float fromAmount = Float.parseFloat(this.fromAmount.getText().toString());

			Currency fromCurrency = Currency.valueOf(this.fromCurrency.getSelectedItem().toString());
			Currency toCurrency = Currency.valueOf(this.toCurrency.getSelectedItem().toString());
			toAmount.setText(Float.toString(fromCurrency.convertTo(toCurrency, fromAmount)));
			return true;
		});

		toAmount.setOnEditorActionListener((v, actionId, event)->{
			float toAmount = Float.parseFloat(this.toAmount.getText().toString());

			Currency toCurrency = Currency.valueOf(this.toCurrency.getSelectedItem().toString());
			Currency fromCurrency = Currency.valueOf(this.fromCurrency.getSelectedItem().toString());
			fromAmount.setText(Float.toString(toCurrency.convertTo(fromCurrency, toAmount)));
			return true;
		});

		//show newExpensePrompt on click
		newExpense.setOnClickListener(v->{
			expenseList.setVisibility(View.GONE);
			newExpensePrompt.setVisibility(View.VISIBLE);
		});

		addExpense.setOnClickListener(v->{
			newExpensePrompt.setVisibility(View.GONE);
			expenseList.setVisibility(View.VISIBLE);

			//retrieve and reset user inputs
			EditText expenseNameItem = newExpensePrompt.findViewById(R.id.expenseName),
					expenseAmountItem = newExpensePrompt.findViewById(R.id.expenseAmount);

			String expenseName = expenseNameItem.getText().toString();
			float expenseAmount = Float.parseFloat(expenseAmountItem.getText().toString());

			expenseNameItem.setText("");
			expenseAmountItem.setText("");

			AddNewExpense(expenseName, expenseAmount);
		});
		return root;
	}

	/**
	 *
	 * @param name   The name of the expense
	 * @param amount The quantity of the expense
	 */
	private void AddNewExpense(String name, Float amount) {
		LinearLayout expenseItem = new LinearLayout(getContext());

		TextView nameView = new TextView(getContext()),
		amountView = new TextView(getContext());

		nameView.setText(name);
		nameView.setTextSize(24);

		amountView.setText(amount.toString());
		amountView.setTextSize(18);
		amountView.setPadding(32, 0, 0, 0);

		expenseItem.addView(nameView);
		expenseItem.addView(amountView);

		expenseList.addView(expenseItem, 0);
	}
}