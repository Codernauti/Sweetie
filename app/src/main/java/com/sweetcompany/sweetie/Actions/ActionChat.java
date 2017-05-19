package com.sweetcompany.sweetie.Actions;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionChat extends Action{

    public ActionChat() {}

    public ActionChat(String title,String description, String date) {
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);

        super.setType(CHAT);
    }
}
