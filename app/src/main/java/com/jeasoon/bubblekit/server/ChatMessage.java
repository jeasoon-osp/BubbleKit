package com.jeasoon.bubblekit.server;

import android.app.Person;

public class ChatMessage {

    private Person sender;
    private String message;

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + sender +
                ", message='" + message + '\'' +
                '}';
    }
}
