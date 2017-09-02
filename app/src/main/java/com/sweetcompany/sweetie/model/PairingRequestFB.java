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
    private String imageUri;

    public PairingRequestFB() {}

    public PairingRequestFB(String username, String phoneNumber, String imageUri) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    @Exclude
    public String getKey() {
        return this.key;
    }
    @Exclude
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

    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
