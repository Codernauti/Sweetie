package com.sweetcompany.sweetie.firebase;

/**
 * Created by Eduard on 09-Jul-17.
 */

interface Constraints {

    String USERS = "users";
        String COUPLE_INFO = "coupleInfo";
            String PARTNER_USERNAME = "partnerUsername";
            String PARTNER_IMAGE_URI = "partnerImageUri";
            String ACTIVE_COUPLE = "activeCouple";
    String ARCHIVED_COUPLES = "archivedCouples";


        String FUTURE_PARTNER = "futurePartner";
    String MSG_NOTIFICATION_ROOMS = "msg-notification-rooms";

    interface Users {
        String IMAGE_URL = "imageUrl";
    }

    String COUPLES = "couples";
        String ACTIVE = "active";
        String BREAK_TIME = "brokenTime";
        String IMAGE_LOCAL_URI = "imageLocalUri";
        String IMAGE_STORAGE_URI = "imageStorageUri";
        String ANNIVERSARY = "anniversary";

    String PAIRING_REQUESTS = "pairing-requests";

    String ACTIONS = "actions";
    interface Actions {
        String TITLE = "title";
        String IMAGE_URL = "imageUrl";
        String DESCRIPTION = "description";
        String DATE_TIME = "dataTime";
        String NOTIFICATION_COUNTER = "notificationCounters";
        String COUNTER = "counter";
    }

    String ACTIONS_DIARY = "actionsDiary";

    interface ChildAction {
        String TITLE = "title";
        String URI_COVER = "uriCover";
        String UPLOADING_IMG = "uploadingImg";
        String PROGRESS = "progress";
    }

    String CHATS = "chats";
    interface Chats extends ChildAction {}

    String CHAT_MESSAGES = "chat-messages";
    interface ChatMessages {
        String URI_STORAGE = "uriStorage";
        String BOOKMARK = "bookmarked";
        String UPLOADING = "uploading";
    }


    String GALLERIES = "galleries";
    interface Galleries extends ChildAction{
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String IMG_SET_BY_USER = "imageSetByUser";
    }

    String GALLERY_PHOTOS = "gallery-photos";
    interface GalleryPhotos {
        String URI_STORAGE = "uriStorage";
        String PROGRESS = "progress";
        String UPLOADING = "uploading";
    }

    String CALENDAR = "calendar";

    String TODOLIST = "todolists";

        String TODOLIST_CHECKENTRY = "todolist-checkentry";
        String CHECKED = "checked";
        String TEXT = "text";


    String GEOGIFTS = "geogifts";
    interface Geogifts {
        String IS_TRIGGERED = "isTriggered";
        String DATE_TIME_VISITED = "datetimeVisited";
    }

    // STORAGE CONSTRAINT

    String COUPLES_DETAILS = "couples_details";

    String GALLERY_PHOTOS_DIRECTORY = "gallery_photos";
    String GALLERY_GEOGIFTS = "gallery_geogifts";

    String ACTIONS_IMAGES = "action_images";

}

