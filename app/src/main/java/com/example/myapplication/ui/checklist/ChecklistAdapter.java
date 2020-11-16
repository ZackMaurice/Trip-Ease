package com.example.myapplication.ui.checklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView text;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbDone);
            text = itemView.findViewById(R.id.tvTitle);
        }
    }

    ArrayList<ChecklistViewModelRecycle> checklist;

    public ChecklistAdapter(ArrayList<ChecklistViewModelRecycle> list){
        this.checklist = list;
    }
    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        holder.text.setText(checklist.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return checklist.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.checklist_item;
    }



}



