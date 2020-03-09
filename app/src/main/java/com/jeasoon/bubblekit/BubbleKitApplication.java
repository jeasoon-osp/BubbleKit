package com.jeasoon.bubblekit;

import android.app.Application;

import com.jeasoon.bubblekit.data.PersonManager;
import com.jeasoon.bubblekit.data.PoetryManager;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.server.ChatServer;

public class BubbleKitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PoetryManager.getInstance().init(this);
        PersonManager.getInstance().init(this);
        NotificationEmitter.getInstance().init(this);
        ChatServer.getInstance().start();
    }
}
