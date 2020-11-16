package com.example.myapplication.ui.checklist;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
     EditText text;
     Button addButton;
     ArrayList<ChecklistViewModelRecycle> checklist;
     ChecklistAdapter adapter;
     RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    @RequiresApi(api = Build.VERSION_CODES.M)
     public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_checklist, container, false);
         checklist = new ArrayList<>();

         mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
         ref = database.getReference("checklist/"+user.getUid());

         adapter = new ChecklistAdapter(checklist);
         recyclerView = root.findViewById(R.id.recyclerview);
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));

         addButton = root.findViewById(R.id.button_addItem);
         text = root.findViewById(R.id.text_addItem);
         initDatabase();
         adapter.notifyDataSetChanged();
         initListeners();
         enableSwipeDelete();
         return root;
     }

     public void initListeners() {
         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String result = text.getText().toString();
                 if(result.length() != 0){
                     checklist.add(new ChecklistViewModelRecycle(result));
                     text.getText().clear();
                     ref.setValue(checklist);
                     adapter.notifyDataSetChanged();
                     for (int i=0; i<checklist.size(); i++)
                         System.out.println(checklist.get(i).getText());
                 }
             }
         });
     }

     public void enableSwipeDelete(){
         SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                 final int position = viewHolder.getAdapterPosition();
                 final ChecklistViewModelRecycle item = checklist.get(position);
                 checklist.remove(position);
                 ref.setValue(checklist);
                 adapter.notifyItemRemoved(position);
             }
         };

         ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
         itemTouchhelper.attachToRecyclerView(recyclerView);
     }



    public void initDatabase() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checklist.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    checklist.add(d.getValue(ChecklistViewModelRecycle.class));
                }
                adapter.notifyDataSetChanged();

            }

            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("\n\nLoad failed.\n");
            }

        });
    }
}
