package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.Exclude;

/**
 * Created by lucas on 30/05/2017.
 */

public class Couple {
    @Exclude
    String key;
    String idFirst;
    String idSecond;

    public Couple() {}

    public Couple(String idFirst, String idSecond) {
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
