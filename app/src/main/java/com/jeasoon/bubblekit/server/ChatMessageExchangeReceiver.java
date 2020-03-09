package com.jeasoon.bubblekit.server;

interface ChatMessageExchangeReceiver {
    void onReceived(ChatDispatcher chatDispatcher, ChatSession chatSession, ChatMessage chatMessage, boolean isDummy);
}
