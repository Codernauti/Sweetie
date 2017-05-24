package com.sweetcompany.sweetie.Registration;

import com.google.firebase.database.Exclude;

/**
 * Created by lucas on 22/05/2017.
 */

public class PairingRequestVM {
    @Exclude
    String key;
    String senderNumber, receiverNumber;

    PairingRequestVM(){}
    PairingRequestVM(String sender, String receiver){
        this.senderNumber = sender;
        this.receiverNumber = receiver;
    }

    public String getSenderNumber(){ return senderNumber; }

    public String getReceiverNumber(){ return receiverNumber; }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
