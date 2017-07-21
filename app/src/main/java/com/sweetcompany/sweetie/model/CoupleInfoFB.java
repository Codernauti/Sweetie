package com.sweetcompany.sweetie.model;

import java.util.Map;

/**
 * Created by Eduard on 11-Jul-17.
 */

public class CoupleInfoFB {

    private String activeCouple;
    private Map<String, Boolean> archivedCouples;

    public CoupleInfoFB() {}

    public String getActiveCouple() {
        return activeCouple;
    }

    public void setActiveCouple(String activeCouple) {
        this.activeCouple = activeCouple;
    }

    public Map<String, Boolean> getArchivedCouples() {
        return archivedCouples;
    }

    public void setArchivedCouples(Map<String, Boolean> archivedCouples) {
        this.archivedCouples = archivedCouples;
    }
}
