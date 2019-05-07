package com.example.instagramclone.Modals;

public class User {

    private String profile_pic, name, mobile_no, email_id;
    private String[] post_id;
    private String[] follow_list;

    public User(String profile_pic, String name, String mobile_no, String email_id, String[] post_id, String[] follow_list) {
        this.profile_pic = profile_pic;
        this.name = name;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.post_id = post_id;
        this.follow_list = follow_list;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String[] getPost_id() {
        return post_id;
    }

    public void setPost_id(String[] post_id) {
        this.post_id = post_id;
    }

    public String[] getFollow_list() {
        return follow_list;
    }

    public void setFollow_list(String[] follow_list) {
        this.follow_list = follow_list;
    }
}
