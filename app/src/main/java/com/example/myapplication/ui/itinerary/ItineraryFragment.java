package com.example.myapplication.ui.itinerary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class ItineraryFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView; // RecyclerView used for displaying items
    private ItineraryAdapter adapter;   // adapter to be used with recyclerview

    // declaring firebase variables
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    // declaring and sort-of instantiating the arraylist of view models
    private ArrayList<ItineraryViewModel> tasks = new ArrayList<>();


    // declaring XML objects
    private ConstraintLayout mainView;
    private ConstraintLayout addView;

    private DatePicker datePicker;
    private TimePicker timePick;
    private TextView input;
    private TextView notes;

    private Button goToAddView;
    private Button addToItinerary;
    private Button backButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_itinerary, container, false);

        context = container.getContext();   // context to use when making new xml elements

        // instantiating firebase variables
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = database.getReference("itinerary/" + user.getUid());


        // Initializing all variables/ references to XML objects
        adapter = new ItineraryAdapter(tasks);
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));


        mainView = root.findViewById(R.id.itinerary_main);   // main view
        addView = root.findViewById(R.id.itinerary_adding);    // adding view

        datePicker = root.findViewById(R.id.itinerary_date);   // date input
        timePick = root.findViewById(R.id.itinerary_time); // time input
        input = root.findViewById(R.id.itinerary_input); // task input
        notes = root.findViewById(R.id.itinerary_notes); // notes for the trip

        goToAddView = root.findViewById(R.id.itinerary_goAddView);   // Button that reveals the view to add things and hides the itinerary
        addToItinerary = root.findViewById(R.id.itinerary_add);  // Button that reveals the itinerary view and hides the view that adds things
        backButton = root.findViewById(R.id.itinerary_back);   // Button that goes back, in case the user doesn't want to add the new task to the itinerary

        // Start
        init();
        enableSwipeDelete();

        return root;
    }

    /*  Master function for the Itinerary fragment
     *   Sets up all listeners
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {

        // Import data from firebase
        // When there is a change to the data in the directory itinerary/<uid>, update the data
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tasks.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    tasks.add(d.getValue(ItineraryViewModel.class));
                    adapter.notifyDataSetChanged();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notification();
                    }
                }


            }
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("\n\nLoad failed.\n");
            }
        });


        // Setting onclick listeners for buttons to switch between either the adding view, or the itinerary/base view
        goToAddView.setOnClickListener(v -> {
            addView.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);

        });

        backButton.setOnClickListener(v -> {
            addView.setVisibility(View.GONE);
            mainView.setVisibility(View.VISIBLE);
            input.setText("");


        });

        // this is the button "Add" on the adding view that confirms the inputs and sends them to the base view.
        addToItinerary.setOnClickListener(v -> {

            String newTask = input.getText().toString();    // getText returns a CharSequence, needs a toString
            input.setText("");
            String note = notes.getText().toString();
            notes.setText("");


            Calendar calendar = Calendar.getInstance();// long value for date in ms to be stored in view model

            // getting time & day information from datePicker and timePicker
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();    // month goes 0-11 with DatePicker
            int year = datePicker.getYear();
            int minute = timePick.getMinute();  // requires api 23
            int hour = timePick.getHour();      // requires api 23


            // Setting up a calendar since most of Date is deprecated
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);


            addView.setVisibility(View.GONE);   // make the addView invisible and bring back the itinerary
            mainView.setVisibility(View.VISIBLE);


            long time = calendar.getTimeInMillis();

            tasks.add(new ItineraryViewModel(newTask, note, time));
            adapter.notifyDataSetChanged();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification();
            }
            ref.setValue(tasks); // send the data to firebase
        });

    }

    // Swipe To Delete from the Checklist
    public void enableSwipeDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final ItineraryViewModel item = tasks.get(position);
                tasks.remove(position);
                ref.setValue(tasks);
                adapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    // from android documentation
    // https://developer.android.com/training/notify-user/channels
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "mainChannel";
            String description = "Trip-Ease Itinerary";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TRIP-EASE MAIN CHANNEL", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification() {

        createNotificationChannel();    // need to create the notification channel so that notifications can be sent

        LocalDateTime now = LocalDateTime.now();    // time to use to compare itinerary tasks

        // Check all itinerary objects, if its the same day as today, send notification
        for (ItineraryViewModel i : tasks) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(i.getTime());
            if (now.getDayOfYear() == c.get(Calendar.DAY_OF_YEAR)) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TRIP-EASE MAIN CHANNEL")  // not entirely sure what channels really are
                        .setSmallIcon(R.drawable.ic_launcher_foreground)    // currently a placeholder image
                        .setContentTitle("Trip-Ease - Don't forget: " + i.getText())    // notification title body that is sent to the user
                        .setContentText(i.getNotes())   // notes the user put on the itinerary task
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);  // default notification priority

                // sending the notification
                NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                manager.notify(1, builder.build());
            }

        }


    }
}