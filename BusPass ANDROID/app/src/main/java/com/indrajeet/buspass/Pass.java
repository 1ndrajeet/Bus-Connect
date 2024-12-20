package com.indrajeet.buspass;

public class Pass {
    private String from;
    private String to;
    private String status;
    private String userName;
    private String expiryDate;

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Pass(String from, String to, String status, String userName, String expiryDate) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.userName = userName;
        this.expiryDate = expiryDate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }
}
