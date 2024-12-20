package com.indrajeet.buspass.admin;

public class Pass {
    private int id;
    private int userId;
    private String userName; // Added userName field
    private String fromDestination;
    private String toDestination;
    private String paymentMode;
    private String status;
    private String createdAt;
    private String expirationDate;

    public Pass(int id, int userId, String userName, String fromDestination, String toDestination, String paymentMode, String status, String createdAt, String expirationDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName; // Initialize userName
        this.fromDestination = fromDestination;
        this.toDestination = toDestination;
        this.paymentMode = paymentMode;
        this.status = status;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName; // Getter for userName
    }

    public String getFromDestination() {
        return fromDestination;
    }

    public String getToDestination() {
        return toDestination;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
