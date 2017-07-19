package com.sweetcompany.sweetie.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 04-Jul-17.
 */

public class CoupleFB {

    private List<String> users = new ArrayList<>();
    private String creationTime;
    private String breakTime;

    public CoupleFB() {}

    public CoupleFB(String userUid, String partnerUid, String creationTime) {
        users.add(userUid);
        users.add(partnerUid);
        this.creationTime = creationTime;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
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
}
