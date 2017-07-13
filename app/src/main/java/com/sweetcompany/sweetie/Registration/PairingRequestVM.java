package com.sweetcompany.sweetie.Registration;

import com.google.firebase.database.Exclude;

/**
 * Created by lucas on 22/05/2017.
 */

@Deprecated
class PairingRequestVM {
    private String key;
    private String senderNumber;
    private String receiverNumber;

    PairingRequestVM(){}

    PairingRequestVM(String key,String sender, String receiver){
        this.key = key;
        this.senderNumber = sender;
        this.receiverNumber = receiver;
    }

    public String getSenderNumber(){ return senderNumber; }

    public void setSenderNumber(String senderNumber) {this.senderNumber = senderNumber;}

    public String getReceiverNumber(){ return receiverNumber; }

    public void setReceiverNumber(String receiverNumber) {this.receiverNumber = receiverNumber;}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
