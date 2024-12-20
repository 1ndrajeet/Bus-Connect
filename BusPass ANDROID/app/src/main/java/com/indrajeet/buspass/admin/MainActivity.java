package com.indrajeet.buspass.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;
import com.indrajeet.buspass.DatabaseHelper;
import com.indrajeet.buspass.LoginActivity;
import com.indrajeet.buspass.R;
import com.indrajeet.buspass.admin.ManageRequests;

public class MainActivity extends AppCompatActivity {
    private MaterialCardView currentPassCard;
    private MaterialButton logoutButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        currentPassCard = findViewById(R.id.currentPassCard);
        logoutButton = findViewById(R.id.logoutButton);

        currentPassCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageRequests.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
