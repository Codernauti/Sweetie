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

    public PhotoVM(boolean who, String date, String desc, String key, String uriL, String uriS, int perc) {
        super(who, date, desc, key, uriL, uriS, perc);
    }

    @Override
    void configViewHolder(MediaViewHolder viewHolder) {
        // TODO: This downcast is secure?
        PhotoViewHolder view = (PhotoViewHolder) viewHolder;

        /*if(super.getUriLocal().equals(""))
        {
            view.setImage(super.getUriStorage());
        }else
        {
            view.setImage(super.getUriLocal());
        }*/

        view.setImage(super.getUriStorage());
        view.setPercentUploading(super.getPercent());
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
