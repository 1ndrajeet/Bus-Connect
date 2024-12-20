package com.indrajeet.buspass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CurrentPassActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PassAdapter passAdapter;
    private List<Pass> passList;
    private ProgressBar progressBar;

    private static final String STATUS_KEY = "status";
    private static final String PASSES_KEY = "passes";
    private static final String ERROR_PARSING_DATA = "Error parsing data";
    private static final String NETWORK_ERROR = "Network error: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_pass);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        passList = new ArrayList<>();
        passAdapter = new PassAdapter(passList);
        recyclerView.setAdapter(passAdapter);

        loadCurrentPasses();
    }

    private void loadCurrentPasses() {
        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ApiUrls.GET_USER_PASSES_URL + userId;

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d("CurrentPassActivity", "Response: " + response.toString());
                    try {
                        String status = response.getString(STATUS_KEY);
                        if ("success".equals(status)) {
                            JSONArray passesArray = response.getJSONArray(PASSES_KEY);
                            for (int i = 0; i < passesArray.length(); i++) {
                                JSONObject passObject = passesArray.getJSONObject(i);
                                String from = passObject.getString("from_destination");
                                String to = passObject.getString("to_destination");
                                String statusPass = passObject.getString("status");
                                String expDate = passObject.getString("expiration_date");
                                String userName = passObject.optString("user_name", "Unknown User");

                                passList.add(new Pass(from, to, statusPass, userName,expDate));
                            }
                            passAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CurrentPassActivity.this, "Failed to load passes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("CurrentPassActivity", ERROR_PARSING_DATA, e);
                        Toast.makeText(CurrentPassActivity.this, ERROR_PARSING_DATA, Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
            if (error.networkResponse != null && error.networkResponse.data != null) {
                String errorMessage = new String(error.networkResponse.data);
                Log.e("CurrentPassActivity", "Error response: " + errorMessage);
                Toast.makeText(CurrentPassActivity.this, NETWORK_ERROR + errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("CurrentPassActivity", "Network error: " + error.getMessage(), error);
                Toast.makeText(CurrentPassActivity.this, NETWORK_ERROR + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
