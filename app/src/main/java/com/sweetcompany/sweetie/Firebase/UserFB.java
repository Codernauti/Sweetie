package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.Exclude;

import java.io.File;
import java.sql.Date;

/**
 * Created by ghiro on 17/05/2017.
 */

public class UserFB {
    @Exclude
    private String key;
    private String username;
    private boolean gender;
    private String phone;
    private String email;

    public UserFB(){}

    public UserFB(String username, String email, String phone, boolean gender) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        this.username = mUsername;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean mGender) {
        this.gender = mGender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String mPhone) {
        this.phone = mPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setmEmail(String mEmail) {
        this.email = mEmail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}