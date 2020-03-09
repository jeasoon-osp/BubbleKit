package com.jeasoon.bubblekit.server;

import android.app.Person;

public class ChatMessage {

    private Person sender;
    private String message;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + sender +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
