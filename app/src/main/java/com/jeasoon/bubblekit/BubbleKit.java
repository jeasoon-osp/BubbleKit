package com.jeasoon.bubblekit;

import android.content.Context;

import com.jeasoon.bubblekit.data.PersonManager;
import com.jeasoon.bubblekit.data.PoetryManager;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.server.ChatServer;
import com.jeasoon.bubblekit.ui.viewmodel.ChatViewModelProvider;

public class BubbleKit {

    private BubbleKit() {
    }

    private static class BubbleKitHolder {
        private static final BubbleKit INSTANCE = new BubbleKit();
    }

    public static BubbleKit getInstance() {
        return BubbleKitHolder.INSTANCE;
    }

    private int mInitCount;

    public void init(Context context) {
        if (mInitCount++ > 0) {
            return;
        }
        doInit(context);
    }

    public void destroy() {
        if (--mInitCount > 0) {
            return;
        }
        doDestroy();
    }

    private void doInit(Context context) {
        ChatServer.getInstance().start();
        PoetryManager.getInstance().init(context);
        PersonManager.getInstance().init(context);
        ChatViewModelProvider.getInstance().init();
        NotificationEmitter.getInstance().init(context);
    }

    private void doDestroy() {
        ChatServer.getInstance().clear();
        PoetryManager.getInstance().clear();
        PersonManager.getInstance().clear();
        ChatViewModelProvider.getInstance().clear();
        NotificationEmitter.getInstance().clear();
    }

}
