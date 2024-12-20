package com.indrajeet.buspass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText emailEditText = findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);
        MaterialButton loginButton = findViewById(R.id.loginButton);
        TextView signupLink = findViewById(R.id.signupLink);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                loginUser(ApiUrls.LOGIN_URL, email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    private void loginUser(String url, String email, String password) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        if ("admin".equals(email) && "pass123".equals(password)) {
            Intent intent = new Intent(LoginActivity.this, com.indrajeet.buspass.admin.MainActivity.class); // Redirect to admin activity
            startActivity(intent);
            loadingProgressBar.setVisibility(View.GONE);
            finish();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
            loadingProgressBar.setVisibility(View.GONE);
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                if ("success".equals(status)) {
                    JSONObject userInfo = response.getJSONObject("userinfo");
                    String userId = userInfo.getString("id");
                    String userName = userInfo.getString("name");
                    String userEmail = userInfo.getString("email");
                    String userNumber = userInfo.getString("number");

                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    dbHelper.insertUser(userId,userName, userEmail, userNumber, password);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("USER_NAME", userName);
                    intent.putExtra("USER_EMAIL", userEmail);
                    intent.putExtra("USER_NUMBER", userNumber);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonObjectRequest);
    }
}
