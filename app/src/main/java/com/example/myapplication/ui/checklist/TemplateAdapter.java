package com.example.myapplication.ui.checklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

/*ChecklistAdapter controls the display of the checklist
Takes in an arraylist of ChecklistViewModel objects, and displays those items as a RecyclerView*/
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder> {
    private OnItemClickListener mListener;
    private ArrayList<TemplateViewModel> templates;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    //Custom ViewHolder to display items properly
    public class TemplateViewHolder extends RecyclerView.ViewHolder{
        private TextView text;

        public TemplateViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.templateTitle);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }



    public TemplateAdapter(ArrayList<TemplateViewModel> list){
        this.templates = list;
    }

    //Abstract functions required by to be implemented by RecyclerView.Adapter
    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_item, parent, false);
        return new TemplateViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder holder, int position) {
        holder.text.setText(templates.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.template_item;
    }
}



