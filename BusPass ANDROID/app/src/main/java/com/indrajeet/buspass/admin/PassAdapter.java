package com.indrajeet.buspass.admin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.indrajeet.buspass.R;
import java.util.List;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.PassViewHolder> {
    private List<Pass> passList;
    private OnPassActionListener actionListener;

    public PassAdapter(List<Pass> passList, OnPassActionListener actionListener) {
        this.passList = passList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_pass, parent, false);
        return new PassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassViewHolder holder, int position) {
        Pass pass = passList.get(position);
        holder.fromDestinationTextView.setText("From: " + pass.getFromDestination());
        holder.toDestinationTextView.setText("To: " + pass.getToDestination());
        holder.paymentModeTextView.setText("Payment Mode: " + pass.getPaymentMode());
        holder.statusTextView.setText("" + pass.getStatus());
        holder.createdAtTextView.setText("Created At: " + pass.getCreatedAt());
        holder.expirationDateTextView.setText("Expiration Date: " + pass.getExpirationDate());
        holder.usernameTextView.setText("User: " + pass.getUserName());

        holder.approveButton.setOnClickListener(v -> actionListener.onApprove(pass.getId()));
        holder.rejectButton.setOnClickListener(v -> actionListener.onReject(pass.getId()));

        updateCardColor(pass, holder);
        updateButtonStates(pass, holder);
    }

    private void updateCardColor(Pass pass, PassViewHolder holder) {
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

    private void updateButtonStates(Pass pass, PassViewHolder holder) {
        switch (pass.getStatus().toLowerCase()) {
            case "approved":
                holder.approveButton.setEnabled(false);
                holder.rejectButton.setEnabled(true);
                break;
            case "rejected":
                holder.approveButton.setEnabled(true);
                holder.rejectButton.setEnabled(false);
                break;
            case "pending":
            default:
                holder.approveButton.setEnabled(true);
                holder.rejectButton.setEnabled(true);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return passList.size();
    }

    static class PassViewHolder extends RecyclerView.ViewHolder {
        TextView fromDestinationTextView, toDestinationTextView, paymentModeTextView, statusTextView, createdAtTextView, expirationDateTextView, usernameTextView;
        Button approveButton, rejectButton;
        CardView cardView;

        PassViewHolder(@NonNull View itemView) {
            super(itemView);
            fromDestinationTextView = itemView.findViewById(R.id.fromTextView);
            toDestinationTextView = itemView.findViewById(R.id.toTextView);
            paymentModeTextView = itemView.findViewById(R.id.paymentMode);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            createdAtTextView = itemView.findViewById(R.id.createdDateTextView);
            expirationDateTextView = itemView.findViewById(R.id.expiryDateTextView);
            usernameTextView = itemView.findViewById(R.id.userNameTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnPassActionListener {
        void onApprove(int passId);
        void onReject(int passId);
    }
}
