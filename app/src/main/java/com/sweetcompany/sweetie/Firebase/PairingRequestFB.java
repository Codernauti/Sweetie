package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 03-Jul-17.
 */

public class PairingRequestFB {

    @Exclude
    private String key;
    private String phoneNumber;

    public PairingRequestFB() {}

    public PairingRequestFB(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
