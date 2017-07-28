package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import com.sweetcompany.sweetie.R;

import java.io.Serializable;

/**
 * Created by ghiro on 22/07/2017.
 */

public class PhotoVM extends MediaVM implements Serializable {

    private String mText;
    private String mUri;

    public PhotoVM(boolean who, String date, String desc, boolean bookMarked, String key, String uri) {
        super(who, date, desc, bookMarked, key);
        this.mUri =  uri;
        this.mText = desc;
    }

    public String getUri(){
        return mUri;
    }

    @Override
    void configViewHolder(MediaViewHolder viewHolder) {
        // TODO: This downcast is secure?
        PhotoViewHolder view = (PhotoViewHolder) viewHolder;

        view.setImage(mUri);
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
