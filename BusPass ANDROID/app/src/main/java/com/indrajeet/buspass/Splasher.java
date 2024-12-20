// Splasher.java
package com.indrajeet.buspass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splasher extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasher);

        dbHelper = new DatabaseHelper(this);
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.title);
        TextView tagline = findViewById(R.id.tagline);

        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide);
        logo.startAnimation(slideIn);
        tagline.startAnimation(slideIn);
        title.startAnimation(slideIn);

        new Handler().postDelayed(() -> {
            if (dbHelper.isUserLoggedIn()) {
                Intent intent = new Intent(Splasher.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Splasher.this, LoginActivity.class);
                startActivity(intent);
            }
            finish();
        }, SPLASH_DURATION);
    }
}
