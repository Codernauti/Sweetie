package com.sweetcompany.sweetie.gallery;

import com.sweetcompany.sweetie.model.MediaFB;

public class MediaConverter {

    private static final String TAG = "MediaConverter";

    public static MediaVM createMediaVM(MediaFB media, String userUid) {

        boolean who = checkWho(media.getUserUid(), userUid);

        MediaVM mediaVM = null;
        mediaVM = MediaConverter.createPhotoVM(media, who);

        return mediaVM;
    }

    private static boolean checkWho(String mediaUserUid, String userUid) {
        return mediaUserUid.equals(userUid)? MediaVM.THE_MAIN_USER : MediaVM.THE_PARTNER;
    }

    private static MediaVM createPhotoVM(MediaFB media, boolean who) {
        PhotoVM newPhotoVM = new PhotoVM(who, media.getDateTime(), media.getText(), media.getUriStorage(), media.getKey(), false);
        return newPhotoVM;
    }
}