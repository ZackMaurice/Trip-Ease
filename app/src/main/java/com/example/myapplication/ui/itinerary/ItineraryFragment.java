package com.example.myapplication.ui.itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class ItineraryFragment extends Fragment {

    private ItineraryViewModel itineraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itineraryViewModel =
                ViewModelProviders.of(this).get(ItineraryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_itinerary, container, false);
        System.out.println("Hello");
        final TextView textView = root.findViewById(R.id.text_itinerary);
        itineraryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}