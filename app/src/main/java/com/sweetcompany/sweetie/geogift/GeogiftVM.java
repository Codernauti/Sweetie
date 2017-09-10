package com.sweetcompany.sweetie.geogift;

/**
 * Created by ghiro on 16/08/2017.
 */

public class GeogiftVM {

    public final static int MESSAGE_GEOGIFT = 0;
    public final static int PHOTO_GEOGIFT = 1;
    public final static int HEART_GEOGIFT = 2;

    private String mKey;
    private String mUserCreator;
    private int mType;
    private String mTitle;
    private String mMessage;
    private String mAddress;
    private Double mLat;
    private Double mLon;
    private String mUriStorage;
    private boolean mIsBookmarked;
    private String mCreationDate;
    private String mVisitedDate;
    private boolean mIsTriggered;

    public GeogiftVM() {
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getUserCreatorUID() {
        return mUserCreator;
    }
    public void setUserCreatorUID(String userCreatorUID) {
        this.mUserCreator = userCreatorUID;
    }

    public int getType() {
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getMessage() {
        return mMessage;
    }
    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getAddress() {
        return mAddress;
    }
    public void setAddress(String address) {
        this.mAddress = address;
    }

    public Double getLat() {
        return mLat;
    }
    public void setLat(Double lat) {
        this.mLat = lat;
    }

    public Double getLon() {
        return mLon;
    }
    public void setLon(Double lon) {
        this.mLon = lon;
    }

    public String getUriStorage() {
        return mUriStorage;
    }
    public void setUriStorage(String uriS) {
        this.mUriStorage = uriS;
    }

    public boolean isBookmarked() {
        return mIsBookmarked;
    }
    public void setBookmarked(boolean bookmarked) {
        this.mIsBookmarked = bookmarked;
    }

    public String getDatetimeVisited() {
        return mVisitedDate;
    }
    public void setDatetimeVisited(String datetimeVisited) {
        this.mVisitedDate = datetimeVisited;
    }

    public boolean getIsTriggered() {
        return mIsTriggered;
    }
    public void setIsTriggered(boolean triggered) {
        this.mIsTriggered = triggered;
    }

    public String getCreationDate() {
        return mCreationDate;
    }
    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }
}
