package com.indrajeet.buspass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class NewPassActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView spinnerRoutes, spinnerDuration, spinnerPayment;
    private TextView priceTextView;
    private Button submitButton;
    private ProgressBar progressBar;

    private ArrayList<String> routeList;
    private HashMap<String, Integer> routeRates;

    private static final String NEW_PASS_REQUEST_URL = ApiUrls.NEW_PASS_REQUEST_URL;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_pass);

        spinnerRoutes = findViewById(R.id.routeSpinner);
        spinnerDuration = findViewById(R.id.durationSpinner);
        spinnerPayment = findViewById(R.id.paymentSpinner);
        priceTextView = findViewById(R.id.priceTextView);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        routeList = new ArrayList<>();
        routeRates = new HashMap<>();

        loadRoutesFromJson();
        initializeSpinner();
        setupDurationSpinner();
        setupPaymentSpinner();

        submitButton.setOnClickListener(view -> submitNewPassRequest());
    }

    private void loadRoutesFromJson() {
        try {
            InputStream inputStream = getAssets().open("routes.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            String json = stringBuilder.toString();
            parseJsonRoutes(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonRoutes(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject routeObject = routesArray.getJSONObject(i);
                String routeName = routeObject.getString("route");
                int rate = routeObject.getInt("rate");
                routeList.add(routeName);
                routeRates.put(routeName, rate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, routeList);
        spinnerRoutes.setAdapter(adapter);

        spinnerRoutes.setOnItemClickListener((parent, view, position, id) -> updatePrice());
    }

    private void setupDurationSpinner() {
        ArrayList<String> durationList = new ArrayList<>();
        durationList.add("1");
        durationList.add("2");
        durationList.add("3");
        durationList.add("6");
        durationList.add("12");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durationList);
        spinnerDuration.setAdapter(adapter);

        spinnerDuration.setOnItemClickListener((parent, view, position, id) -> updatePrice());
    }

    private void setupPaymentSpinner() {
        ArrayList<String> paymentModes = new ArrayList<>();
        paymentModes.add("Cash");
        paymentModes.add("Credit Card");
        paymentModes.add("Debit Card");
        paymentModes.add("UPI");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, paymentModes);
        spinnerPayment.setAdapter(adapter);
    }

    private void updatePrice() {
        String selectedRoute = spinnerRoutes.getText().toString();
        String selectedDuration = spinnerDuration.getText().toString();

        if (routeRates.containsKey(selectedRoute) && !selectedDuration.isEmpty()) {
            int rate = routeRates.get(selectedRoute);
            int duration = Integer.parseInt(selectedDuration);
            int totalPrice = rate * duration;
            priceTextView.setText("Price: ₹" + totalPrice);
        } else {
            priceTextView.setText("Price: ₹0");
        }
    }

    private void submitNewPassRequest() {
        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }


        String selectedRoute = spinnerRoutes.getText().toString();
        String selectedDuration = spinnerDuration.getText().toString();
        String selectedPaymentMode = spinnerPayment.getText().toString();
        int durationMonths = Integer.parseInt(selectedDuration);
        int rate = routeRates.get(selectedRoute);
        int totalPrice = rate * durationMonths;

        try {
            JSONObject routeObject = getRouteObject(selectedRoute);
            JSONObject requestBody = new JSONObject();
            requestBody.put("user_id", userId);
            requestBody.put("from", routeObject.getString("from"));
            requestBody.put("to", routeObject.getString("to"));
            requestBody.put("paymentMode", selectedPaymentMode);
            requestBody.put("duration", durationMonths);

            progressBar.setVisibility(View.VISIBLE);
            submitButton.setEnabled(false);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NEW_PASS_REQUEST_URL, requestBody,
                    response -> {
                        Toast.makeText(NewPassActivity.this, "New pass request submitted!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        submitButton.setEnabled(true);
                        Intent i = new Intent(NewPassActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }, error -> {
                Toast.makeText(NewPassActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
            });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getRouteObject(String routeName) {
        try {
            InputStream inputStream = getAssets().open("routes.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            String json = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject routeObject = routesArray.getJSONObject(i);
                if (routeObject.getString("route").equals(routeName)) {
                    return routeObject;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
