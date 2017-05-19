package com.sweetcompany.sweetie.Firebase;

import java.io.File;
import java.sql.Date;

/**
 * Created by ghiro on 17/05/2017.
 */

class SweetUser {

    private String mUserID;
    private String mUsername;
    private String mAvatarUrl;
    private String mGender;
    private String mPhone;
    private String mEmail;

    public SweetUser(String userId, String phone, String gender) {
        mUserID = userId;
        //mEmail = email;
        mPhone = phone;
        mGender = gender;
    }

    //TODO
    /*** SETTER ***/

    /*** GETTER ***/ //TODO crate assert null function

    public String getUserID(){
        return mUserID;
    }

    public  String getUsername(){
        return mUsername;
    }

    //TODO ...
}
