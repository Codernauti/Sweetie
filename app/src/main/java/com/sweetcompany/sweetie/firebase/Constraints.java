package com.sweetcompany.sweetie.firebase;

/**
 * Created by Eduard on 09-Jul-17.
 */

interface Constraints {

    String USERS_NODE = "users";
        String COUPLE_INFO_NODE = "coupleInfo";
            String ACTIVE_COUPLE = "activeCouple";
            String ARCHIVED_COUPLES = "archivedCouples";
        String FUTURE_PARTNER = "futurePartner";

    String COUPLES_NODE = "couples";

        String ACTIVE = "active";

    String PAIRING_REQUESTS_NODE = "pairing-requests";

    String ACTIONS_NODE = "actions";

    String CHATS_NODE = "chats";

    String CHAT_MESSAGES_NODE = "chat-messages";
    String BREAK_TIME = "brokenTime";
}
