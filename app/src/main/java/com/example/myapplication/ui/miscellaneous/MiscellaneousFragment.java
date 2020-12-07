package com.example.myapplication.ui.miscellaneous;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;



public class MiscellaneousFragment extends Fragment {

    private MiscellaneousViewModel miscellaneousViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miscellaneousViewModel = ViewModelProviders.of(this).get(MiscellaneousViewModel.class);
        View root = inflater.inflate(R.layout.fragment_miscellaneous, container, false);

        Button loginBtn      = root.findViewById(R.id.loginButton);
        Button logoutBtn     = root.findViewById(R.id.logoutButton);
        Button passwordBtn   = root.findViewById(R.id.passwordChange);
        Button emailBtn      = root.findViewById(R.id.verifyEmail);
        Button confirmBtn    = root.findViewById(R.id.confirmChange);

        EditText confirmEmail     = root.findViewById(R.id.confirmEmail);
        EditText currentPassword  = root.findViewById(R.id.confirmPassword);
        EditText newPassword      = root.findViewById(R.id.newPassword);

        loginBtn.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        passwordBtn.setVisibility(View.GONE);
        emailBtn.setVisibility(View.GONE);
        confirmBtn.setVisibility(View.GONE);

        confirmEmail.setVisibility(View.GONE);
        currentPassword.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);

        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
        {
            loginBtn.setVisibility(View.VISIBLE);
        }
        else{
            logoutBtn.setVisibility(View.VISIBLE);
            passwordBtn.setVisibility(View.VISIBLE);
            emailBtn.setVisibility(View.VISIBLE);
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

        passwordBtn.setOnClickListener(v -> {
            confirmEmail.setVisibility(View.VISIBLE);
            currentPassword.setVisibility(View.VISIBLE);
            newPassword.setVisibility(View.VISIBLE);
            confirmBtn.setVisibility(View.VISIBLE);
        });

        confirmBtn.setOnClickListener((v -> {
            Toast.makeText(getContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
        }));

        emailBtn.setOnClickListener((v -> {
            Toast.makeText(getContext(), "Email Link Sent!", Toast.LENGTH_SHORT).show();
        }));
        return root;
    }
}