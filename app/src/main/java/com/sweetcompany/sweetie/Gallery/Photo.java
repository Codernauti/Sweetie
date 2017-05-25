package com.sweetcompany.sweetie.Gallery;

/**
 * Created by Federico on 22/05/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;


public class Photo implements Parcelable {

    private String mUrl;
    private String mTitle;

    public Photo(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected Photo(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  Photo[] getPhotos() {

        return new Photo[]{
                new Photo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new Photo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new Photo("https://i.ytimg.com/vi/KEkrWRHCDQU/maxresdefault.jpg", "Galaxy Orion"),
                new Photo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new Photo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new Photo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
                new Photo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new Photo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new Photo("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new Photo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new Photo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new Photo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
                new Photo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new Photo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new Photo("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new Photo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new Photo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new Photo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
