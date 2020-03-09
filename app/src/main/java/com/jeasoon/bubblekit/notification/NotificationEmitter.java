package com.jeasoon.bubblekit.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;

import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.activity.BubbleChatActivity;
import com.jeasoon.bubblekit.data.PersonFactory;

import java.util.List;
import java.util.Objects;

public class NotificationEmitter {

    private static final String CHANNEL_ID_BUBBLE_ON = "channel_id_bubble_on";
    private static final String CHANNEL_ID_BUBBLE_OFF = "channel_id_bubble_off";

    private Context mCtx;
    private NotificationManager mNM;
    private String mBubbleChannelId;
    private boolean isUsingService;

    private NotificationEmitter() {
    }

    private static class NotificationEmitterHolder {
        @SuppressLint("StaticFieldLeak")
        private static final NotificationEmitter INSTANCE = new NotificationEmitter();
    }

    public static NotificationEmitter getInstance() {
        return NotificationEmitterHolder.INSTANCE;
    }

    public void init(Context context) {
        mCtx = context.getApplicationContext();
        mNM = context.getSystemService(NotificationManager.class);
        initChannels();
        enableBubble();
        disableService();
    }

    private void initChannels() {
        NotificationChannel bubbleChannelOff = new NotificationChannel(CHANNEL_ID_BUBBLE_OFF, mCtx.getString(R.string.channel_name_bubble_off), NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel bubbleChannelOn = new NotificationChannel(CHANNEL_ID_BUBBLE_ON, mCtx.getString(R.string.channel_name_bubble_on), NotificationManager.IMPORTANCE_HIGH);
        bubbleChannelOn.setAllowBubbles(true);
        bubbleChannelOff.setAllowBubbles(false);
        mNM.createNotificationChannel(bubbleChannelOn);
        mNM.createNotificationChannel(bubbleChannelOff);
    }

    public void enableBubble() {
        mBubbleChannelId = CHANNEL_ID_BUBBLE_ON;
    }

    public void disableBubble() {
        mBubbleChannelId = CHANNEL_ID_BUBBLE_OFF;
    }

    public void enableService() {
        isUsingService = true;
    }

    public void disableService() {
        isUsingService = false;
    }

    public void buildNotification(Person host, List<NotificationMessage> msgHost) {
        // BubbleMetadata 构建
        Intent intent = new Intent(mCtx, BubbleChatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mCtx, 10086, intent, 0);
        Notification.BubbleMetadata.Builder bubbleBuilder = new Notification.BubbleMetadata.Builder();
        bubbleBuilder.setIntent(pIntent)
                .setIcon(Objects.requireNonNull(host.getIcon()))
                .setAutoExpandBubble(true)
                .setSuppressNotification(true)
                .setDesiredHeight(1000);

        Notification.MessagingStyle msgStyle = new Notification.MessagingStyle(host);
        for (NotificationMessage nMsg : msgHost) {
            Notification.MessagingStyle.Message msg = new Notification.MessagingStyle.Message(nMsg.getMessage(), nMsg.getTimestamp(), nMsg.getPerson());
            msgStyle.addMessage(msg);
        }
        StringBuilder sbTitle = new StringBuilder();
        boolean isFirst = true;
        for (NotificationMessage msg : msgHost) {
            if (isFirst) {
                isFirst = false;
            } else {
                sbTitle.append(", ");
            }
            sbTitle.append(msg.getPerson().getName());
        }
        msgStyle.setConversationTitle(sbTitle.toString());

        Notification.Builder builder = new Notification.Builder(mCtx, mBubbleChannelId);
        builder.setBubbleMetadata(bubbleBuilder.build())
                .setSmallIcon(msgHost.size() != 1 ? host.getIcon() : msgHost.get(0).getPerson().getIcon())
                .setContentIntent(pIntent)
                .setStyle(msgStyle);

        for (Person person : PersonFactory.getInstance().getGroupPerson()) {
            builder.addPerson(person);
        }

        mNM.notify(10010, builder.build());
    }


}
