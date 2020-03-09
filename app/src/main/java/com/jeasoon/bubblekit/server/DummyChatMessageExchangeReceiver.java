package com.jeasoon.bubblekit.server;

import android.app.Person;

import com.jeasoon.bubblekit.data.PoetryManager;

import java.util.Set;

public class DummyChatMessageExchangeReceiver implements ChatMessageExchangeReceiver {

    private static final long DELAY_TIME_MIN = 5000;
    private static final long DELAY_TIME_STEP = 1000;

    @Override
    public void onReceived(ChatDispatcher chatDispatcher, ChatSession chatSession, ChatMessage chatMessage, boolean isDummy) {
        chatDispatcher.postReceiveMsg(chatSession, chatMessage, isDummy);
        if (isDummy) {
            return;
        }
        Set<Person> receiverPeople = chatSession.getAllReceiver();
        receiverPeople.remove(chatSession.getSelf());
        int times = 0;
        for (Person person : receiverPeople) {
            long delayTime = DELAY_TIME_MIN + DELAY_TIME_STEP * (times++);
            ChatMessage replyMsg = new ChatMessage();
            replyMsg.setSender(person);
            replyMsg.setTimestamp(System.currentTimeMillis() + delayTime);
            replyMsg.setMessage(PoetryManager.getInstance().next().toParagraphsString());
            chatSession.postSendMsg(replyMsg, delayTime, true);
        }
    }
}
