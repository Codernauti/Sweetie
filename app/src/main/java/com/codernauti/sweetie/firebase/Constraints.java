package com.codernauti.sweetie.firebase;


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
        String UPLOADING_IMG = "uploadingImg";
    }

    String COUPLES = "couples";
    interface Couples {
        String ACTIVE = "active";
        String BREAK_TIME = "brokenTime";
        String IMAGE_LOCAL_URI = "imageLocalUri";
        String IMAGE_STORAGE_URI = "imageStorageUri";
        String ANNIVERSARY = "anniversary";
        String UPLOADING_IMG = "uploadingImg";
        String PROGRESS = "progress";
    }

    String PAIRING_REQUESTS = "pairing-requests";

    String ACTIONS = "actions";
    interface Actions {
        String TITLE = "title";
        String IMAGE_URL = "imageUrl";
        String DESCRIPTION = "description";
        String LAST_UPDATED_DATE = "lastUpdateDate";
        String NOTIFICATION_COUNTERS = "notificationCounters";
        String COUNTER = "counter";
        String UPDATED_ELEMENTS = "updatedElements";
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
        String ADDRESS = "address";
        String IMG_SET_BY_USER = "imageSetByUser";
    }

    String GALLERY_PHOTOS = "gallery-photos";
    interface GalleryPhotos {
        String DATE_TIME = "dateTime";
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

