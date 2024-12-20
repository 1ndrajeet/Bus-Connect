package com.indrajeet.buspass.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.indrajeet.buspass.ApiUrls;
import com.indrajeet.buspass.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageRequests extends AppCompatActivity implements PassAdapter.OnPassActionListener {
    private RecyclerView passRecyclerView;
    private PassAdapter passAdapter;
    private List<Pass> passList;
    private static final String ADMIN_GET_PASSES_URL = ApiUrls.ADMIN_GET_PASSES_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        passRecyclerView = findViewById(R.id.passRecyclerView);
        passRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        passList = new ArrayList<>();
        passAdapter = new PassAdapter(passList, this);
        passRecyclerView.setAdapter(passAdapter);

        fetchPassData();
    }

    private void fetchPassData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                ADMIN_GET_PASSES_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                passList.clear();
                                JSONArray requestsArray = response.getJSONArray("requests");

                                for (int i = 0; i < requestsArray.length(); i++) {
                                    JSONObject jsonObject = requestsArray.getJSONObject(i);

                                    int id = jsonObject.getInt("id");
                                    int userId = jsonObject.getInt("user_id");
                                    String userName = jsonObject.getString("user_name");
                                    String from = jsonObject.getString("from_destination");
                                    String to = jsonObject.getString("to_destination");
                                    String paymentMode = jsonObject.getString("payment_mode");
                                    String status = jsonObject.getString("status");
                                    String createdAt = jsonObject.getString("created_at");
                                    String expirationDate = jsonObject.getString("expiration_date");

                                    Pass pass = new Pass(id, userId, userName, from, to, paymentMode, status, createdAt, expirationDate);
                                    passList.add(pass);
                                }
                                passAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ManageRequests.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ManageRequests.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onApprove(int passId) {
        sendApprovalOrRejectionRequest(passId, "approve");
    }

    @Override
    public void onReject(int passId) {
        sendApprovalOrRejectionRequest(passId, "reject");
    }

    private void sendApprovalOrRejectionRequest(int requestId, String action) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request_id", requestId);
            jsonObject.put("action", action);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    ADMIN_GET_PASSES_URL,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").equals("success")) {
                                    Toast.makeText(ManageRequests.this, "Request " + action + "d successfully", Toast.LENGTH_SHORT).show();
                                    fetchPassData(); // Refresh the list after action
                                } else {
                                    Toast.makeText(ManageRequests.this, "Failed to " + action + " the request", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ManageRequests.this, "Failed to " + action + " the request", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
