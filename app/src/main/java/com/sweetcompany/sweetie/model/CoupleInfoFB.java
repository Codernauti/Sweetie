package com.sweetcompany.sweetie.model;

/**
 * Created by Eduard on 11-Jul-17.
 */

public class CoupleInfoFB {

    private String activeCoupleUid;
    private String creationTime;
    private String brokeTime;
    /* TODO: put other data */

    public CoupleInfoFB() {}

    public CoupleInfoFB(String activeCoupleUid, String creationTime) {
        this.activeCoupleUid = activeCoupleUid;
        this.creationTime = creationTime;
    }

    public String getActiveCoupleUid() {
        return activeCoupleUid;
    }

    public void setActiveCoupleUid(String activeCoupleUid) {
        this.activeCoupleUid = activeCoupleUid;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getBrokeTime() {
        return brokeTime;
    }

    public void setBrokeTime(String brokeTime) {
        this.brokeTime = brokeTime;
    }
}
