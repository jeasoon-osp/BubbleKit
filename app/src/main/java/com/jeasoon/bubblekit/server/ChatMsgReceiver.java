package com.jeasoon.bubblekit.server;

import android.app.Person;

public interface ChatMsgReceiver {

    void onReceiveMessage(Person receiver, ChatMessage chatMessage, boolean isIncoming);

}
