package com.sweetcompany.sweetie.Registration;

/**
 * Created by lucas on 22/05/2017.
 */

public class RequestVM {
    String senderNumber, receiverNumber;

    RequestVM(){}
    RequestVM(String sender,String receiver){
        this.senderNumber = sender;
        this.receiverNumber = receiver;
    }

    public String getSenderNumber(){ return senderNumber; }

    public String getReceiverNumber(){ return receiverNumber; }
}
