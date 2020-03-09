package com.jeasoon.bubblekit.server;

interface ChatMessageExchangeSender {
    void onSend(ChatDispatcher chatDispatcher, ChatSession chatSession, ChatMessage chatMessage, boolean isDummy, ChatMessageExchangeReceiver receiver);
}
