package com.sweetcompany.sweetie.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 04-Jul-17.
 */

public class CoupleFB {

    private String creationTime;
    private String breakTime;
    private String partnerOneUid;
    private String partnerTwoUid;
    private String partnerOneUsername;
    private String partnerTwoUsername;
    private String imageStorageUri;
    private String anniversary;

    public CoupleFB() {}

    public CoupleFB(String userUid, String partnerUid,
                    String userUsername, String partnerUsername, String creationTime) {
        partnerOneUid = userUid;
        partnerTwoUid = partnerUid;
        partnerOneUsername = userUsername;
        partnerTwoUsername = partnerUsername;
        this.creationTime = creationTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getPartnerOneUid() {
        return partnerOneUid;
    }

    public void setPartnerOneUid(String partnerOneUid) {
        this.partnerOneUid = partnerOneUid;
    }

    public String getPartnerTwoUid() {
        return partnerTwoUid;
    }

    public void setPartnerTwoUid(String partnerTwoUid) {
        this.partnerTwoUid = partnerTwoUid;
    }

    public String getPartnerOneUsername() {
        return partnerOneUsername;
    }
    public void setPartnerOneUsername(String partnerOneUsername) {
        this.partnerOneUsername = partnerOneUsername;
    }

    public String getPartnerTwoUsername() {
        return partnerTwoUsername;
    }
    public void setPartnerTwoUsername(String partnerTwoUsername) {
        this.partnerTwoUsername = partnerTwoUsername;
    }

    public String getImageStorageUri() {
        return imageStorageUri;
    }
    public void setImageStorageUri(String imageStorageUri) {
        this.imageStorageUri = imageStorageUri;
    }

    public String getAnniversary() {
        return this.anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }
}
