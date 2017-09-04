package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionFB {

    public final static int CHAT = 0;
    public final static int GALLERY = 1;
    public final static int TODOLIST = 2;
    public final static int GEOGIFT = 3;

    private String key;
    private String userCreator;
    private String title;
    private String lastUser;
    private String description;
    private String dataTime;
    private int type;
    private String childKey;
    private String actionKey;
    @Deprecated
    private boolean isOpened;
    private boolean isTriggered; //geogift
    private String imageUrl;
    private HashMap<String, ActionNotification> notificationCounters;

    ActionFB() {}

    public ActionFB(String title, String usrCreator, String lastUser, String description, String date, int type) {
        this.title = title;
        this.userCreator = usrCreator;
        this.lastUser = lastUser;
        this.description = description;
        this.dataTime = date;
        this.type = type;

        this.notificationCounters = new HashMap<>();
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getUserCreator() {
        return userCreator;
    }
    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUser() {
        return lastUser;
    }
    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataTime() {
        return dataTime;
    }
    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getChildKey() {
        return childKey;
    }
    public void setChildKey(String childKey) {
        this.childKey = childKey;
    }

    public String getActionKey() {
        return actionKey;
    }
    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public boolean isOpened() {
        return isOpened;
    }
    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    public boolean getIsTriggered() {
        return isTriggered;
    }
    public void setIsTriggered(boolean isTriggered) {
        this.isTriggered = isTriggered;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, ActionNotification> getNotificationCounters() {
        return notificationCounters;
    }

    public void setNotificationCounters(HashMap<String, ActionNotification> notificationCounters) {
        this.notificationCounters = notificationCounters;
    }
}
