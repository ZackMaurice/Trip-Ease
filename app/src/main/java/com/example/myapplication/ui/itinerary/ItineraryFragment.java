package com.example.myapplication.ui.itinerary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ItineraryFragment extends Fragment {

    private ItineraryViewModel itineraryViewModel;
    private Context context;
    private LinearLayout scroll; // Linear Layout used in the ScrollView


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Calendar> dates = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itineraryViewModel =
                ViewModelProviders.of(this).get(ItineraryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_itinerary, container, false);

        context = container.getContext();   // context to use when making new xml elements

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();





        scroll = root.findViewById(R.id.itinerary_scroll);  // Assigning the scroll linear layout

        ref = database.getReference("itinerary/"+user.getUid());

        init(root);

        return root;
    }

    /*  init(View root)
    * Interacts with all elements on the itinerary fragment,
    * Pulls time, date & text input
    * Builds 2 strings with time, date & text
    * Adds the string to the scrollview so that there can be many tasks/ things to do on the itinerary, scrollable
    * Button goToAddView makes the itinerary view invisible, brings up the DatePicker and TimePicker + text input box
    * Button addToItinerary adds the strings to the scroll view, then makes the adding layout invisible and brings back the itinerary
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(View root){

        final ConstraintLayout mainView = root.findViewById(R.id.itinerary_main);   // main view
        final ConstraintLayout addView = root.findViewById(R.id.itinerary_adding);    // adding view

        final DatePicker datePicker = root.findViewById(R.id.itinerary_date);   // date input
        final TimePicker timePick = root.findViewById(R.id.itinerary_time); // time input
        final TextView input = root.findViewById(R.id.itinerary_input); // task input

        final Button goToAddView = root.findViewById(R.id.itinerary_goAddView);   // Button that reveals the view to add things and hides the itinerary
        final Button addToItinerary = root.findViewById(R.id.itinerary_add);  // Button that reveals the itinerary view and hides the view that adds things



        // Import data from firebase
        // When there is a change to the data in this directory itinerary/<emailid>, update the data
        ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                GenericTypeIndicator<ArrayList<String>> generic = new GenericTypeIndicator<ArrayList<String>>(){};
                list = dataSnapshot.getValue(generic);

                if(list!=null) {
                    for (int i = 0; i < list.size(); i++) {
                        Task t = new Task(list.get(i));
                        if(!isDuplicate(t)){
                            dates.add(t.getCalendar());
                            createTask(t);
                        }


                    }
                }else {
                    list = new ArrayList<>(); // replace the list in the case it pulls a null

                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("\n\nLoad failed.\n");
            }

        });







        goToAddView.setOnClickListener(v -> {
            addView.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);

        });

        addToItinerary.setOnClickListener(v -> {

            String newTask = input.getText().toString();    // getText returns a CharSequence for some reason, needed a toString
            Calendar calendar = Calendar.getInstance();// to be stored in Tasks in the ArrayList

            // getting time & day information from datePicker and timePicker
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();    // month goes 0-11 with DatePicker
            int year = datePicker.getYear();
            int minute = timePick.getMinute();  // requires api 23
            int hour = timePick.getHour();      // requires api 23


            // Setting up a calendar since most of Date is deprecated
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);


            Task t = new Task(newTask,calendar);   // new Task to be stored in the ArrayList and the servers
            if(!isDuplicate(t)){
                dates.add(t.getCalendar());
                createTask(t);
                list.add(t.toString()); // save the data as an ArrayList of strings to be used on the database
            }


            addView.setVisibility(View.GONE);   // make the addView invisible and bring back the itinerary
            mainView.setVisibility(View.VISIBLE);


            ref.setValue(list); // send the string
        });

    }


    // Method that creates the TextViews on the linearLayout scroll, takes in a task
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void createTask(Task t){
        String thing = t.getThing();
        Calendar date = t.getCalendar();
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH)+1;
        int year = date.get(Calendar.YEAR);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);


        TextView taskText = new TextView(context);  // making a TextView for the thing to be done
        TextView dateText = new TextView(context);  // making a TextView for the date & time of said thing
        taskText.setText(thing);   // "Task"
        taskText.setTextSize(24);   // Set font
        dateText.setTextSize(18);   // Set font slightly smaller, this is the date & time
        dateText.setText(day +"/" + month + "/" + year + ", " + String.format("%02d:%02d", hour, minute));  // DD/MM/YYYY format , time


        scroll.addView(taskText);   // adding task & date/time to the scroll view
        scroll.addView(dateText);
    }



    private boolean isDuplicate(Task t){
        return dates.contains(t.getCalendar());

    }

//    private void reSort(){
//        if(list != null){
//            list.sort();
//
//        }
//
//    }

}