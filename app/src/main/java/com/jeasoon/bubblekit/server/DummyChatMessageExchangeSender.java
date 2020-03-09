package com.jeasoon.bubblekit.server;

public class DummyChatMessageExchangeSender implements ChatMessageExchangeSender {

    @Override
    public void onSend(ChatDispatcher chatDispatcher, ChatSession chatSession, ChatMessage chatMessage, boolean isDummy, ChatMessageExchangeReceiver receiver) {
        receiver.onReceived(chatDispatcher, chatSession, chatMessage, isDummy);
    }

}
