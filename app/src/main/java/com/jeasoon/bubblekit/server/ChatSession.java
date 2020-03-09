package com.jeasoon.bubblekit.server;

import android.app.Person;

import java.util.HashSet;
import java.util.Set;

public class ChatSession {

    private Set<Person> mPersonSet = new HashSet<>();
    private Person mHostPerson;
    private ChatDispatcher mChatDispatcher;

    ChatSession(ChatDispatcher chatDispatcher, Person hostPerson) {
        mHostPerson = hostPerson;
        mChatDispatcher = chatDispatcher;
    }

    public void destroy() {
        mPersonSet.clear();
        mChatDispatcher.reportSessionDestroyed(this);
        mHostPerson = null;
        mChatDispatcher = null;

    }

    public void addPerson(Person person) {
        assert person != null;
        mPersonSet.add(person);
    }

    public void removePerson(Person person) {
        assert person != null;
        mPersonSet.remove(person);
    }

    public void postSendMsg(ChatMessage chatMessage) {
        postSendMsg(chatMessage, 0);
    }

    public void postSendMsg(ChatMessage chatMessage, long delay) {
        postSendMsg(chatMessage, delay, false);
    }

    void postSendMsg(ChatMessage chatMessage, long delay, boolean isDummy) {
        mChatDispatcher.postSendMsg(this, chatMessage, delay, isDummy);
    }

    public void postReceiveMsg(ChatMsgReceiveRunnable runnable) {
        mChatDispatcher.postReceiveMsg(this, runnable);
    }

    public Person getSelf() {
        return mHostPerson;
    }

    public Set<Person> getAllReceiver() {
        Set<Person> people = new HashSet<>(mPersonSet);
        people.add(mHostPerson);
        return people;
    }

    void dispatchMessage(ChatMsgReceiveRunnable runnable, ChatMessage chatMessage) {
        runnable.onReceiveMessage(mHostPerson, chatMessage, mHostPerson != chatMessage.getSender());
    }

}
