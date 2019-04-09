package com.afh.busbay.models;

public class Attendance {
    private String userId;
    private String username;
    private boolean userPresence;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isUserPresence() {
        return userPresence;
    }

    public void setUserPresence(boolean userPresence) {
        this.userPresence = userPresence;
    }
}
