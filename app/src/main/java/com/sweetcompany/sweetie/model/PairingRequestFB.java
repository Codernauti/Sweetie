package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 03-Jul-17.
 */

public class PairingRequestFB {

    @Exclude
    private String key;
    private String username;
    private String phoneNumber;

    public PairingRequestFB() {}

    public PairingRequestFB(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
