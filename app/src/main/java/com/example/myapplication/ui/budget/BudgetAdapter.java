package com.example.myapplication.ui.budget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    ArrayList<BudgetViewModel> items;

    public BudgetAdapter(ArrayList<BudgetViewModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).name);
        holder.itemAmount.setText(Float.toString(items.get(position).amount));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemAmount;

        public BudgetViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemTitle);
            itemAmount = itemView.findViewById(R.id.itemAmount);
        }
    }
}
