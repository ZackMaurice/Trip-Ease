package com.example.myapplication.ui.checklist;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.budget.BudgetViewModel;

public class ChecklistFragment extends Fragment {

    private ChecklistViewModel checklistViewModel;
    EditText text;
    Button addButton;
    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checklistViewModel = ViewModelProviders.of(this).get(ChecklistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_checklist, container, false);

        text = root.findViewById(R.id.text_addItem);
        addButton = root.findViewById(R.id.button_addItem);
        list = root.findViewById(R.id.list_items);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);
        onBtnClick();

        return root;
    }

    public void onBtnClick(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = text.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
