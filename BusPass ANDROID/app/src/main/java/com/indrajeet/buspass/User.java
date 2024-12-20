package com.indrajeet.buspass;

public class User {
    private String uid; // Rename id to uid
    private String name;
    private String email;
    private String number;
    private String password;

    public User(String uid, String name, String email, String number, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
    }

    public String getUid() {
        return uid; // Rename method to getUid
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }
}
