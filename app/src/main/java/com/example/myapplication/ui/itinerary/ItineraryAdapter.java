package com.example.myapplication.ui.itinerary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;


// Copied from the checklist setup and adapted
/*ItineraryAdapter controls the display of the task list
Takes in an arraylist of ItineraryViewModel, and displays those items as a RecyclerView
*/
public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {

    //Custom ViewHolder to display items properly
    public class ItineraryViewHolder extends RecyclerView.ViewHolder {
        TextView task;
        TextView date;
        TextView notes;

        public ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.tvTitle);
            date = itemView.findViewById(R.id.dateId);
            notes = itemView.findViewById(R.id.notesId);
        }
    }

    ArrayList<ItineraryViewModel> tasks;

    public ItineraryAdapter(ArrayList<ItineraryViewModel> list) {
        this.tasks = list;
    }

    //Abstract functions required by to be implemented by RecyclerView.Adapter
    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        ItineraryViewModel t = tasks.get(position);

        holder.task.setText(t.getText());
        holder.notes.setText(t.getNotes());

        Calendar c = Calendar.getInstance();    // getting all the time/date from the calendar
        c.setTimeInMillis(t.getTime());
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String dayMonthYearTime = day + "/" + month + "/" + year + ", " + String.format("%02d:%02d", hour, minute);
        holder.date.setText(dayMonthYearTime);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.itinerary_item;
    }


}



