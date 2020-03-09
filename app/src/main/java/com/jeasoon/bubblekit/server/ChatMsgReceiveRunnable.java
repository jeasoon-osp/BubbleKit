package com.jeasoon.bubblekit.server;

import android.app.Person;

public interface ChatMsgReceiveRunnable {

    void onReceiveMessage(Person receiver, ChatMessage chatMessage, boolean isIncoming);

}
