package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.Exclude;

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
    private CoupleInfoFB coupleInfo;
    private String futurePartner;

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

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public CoupleInfoFB getCoupleInfo() {
        return coupleInfo;
    }

    public void setCoupleInfo(CoupleInfoFB coupleInfo) {
        this.coupleInfo = coupleInfo;
    }

    public String getFuturePartner() {
        return futurePartner;
    }

    public void setFuturePartner(String futurePartner) {
        this.futurePartner = futurePartner;
    }
}