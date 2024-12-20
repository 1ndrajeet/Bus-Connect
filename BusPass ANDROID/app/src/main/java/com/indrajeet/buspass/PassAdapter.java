package com.indrajeet.buspass;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.PassViewHolder> {
    private final List<Pass> passList;

    public PassAdapter(List<Pass> passList) {
        this.passList = passList;
    }

    @NonNull
    @Override
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pass, parent, false);
        return new PassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassViewHolder holder, int position) {
        Pass pass = passList.get(position);
        holder.fromTextView.setText("From: " + pass.getFrom());
        holder.toTextView.setText("To: " + pass.getTo());
        holder.userNameTextView.setText("User: " + pass.getUserName());
        holder.statusTextView.setText("" + pass.getStatus());
        holder.expiryDateTextView.setText("Exp. Date: " + pass.getExpiryDate());

        switch (pass.getStatus().toLowerCase()) {
            case "approved":
                holder.cardView.setCardBackgroundColor(Color.parseColor("#A5D6A7"));
                break;
            case "rejected":
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FF8A80"));
                break;
            case "pending":
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF59D"));
                break;
            default:
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return passList.size();
    }

    static class PassViewHolder extends RecyclerView.ViewHolder {
        TextView fromTextView;
        TextView toTextView;
        TextView statusTextView;
        TextView userNameTextView;
        TextView expiryDateTextView;
        CardView cardView;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            expiryDateTextView = itemView.findViewById(R.id.expiryDateTextView); // Initialize expiry date TextView
            cardView = (CardView) itemView;
        }
    }
}
