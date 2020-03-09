package com.jeasoon.bubblekit.server;

import android.app.Person;

import com.jeasoon.bubblekit.data.PoetryManager;

import java.util.Set;

public class DummyChatInterceptor implements ChatDispatcher.OnChatMessageSentListener {

    private static final long DELAY_TIME = 5000;

    @Override
    public void onChatMessageSent(ChatSession chatSession, ChatMessage chatMessage, boolean isDummy) {
        if (isDummy) {
            return;
        }
        Set<Person> receiver = chatSession.getAllReceiver();
        receiver.remove(chatMessage.getSender());
        for (Person person : receiver) {
            ChatMessage replyMsg = new ChatMessage();
            replyMsg.setSender(person);
            replyMsg.setMessage(PoetryManager.getInstance().next().toParagraphsString());
            chatSession.postSendMsg(replyMsg, DELAY_TIME, true);
        }
    }
}
