package com.sweetcompany.sweetie.gallery;


import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ghiro on 22/07/2017.
 */

public class PhotoVM implements Serializable{
    private String name;
    private String timestamp;
    private Bitmap bitmap;
    private String text;
    private boolean bookmarked;

    public PhotoVM() {
    }

    public PhotoVM(String name, String timestamp, boolean bookmarked, Bitmap bitmap) {
        this.name = name;
        this.timestamp = timestamp;
        this.bookmarked = bookmarked;
        this.bitmap =  bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
