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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.checklist.ChecklistViewModel;
import com.example.myapplication.util.Currency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

	private TextWatcher fromAmountWatcher, toAmountWatcher;
	private boolean ignoreChanges = false; //prevent infinite loops caused by TextWatchers recursively triggering each other

	private ArrayList<BudgetViewModel> expenses;
	private BudgetAdapter adapter;

	Spinner fromCurrency, toCurrency;
	TextView fromAmount, toAmount;
	Button newExpense, addExpense;
	ViewGroup newExpensePrompt;
	RecyclerView recyclerView;

	private FirebaseAuth mAuth;
	private FirebaseUser user;
	private FirebaseDatabase database = FirebaseDatabase.getInstance();
	private DatabaseReference ref;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_budget, container, false);

		expenses = new ArrayList<>();

		//Firebase instantiations
		mAuth = FirebaseAuth.getInstance();
		user = mAuth.getCurrentUser();
		ref = database.getReference("budget/"+user.getUid());
		initDatabase();


		//RecyclerView instantiations
		adapter = new BudgetAdapter(expenses);
		recyclerView = root.findViewById(R.id.expenseList);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

		fromCurrency = root.findViewById(R.id.fromCurrency);
		fromAmount = root.findViewById(R.id.fromAmount);
		toCurrency = root.findViewById(R.id.toCurrency);
		toAmount = root.findViewById(R.id.toAmount);

		newExpense = root.findViewById(R.id.newExpense);
		addExpense = root.findViewById(R.id.addExpense);
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
			recyclerView.setVisibility(View.GONE);
			newExpensePrompt.setVisibility(View.VISIBLE);
		});

		addExpense.setOnClickListener(v->{
			newExpensePrompt.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);

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
			toAmount = Float.parseFloat(this.toAmount.getText().toString());
		} catch(NumberFormatException e) { return; }

		Currency toCurrency = Currency.valueOf(this.toCurrency.getSelectedItem().toString());
		Currency fromCurrency = Currency.valueOf(this.fromCurrency.getSelectedItem().toString());
		setCurrencyText(fromAmount, fromCurrency.format(toCurrency.convertTo(fromCurrency, toAmount), false));
	}

	private void convertFromAmount() {
		float fromAmount;
		try {
			fromAmount = Float.parseFloat(this.fromAmount.getText().toString());
		} catch(NumberFormatException e) { return;}

		Currency fromCurrency = Currency.valueOf(this.fromCurrency.getSelectedItem().toString());
		Currency toCurrency = Currency.valueOf(this.toCurrency.getSelectedItem().toString());
		setCurrencyText(toAmount, toCurrency.format(fromCurrency.convertTo(toCurrency, fromAmount), false));
	}

	/**
	 *
	 * @param name   The name of the expense
	 * @param amount The quantity of the expense
	 */
	private void addNewExpense(String name, float amount) {
		addNewExpense(new BudgetViewModel(name, amount));
	}

	/**
	 * @param expense the BudgetItem that represents this expense.
	 */
	private void addNewExpense(BudgetViewModel expense) {
		String name = expense.name;
		float amount = expense.amount;

		expenses.add(expense);

		ref.setValue(expenses);

		adapter.notifyDataSetChanged();
	}

	private void setCurrencyText(TextView textView, String amount) {
		ignoreChanges = true;
		textView.setText(amount);
		ignoreChanges = false;
	}

	//Setting up Firebase integration
	public void initDatabase () {
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				expenses.clear();
				for (DataSnapshot d : dataSnapshot.getChildren()) {
					expenses.add(d.getValue(BudgetViewModel.class));
				}
				adapter.notifyDataSetChanged();
			}
			public void onCancelled(@NonNull DatabaseError error) {
				System.out.println("\n\nLoad failed.\n");
			}

		});
	}
}