package com.sweetcompany.sweetie.Registration;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.Firebase.Couple;

/**
 * Created by lucas on 30/05/2017.
 */

public class CoupleVM {
    String key;
    String idFirst;
    String idSecond;


    public CoupleVM(){}

    public CoupleVM(String key, String idFirst, String idSecond) {
        this.key = key;
        this.idFirst = idFirst;
        this.idSecond = idSecond;
    }

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
