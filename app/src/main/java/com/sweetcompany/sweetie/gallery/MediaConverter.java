package com.sweetcompany.sweetie.gallery;

import com.sweetcompany.sweetie.model.MediaFB;

/**
 * Created by ghiro on 28/08/2017.
 */

public class MediaConverter {

    private static final String TAG = "MediaConverter";

    public static MediaVM createMediaVM(MediaFB media, String userMail) {

        boolean who = checkWho(media.getEmail(), userMail);

        MediaVM mediaVM = null;
        mediaVM = MediaConverter.createPhotoVM(media, who);

        return mediaVM;
    }

    private static boolean checkWho(String mediaMail, String userMail) {
        return mediaMail.equals(userMail)? MediaVM.THE_MAIN_USER : MediaVM.THE_PARTNER;
    }

    private static MediaVM createPhotoVM(MediaFB media, boolean who) {
        PhotoVM newPhotoVM = new PhotoVM(who, media.getDateTime(), media.getText(), media.getUriStorage(), media.getKey(), false);
        return newPhotoVM;
    }
}