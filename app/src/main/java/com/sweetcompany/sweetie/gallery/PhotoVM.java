package com.sweetcompany.sweetie.gallery;

import android.view.View;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.Utility;

import java.io.Serializable;

/**
 * Created by ghiro on 22/07/2017.
 */

public class PhotoVM extends MediaVM implements Serializable {

    PhotoVM(boolean who, String date, String desc, String uriS, int perc, String key) {
        super(who, date, desc, uriS, perc, key);
    }

    @Override
    void configViewHolder(MediaViewHolder viewHolder) {
        PhotoViewHolder view = (PhotoViewHolder) viewHolder;

        view.setImage(super.getUriStorage());
        view.setPercentUploading(super.getPercent());
    }


    @Override
    public void configViewHolder(GalleryViewHolder viewHolder) {

    }

    @Override
    public int getIdView() {
            return R.layout.gallery_thumbnail;
    }

    @Override
    PhotoViewHolder newViewHolder(View inflatedView) {
        return new PhotoViewHolder(inflatedView);
    }
}
