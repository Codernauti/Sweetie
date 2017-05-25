package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.Registration.PairingRequestVM;

/**
 * Created by lucas on 24/05/2017.
 */

public class PairingRequest {
    @Exclude
    private String key;
    private String senderNumber;
    private String receiverNumber;

    PairingRequest(){}

    public PairingRequest(String senderNumber,String receiverNumber) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
    }

    PairingRequest(PairingRequestVM pairingRequestVM){
        this.receiverNumber = pairingRequestVM.getReceiverNumber();
        this.senderNumber = pairingRequestVM.getSenderNumber();
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
