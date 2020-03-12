package com.jeasoon.bubblekit.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.jeasoon.bubblekit.BubbleKit;

public class BubbleChatService extends Service {

    @SuppressLint("StaticFieldLeak")
    private static Service sInstance;

    public static Service getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BubbleKit.getInstance().init(this);
        sInstance = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BubbleKit.getInstance().destroy();
        sInstance = null;
    }
}
