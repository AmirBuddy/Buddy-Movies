package com.example.buddymovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "admin";
    private TextInputLayout usernameInputLayout, passwordInputLayout;
    private TextInputEditText usernameEditText, passwordEditText;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        preloadData();
        loginButton.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {
        String enteredUsername = Objects.requireNonNull(usernameEditText.getText()).toString().trim();
        String enteredPassword = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

        if (enteredUsername.equals(CORRECT_USERNAME) && enteredPassword.equals(CORRECT_PASSWORD)) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra("USERNAME", enteredUsername);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void preloadData() {
        // Fetch data from API and store it in cache
        DashboardViewModel.getInstance().saveToCache(true, DashboardActivity.GENRE_IDS);
        DashboardViewModel.getInstance().saveToCache(false, DashboardActivity.GENRE_IDS);
    }

    private void initializeViews() {
        usernameInputLayout = findViewById(R.id.usernameInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }
}