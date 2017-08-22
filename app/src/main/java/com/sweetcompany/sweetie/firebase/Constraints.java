package com.sweetcompany.sweetie.firebase;

/**
 * Created by Eduard on 09-Jul-17.
 */

interface Constraints {

    int ACTION_CHAT_TYPE = 0;
    int ACTION_GALLERY_TYPE = 1;
    int ACTION_TODO_TYPE = 2;
    int ACTION_GEOGIFT_TYPE = 3;


    String USERS = "users";
        String COUPLE_INFO = "coupleInfo";
            String ACTIVE_COUPLE = "activeCouple";
            String ARCHIVED_COUPLES = "archivedCouples";
        String FUTURE_PARTNER = "futurePartner";

    String COUPLES = "couples";
        String ACTIVE = "active";
        String BREAK_TIME = "brokenTime";

    String PAIRING_REQUESTS = "pairing-requests";

    String ACTIONS = "actions";
        String DESCRIPTION = "description";
        String DATE_TIME = "dataTime";

    String ACTIONS_DIARY = "actionsDiary";

    String CHATS = "chats";
        String CHAT_MESSAGES = "chat-messages";

    String BOOKMARK = "bookmarked";

    String GALLERIES = "galleries";
        String GALLERY_PHOTOS = "gallery-photos";

    String CALENDAR = "calendar";

    String TODOLIST = "todolists";
        String TODOLIST_CHECKENTRY = "todolist-checkentry";
        String CHECKED = "checked";

    String GEOGIFT = "geogifts";
        String GEOGIFT_ITEMS = "geogift-items";

    String GALLERY_PHOTOS_DIREECTORY = "gallery_photos/";
    String GALLERY_GEOGIFTS = "gallery_geogifts/";
}
