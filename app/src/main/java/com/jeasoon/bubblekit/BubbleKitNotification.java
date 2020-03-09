package com.jeasoon.bubblekit;

import android.app.Application;

import com.jeasoon.bubblekit.data.PersonFactory;
import com.jeasoon.bubblekit.data.PoetryManager;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.server.ChatServer;

public class BubbleKitNotification extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PoetryManager.getInstance().init(this);
        PersonFactory.getInstance().init(this);
        NotificationEmitter.getInstance().init(this);
        ChatServer.getInstance().start();
    }
}
