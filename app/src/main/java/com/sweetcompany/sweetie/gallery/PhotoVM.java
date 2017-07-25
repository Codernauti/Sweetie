package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.view.View;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 22/07/2017.
 */

public class PhotoVM extends MediaVM{

    private String mText;
    private Bitmap bitmap;

    public PhotoVM(boolean who, String date, String desc, boolean bookMarked, String key, Bitmap bit) {
        super(who, date, desc, bookMarked, key);
        this.bitmap =  bit;
        this.mText = desc;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    void configViewHolder(MediaViewHolder viewHolder) {
        // TODO: This downcast is secure?
        PhotoViewHolder view = (PhotoViewHolder) viewHolder;

        //view.setText(mText);
        view.setBitmap(bitmap);
    }

    @Override
    int getIdView() {
            return R.layout.gallery_thumbnail;
    }

    @Override
    PhotoViewHolder newViewHolder(View inflatedView) {
        return new PhotoViewHolder(inflatedView);
    }
}
