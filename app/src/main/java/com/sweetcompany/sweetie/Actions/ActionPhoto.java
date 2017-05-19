package com.sweetcompany.sweetie.Actions;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionPhoto extends Action{

    public ActionPhoto() {}

    public ActionPhoto(String title,String description, String date) {
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);

        super.setType(PHOTO);
    }
}