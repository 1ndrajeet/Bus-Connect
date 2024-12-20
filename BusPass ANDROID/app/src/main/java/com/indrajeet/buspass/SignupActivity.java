package com.indrajeet.buspass;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextInputEditText nameEditText = findViewById(R.id.nameEditText);
        TextInputEditText emailEditText = findViewById(R.id.emailEditText);
        TextInputEditText phoneEditText = findViewById(R.id.phoneEditText);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);
        MaterialButton signupButton = findViewById(R.id.signupButton);
        TextView loginLink = findViewById(R.id.loginLink);

        signupButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(name, email, phone, password)) {
                signupUser(ApiUrls.SIGNUP_URL, name, email, phone, password);
            } else {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v -> {
            // Code to navigate back to LoginActivity
            finish(); // Close SignupActivity and return to LoginActivity
        });
    }

    private boolean validateInputs(String name, String email, String phone, String password) {
        return !name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty();
    }

    private void signupUser(String url, String name, String email, String phone, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("number", phone);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(SignupActivity.this, "Signup failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }
}
