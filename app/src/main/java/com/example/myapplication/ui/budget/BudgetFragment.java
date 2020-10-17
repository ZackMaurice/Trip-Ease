package com.example.myapplication.ui.budget;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.util.Currency;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

	private BudgetViewModel budgetViewModel;
	private TextWatcher fromAmountWatcher, toAmountWatcher;
	private boolean ignoreChanges = false; //prevent infinite loops caused by TextWatchers recursively triggering each other

	private ArrayList<BudgetItem> expenses;

	Spinner fromCurrency, toCurrency;
	TextView fromAmount, toAmount;
	Button newExpense, addExpense;
	ViewGroup expenseList, newExpensePrompt;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		budgetViewModel = ViewModelProviders.of(this).get(BudgetViewModel.class);
		View root = inflater.inflate(R.layout.fragment_budget, container, false);

		expenses = new ArrayList<>();

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

		//Initialize TextWatchers
		fromAmountWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(!ignoreChanges) {
					try {
						ignoreChanges = true;
						convertFromAmount();
					} finally {
						ignoreChanges = false;
					}
				}
			}
		};
		toAmountWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(!ignoreChanges) {
					try {
						ignoreChanges = true;
						convertToAmount();
					} finally {
						ignoreChanges = false;
					}
				}
			}
		};

		//Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//Apply the adapter to the spinners
		fromCurrency.setAdapter(adapter);
		toCurrency.setAdapter(adapter);

		//interactively calculate converted amounts.
		fromAmount.addTextChangedListener(fromAmountWatcher);

		toCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					convertFromAmount();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		toAmount.addTextChangedListener(toAmountWatcher);

		fromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				convertToAmount();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
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

			addNewExpense(expenseName, expenseAmount);
		});
		return root;
	}

	private void convertToAmount() {
		float toAmount;
		try {
			toAmount = Float.parseFloat(BudgetFragment.this.toAmount.getText().toString());
		} catch(NumberFormatException e) { return; }

		Currency toCurrency = Currency.valueOf(BudgetFragment.this.toCurrency.getSelectedItem().toString());
		Currency fromCurrency = Currency.valueOf(BudgetFragment.this.fromCurrency.getSelectedItem().toString());
		fromAmount.setText(Float.toString(toCurrency.convertTo(fromCurrency, toAmount)));
	}

	private void convertFromAmount() {
		float fromAmount;
		try {
			fromAmount = Float.parseFloat(BudgetFragment.this.fromAmount.getText().toString());
		} catch(NumberFormatException e) { return;}

		Currency fromCurrency = Currency.valueOf(BudgetFragment.this.fromCurrency.getSelectedItem().toString());
		Currency toCurrency = Currency.valueOf(BudgetFragment.this.toCurrency.getSelectedItem().toString());
		toAmount.setText(Float.toString(fromCurrency.convertTo(toCurrency, fromAmount)));
	}

	/**
	 *
	 * @param name   The name of the expense
	 * @param amount The quantity of the expense
	 */
	private void addNewExpense(String name, float amount) {
		addNewExpense(new BudgetItem(name, amount));
	}

	/**
	 * @param expense the BudgetItem that represents this expense.
	 */
	private void addNewExpense(BudgetItem expense) {
		String name = expense.name;
		float amount = expense.amount;

		expenses.add(expense);

		LinearLayout expenseItem = new LinearLayout(getContext());

		TextView nameView = new TextView(getContext()),
				amountView = new TextView(getContext());

		nameView.setText(name);
		nameView.setTextSize(24);

		amountView.setText(Float.toString(amount));
		amountView.setTextSize(18);
		amountView.setPadding(32, 0, 0, 0);

		expenseItem.addView(nameView);
		expenseItem.addView(amountView);

		expenseList.addView(expenseItem, 0);
	}

	private static class BudgetItem {
		public final String name;
		public final float amount;

		public BudgetItem(String name, Float amount) {
			this.name = name;
			this.amount = amount;
		}
	}
}