package com.zybooks.mobile2app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

    public class LoginActivity extends AppCompatActivity {

        private DatabaseHelper dbHelper;
        private EditText usernameField, passwordField;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Sets starting screen to login screen on Launch
            setContentView(R.layout.activity_login);

            // Initializes a new DatabaseHelper to handle database functions
            dbHelper = new DatabaseHelper(this);

            usernameField = findViewById(R.id.usernameField);
            passwordField = findViewById(R.id.passwordField);
            Button loginButton = findViewById(R.id.loginButton);
            Button registerButton = findViewById(R.id.registerButton);

            loginButton.setOnClickListener(v -> {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Using a background thread to run Database check and prevent crashing
                new Thread(() -> {
                    boolean validUser = dbHelper.checkUser(username, password);
                    runOnUiThread(() -> {
                        if (validUser) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                         } else {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                     });
                }).start();
            });


            registerButton.setOnClickListener(v -> {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Using background thread again to check database
                    new Thread(() -> {
                        boolean success = dbHelper.addUser(username, password);
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Account created! You can now log in.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            });
        }



    }
