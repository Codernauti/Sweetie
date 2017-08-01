package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.Utility;

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

        String uriToLoad;
        // is image uploaded by me?
        //verify if is in Local memory and has valid path
        if(super.isTheMainUser()) {
            if(Utility.isImageAvaibleInLocal(super.getUriLocal())) uriToLoad = super.getUriLocal();
            else uriToLoad = super.getUriStorage();
        }
        else // uploaded by partner, take uri storage
        {
            uriToLoad = super.getUriStorage();
        }

        view.setImage(uriToLoad);
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
