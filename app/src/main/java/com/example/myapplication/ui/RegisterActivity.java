package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText registerPw, confirmPw, newEmail;
    Button loginButton, registerButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Setup the variables to be used from the view.

        newEmail       = findViewById(R.id.registerEmail);
        registerPw     = findViewById(R.id.registerPw1);
        confirmPw      = findViewById(R.id.registerPw2);
        loginButton    = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerBtn);

        fAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            String email = newEmail.getText().toString().trim();
            String password = registerPw.getText().toString().trim();

            //To simplify the password creation process, this criteria makes it easier for the user to create a password
            if(password.length() < 6){
                Toast.makeText(RegisterActivity.this, "Please, create a password with 6 or more characters.", Toast.LENGTH_SHORT).show();
            }
            else {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Welcome to Trip-Ease!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }


}