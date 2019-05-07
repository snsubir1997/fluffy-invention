package com.example.instagramclone.Modals;

public class Post {

    private String user, text, time;

    public Post(String user, String text, String time) {
        this.user = user;
        this.text = text;
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}