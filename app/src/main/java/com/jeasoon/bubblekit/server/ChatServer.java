package com.jeasoon.bubblekit.server;

import android.app.Person;

public class ChatServer {

    private static class DummyChatServerHolder {
        private static final ChatServer INSTANCE = new ChatServer();
    }

    public static ChatServer getInstance() {
        return ChatServer.DummyChatServerHolder.INSTANCE;
    }

    public void start() {
        ChatDispatcher.getInstance().setOnChatMessageSentListener(new DummyChatInterceptor());
    }

    public void shutdown() {
        ChatDispatcher.getInstance().clear();
    }

    public ChatSession createSession(Person person) {
        return new ChatSession(ChatDispatcher.getInstance(), person);
    }

}
