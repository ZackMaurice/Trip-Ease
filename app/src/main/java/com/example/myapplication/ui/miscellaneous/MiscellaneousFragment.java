package com.example.myapplication.ui.miscellaneous;

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

public class MiscellaneousFragment extends Fragment {

    private MiscellaneousViewModel miscellaneousViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miscellaneousViewModel =
                ViewModelProviders.of(this).get(MiscellaneousViewModel.class);
        View root = inflater.inflate(R.layout.fragment_miscellaneous, container, false);
        final TextView textView = root.findViewById(R.id.text_miscellaneous);
        miscellaneousViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}