package com.indrajeet.buspass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    TextView welcomeTextView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Get user information from the database
        String userId = dbHelper.getUserId();
        if (userId != null) {
            user = dbHelper.getUserById(userId);
            if (user != null) {
                welcomeTextView.setText("Welcome, " + user.getName() + "!");

                TextView userEmailTextView = findViewById(R.id.userEmailTextView);
                TextView userNumberTextView = findViewById(R.id.userNumberTextView);

                userEmailTextView.setText("Email: " + user.getEmail());
                userNumberTextView.setText("Phone: " + user.getNumber());
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
        }

        MaterialButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            dbHelper.clearUser();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        MaterialCardView currentPassCard = findViewById(R.id.currentPassCard);
        currentPassCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CurrentPassActivity.class);
            intent.putExtra("USER_ID", user.getUid());
            startActivity(intent);
        });

        MaterialCardView newPassCard = findViewById(R.id.newPassCard);
        newPassCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewPassActivity.class);
            intent.putExtra("USER_ID", user.getUid());
            startActivity(intent);
        });
    }
}
