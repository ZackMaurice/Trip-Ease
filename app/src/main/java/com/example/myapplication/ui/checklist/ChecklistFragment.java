package com.example.myapplication.ui.checklist;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

import com.example.myapplication.R;

public class ChecklistFragment extends Fragment {

     private ChecklistViewModel checklistViewModel;
     EditText text;
     Button addButton;
     ArrayList<ChecklistViewModelRecycle> checklist;
     ChecklistAdapter adapter;

     public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         checklistViewModel = ViewModelProviders.of(this).get(ChecklistViewModel.class);
         View root = inflater.inflate(R.layout.fragment_checklist, container, false);
         checklist = new ArrayList<>();
         checklist.add(new ChecklistViewModelRecycle("hello"));
         adapter = new ChecklistAdapter(checklist);

         RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         addButton = root.findViewById(R.id.button_addItem);
         text = root.findViewById(R.id.text_addItem);
         initListeners();
         return root;
     }

     public void initListeners() {
         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String result = text.getText().toString();
                 if(result.length() != 0){
                     checklist.add(new ChecklistViewModelRecycle(result));
                     adapter.notifyItemInserted(checklist.size() - 1);
                     text.getText().clear();
                 }
             }
         });
     }
}
