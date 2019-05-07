package com.example.instagramclone.Modals;

public class SearchModal {

    String username;
    boolean isPresent;

    public SearchModal(String username, boolean isPresent) {
        this.username = username;
        this.isPresent = isPresent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
