package com.example.myapplication.ui.checklist;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.util.SwipeToDeleteCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChecklistFragment extends Fragment {
     private EditText text, templateText;
     private Button addButton, loadButton, saveViewButton, cancelButton, saveButton, cancelButton2;
     private ArrayList<ChecklistViewModel> checklist;
     private ArrayList<TemplateViewModel> templates;
     private ChecklistAdapter adapter;
     private TemplateAdapter templateAdapter;
     private RecyclerView recyclerView, templateView;
     private ConstraintLayout mainView, saveView, loadView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    @RequiresApi(api = Build.VERSION_CODES.M)
     public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_checklist, container, false);
         checklist = new ArrayList<>();
         templates = new ArrayList<>();

         //Firebase instantiations
         mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
         ref = database.getReference("checklist/"+user.getUid());

         //RecyclerView instantiations
         adapter = new ChecklistAdapter(checklist);
         recyclerView = root.findViewById(R.id.recyclerview);
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

         templateAdapter = new TemplateAdapter(templates);
         templateView = root.findViewById(R.id.templateRecyclerView);
         templateView.setHasFixedSize(true);
         templateView.setAdapter(templateAdapter);
         templateView.setLayoutManager(new LinearLayoutManager(getContext()));
         templateView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
         templateAdapter.setOnItemClickListener(new TemplateAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(int position) {
                 checklist.clear();
                 adapter.notifyDataSetChanged();
                 for (int i=0; i < templates.get(position).getTemplates().size(); i++) {
                    checklist.add(new ChecklistViewModel(templates.get(position).getTemplates().get(i)));
                    adapter.notifyItemInserted(i);
                 }
                 ref.setValue(checklist);
                 adapter.notifyDataSetChanged();
                 mainView.setVisibility(View.VISIBLE);
                 loadView.setVisibility(View.GONE);
             }
         });

         //XML item instantiations
         addButton = root.findViewById(R.id.button_addItem);
         loadButton = root.findViewById(R.id.loadTemplateButton);
         saveViewButton = root.findViewById(R.id.saveTemplateButton);
         saveButton = root.findViewById(R.id.saveTemplate2);
         text = root.findViewById(R.id.text_addItem);
         loadButton = root.findViewById(R.id.loadTemplateButton);
         cancelButton = root.findViewById(R.id.cancelTemplate);
         cancelButton2 = root.findViewById(R.id.cancelLoad);
         mainView = root.findViewById(R.id.checklist_main);
         saveView = root.findViewById(R.id.saveTemplateView);
         loadView = root.findViewById(R.id.loadTemplateView);
         templateText = root.findViewById(R.id.text_saveTemplate);

         //Initialization helpers
         initListeners();
         initTemplates();
         enableSwipeDelete();
         initDatabase();
         return root;
     }

    //Setting up addItem button listener
     public void initListeners () {
         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String result = text.getText().toString();
                 if (result.length() != 0) {
                     checklist.add(new ChecklistViewModel(result));
                     text.getText().clear();
                     ref.setValue(checklist);
                     adapter.notifyDataSetChanged();
                 }
             }
         });

         loadButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mainView.setVisibility(View.GONE);
                 loadView.setVisibility(View.VISIBLE);
                 }
         });

         saveViewButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mainView.setVisibility(View.GONE);
                 saveView.setVisibility(View.VISIBLE);
             }
         });

         cancelButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mainView.setVisibility(View.VISIBLE);
                 saveView.setVisibility(View.GONE);
             }
         });

         cancelButton2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mainView.setVisibility(View.VISIBLE);
                 loadView.setVisibility(View.GONE);
             }
         });

         saveButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 saveTemplate();
             }
         });
     }

     public void saveTemplate(){
        boolean nameEQ = false;
         if(templateText.getText().toString().length() == 0) {
             Toast toast = Toast.makeText(getContext(), "Please enter a name.", Toast.LENGTH_SHORT);
             toast.show();
             return;
         }
        for(int i=0; i < templates.size(); i++) {
            if (templateText.getText().toString().equals(templates.get(i).getText())) {
                Toast toast = Toast.makeText(getContext(), "Please enter a unique name.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i < checklist.size(); i++) {
            list.add(checklist.get(i).getText());
        }
        templates.add(new TemplateViewModel(templateText.getText().toString(), list));
        templateAdapter.notifyItemInserted(templates.size()-1);
        mainView.setVisibility(View.VISIBLE);
        saveView.setVisibility(View.GONE);
     }

     public void initTemplates(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add("Towel");
        temp.add("Sunscreen");
        temp.add("Volleyball");
        temp.add("Frisbee");
        temp.add("Swimsuit");
        temp.add("Sandals");
        temp.add("Cooler");
        templates.add(new TemplateViewModel("Beach", temp));
        templateAdapter.notifyItemInserted(templates.size()-1);
         ArrayList<String> temp2 = new ArrayList<>();
         temp2.add("Backpack");
         temp2.add("Water Bottle");
         temp2.add("Tent");
         temp2.add("Sleeping Bag");
         temp2.add("Firewood");
         temp2.add("Sweater");
         temp2.add("Towel");
         temp2.add("Camp chair");
         temp2.add("Air mattress");
         temp2.add("Cooler");
         templates.add(new TemplateViewModel("Camping", temp2));
         templateAdapter.notifyItemInserted(templates.size()-1);
     }

     //Setting up swipe to delete
     public void enableSwipeDelete () {
         SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 final int position = viewHolder.getAdapterPosition();
                 checklist.remove(position);
                 ref.setValue(checklist);
                 adapter.notifyItemRemoved(position);
             }
         };
         SwipeToDeleteCallback swipeToDeleteCallback2 = new SwipeToDeleteCallback(getContext()) {
             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 final int position = viewHolder.getAdapterPosition();
                 templates.remove(position);
                 templateAdapter.notifyItemRemoved(position);
             }
         };
         ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
         itemTouchhelper.attachToRecyclerView(recyclerView);
         ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(swipeToDeleteCallback2);
         itemTouchHelper2.attachToRecyclerView(templateView);
     }

    //Setting up Firebase integration
     public void initDatabase () {
         ref.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 checklist.clear();
                 for (DataSnapshot d : dataSnapshot.getChildren()) {
                     checklist.add(d.getValue(ChecklistViewModel.class));
                 }
                 adapter.notifyDataSetChanged();
             }
             public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("\n\nLoad failed.\n");
             }
         });
     }
}
