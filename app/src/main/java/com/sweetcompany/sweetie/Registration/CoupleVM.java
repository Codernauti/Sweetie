package com.sweetcompany.sweetie.Registration;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.Firebase.Couple;

/**
 * Created by lucas on 30/05/2017.
 */

public class CoupleVM {
    private String key;
    private String idFirst;
    private String idSecond;

    public String getIdFirst() {
        return idFirst;
    }

    public void setIdFirst(String idFirst) {
        this.idFirst = idFirst;
    }

    public String getIdSecond() {
        return idSecond;
    }

    public void setIdSecond(String idSecond) {
        this.idSecond = idSecond;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
