package com.sweetcompany.sweetie.Firebase;

/**
 * Created by Eduard on 21-May-17.
 */

public class Message {
    //TODO: add user fields
    private String text;

    // For firebase serialization
    public Message() {}

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
