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

public class ItineraryFragment extends Fragment {








    private ItineraryViewModel itineraryViewModel;
    private Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itineraryViewModel =
                ViewModelProviders.of(this).get(ItineraryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_itinerary, container, false);
        //System.out.println("Hello");
        /*final TextView textView = root.findViewById(R.id.text_itinerary);
        itineraryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


        context = container.getContext();   // context to use when making new xml elements

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
    private void init(View root){

        final ConstraintLayout mainView = root.findViewById(R.id.itinerary_main);   // main view
        final ConstraintLayout addView = root.findViewById(R.id.itinerary_adding);    // adding view

        final DatePicker datePicker = root.findViewById(R.id.itinerary_date);   // date input
        final TimePicker timePick = root.findViewById(R.id.itinerary_time); // time input
        final TextView input = root.findViewById(R.id.itinerary_input); // task input

        final Button goToAddView = root.findViewById(R.id.itinerary_goAddView);   // Button that reveals the view to add things and hides the itinerary
        final Button addToItinerary = root.findViewById(R.id.itinerary_add);  // Button that reveals the itinerary view and hides the view that adds things

        final LinearLayout scroll = root.findViewById(R.id.itinerary_scroll); // Linear Layout used in the ScrollView




        goToAddView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                addView.setVisibility(View.VISIBLE);
                mainView.setVisibility(View.GONE);

            }

        });

        addToItinerary.setOnClickListener(new View.OnClickListener() {


            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v){

                String newTask = input.getText().toString();    // getText returns a CharSequence for some reason, needed a toString


                // getting time & day information from datePicker and timePicker
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth()+1;    // month goes 0-11 with DatePicker
                int year = datePicker.getYear();
                int minute = timePick.getMinute();  // requires api 23
                int hour = timePick.getHour();      // requires api 23



                TextView taskText = new TextView(context);  // making a TextView for the thing to be done
                TextView dateText = new TextView(context);  // making a TextView for the date & time of said thing
                taskText.setText(newTask);   // "Task"
                taskText.setTextSize(24);   // Set font
                dateText.setTextSize(18);   // Set font slightly smaller, this is the date & time
                dateText.setText(day +"/" + month + "/" + year + ", " + String.format("%02d:%02d", hour, minute));            // DD/MM/YYYY format , time



                scroll.addView(taskText);   // adding task & date/time to the scroll view
                scroll.addView(dateText);


                addView.setVisibility(View.GONE);   // make the addView invisible and bring back the itinerary
                mainView.setVisibility(View.VISIBLE);




            }

        });





    }



}