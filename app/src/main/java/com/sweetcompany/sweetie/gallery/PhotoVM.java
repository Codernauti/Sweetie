package com.sweetcompany.sweetie.gallery;


import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ghiro on 22/07/2017.
 */

public class PhotoVM extends MediaVM{

    private Bitmap bitmap;

    public PhotoVM(boolean who, String date, String desc, boolean bookMarked, String key, Bitmap bit) {
        super(who, date, desc, bookMarked, key);
        this.bitmap =  bit;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
}
