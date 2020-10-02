package com.example.myapplication.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class BudgetFragment extends Fragment {

	private BudgetViewModel budgetViewModel;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		budgetViewModel = ViewModelProviders.of(this).get(BudgetViewModel.class);
		View root = inflater.inflate(R.layout.fragment_budget, container, false);

		Spinner[] spinners = {
				root.findViewById(R.id.fromCurrency),
				root.findViewById(R.id.toCurrency)
		};

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getContext(),
				R.array.currencies,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinners
		for(Spinner spinner : spinners) spinner.setAdapter(adapter);

		return root;
	}
}