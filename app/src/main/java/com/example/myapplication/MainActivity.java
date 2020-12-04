package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onStart(){
        super.onStart();
        //Makes sure the user isn't already logged in
        if(FirebaseAuth.getInstance().getCurrentUser()== null){
            System.out.println("No user is signed in.. somehow?");
        }
        else{
            updateUI(FirebaseAuth.getInstance().getCurrentUser());
        }
    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            System.out.println("User has signed into the Main Activity");
            System.out.println(FirebaseAuth.getInstance().getCurrentUser());

        }else {
            Toast.makeText(this,"Something went wrong..",Toast.LENGTH_SHORT).show();
        }
    }
}