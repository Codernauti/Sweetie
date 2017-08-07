package com.sweetcompany.sweetie.firebase;

/**
 * Created by Eduard on 09-Jul-17.
 */

interface Constraints {

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

    String CHATS = "chats";

    String CHAT_MESSAGES = "chat-messages";
        String BOOKMARK = "bookmarked";

    String GALLERIES = "galleries";

    String GALLERY_PHOTOS = "gallery-photos";

    String CALENDAR = "calendar";

    String TODOLIST = "todolists";

    String TODOLIST_CHECKENTRY = "todolist-checkentry";
        String CHECKED = "checked";
}
