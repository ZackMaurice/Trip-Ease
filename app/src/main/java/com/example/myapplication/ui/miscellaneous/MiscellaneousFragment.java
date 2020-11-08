package com.example.myapplication.ui.miscellaneous;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.budget.BudgetViewModel;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
public class MiscellaneousFragment extends Fragment {

    private MiscellaneousViewModel miscellaneousViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miscellaneousViewModel = ViewModelProviders.of(this).get(MiscellaneousViewModel.class);
        View root = inflater.inflate(R.layout.fragment_miscellaneous, container, false);

        Button loginBtn   = root.findViewById(R.id.loginButton);
        Button logoutBtn  = root.findViewById(R.id.logoutButton);

        loginBtn.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            logoutBtn.setVisibility(View.VISIBLE);
        }
        else{
            loginBtn.setVisibility(View.VISIBLE);
        }
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        });
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        return root;
    }
}