package com.jeasoon.bubblekit.notification;

import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;

import com.jeasoon.bubblekit.activity.BubbleChatActivity;
import com.jeasoon.bubblekit.constant.ChatConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NotificationSession implements ChatConstant {

    private final int mNotificationId = UUID.randomUUID().hashCode() & 0xFFFF;
    private final int mNotificationRequestCode = UUID.randomUUID().hashCode() & 0xFFFF;
    private Person mHost;
    private String mModelName;
    private LinkedList<NotificationMessage> mMsgList = new LinkedList<>();

    NotificationSession(Person person) {
        mHost = person;
    }

    public void enqueueNotificationMessage(NotificationMessage msg) {
        mMsgList.add(msg);
    }

    int getNotificationId() {
        return mNotificationId;
    }

    Person getHostPerson() {
        return mHost;
    }

    List<NotificationMessage> getMsgList() {
        return new LinkedList<>(mMsgList);
    }

    NotificationMessage getLastMsg() {
        return mMsgList.isEmpty() ? null : mMsgList.getLast();
    }

    String getRemoteInputResultKey() {
        return EXTRA_CHAT_REMOTE_INPUT_RESULT_KEY;
    }

    PendingIntent getActivityPendingIntent(Context ctx) {
        Intent intent = new Intent(ctx, BubbleChatActivity.class);
        setUpExtraData(intent);
        return PendingIntent.getActivity(ctx, mNotificationRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent getBroadcastPendingIntent(Context ctx) {
        Intent intent = new Intent(ACTION_CHAT_REMOTE_INPUT_RECEIVER);
        setUpExtraData(intent);
        return PendingIntent.getBroadcast(ctx, mNotificationRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setUpExtraData(Intent data) {
        data.putExtra(EXTRA_CHAT_VIEW_MODEL, mModelName);
    }

    public void setModelName(String modelName) {
        mModelName = modelName;
    }

    public void sendNotification() {
        NotificationEmitter.getInstance().sendNotification(this);
    }

    public void cancelNotification() {
        NotificationEmitter.getInstance().cancelNotification(this);
    }

    public void destroy() {
        mMsgList.clear();
        mHost = null;
        mMsgList = null;
    }

}
