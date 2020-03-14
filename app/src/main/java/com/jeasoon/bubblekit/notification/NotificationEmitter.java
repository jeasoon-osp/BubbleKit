package com.jeasoon.bubblekit.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Person;
import android.app.RemoteInput;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;

import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.broadcast.BubbleChatBroadcastReceiver;
import com.jeasoon.bubblekit.constant.ChatConstant;
import com.jeasoon.bubblekit.service.BubbleChatService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NotificationEmitter {

    private static final String CHANNEL_ID_BUBBLE_ON = "channel_id_bubble_on";
    private static final String CHANNEL_ID_BUBBLE_OFF = "channel_id_bubble_off";

    private Context mCtx;
    private NotificationManager mNM;
    private String mBubbleChannelId;
    private boolean isUsingService;
    private boolean isReplyEnabled;
    private boolean isAutoExpandEnabled;
    private BroadcastReceiver mReceiver;
    private Set<Integer> mSentIdSet;

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
        mSentIdSet = new HashSet<>();
        initChannels();
        enableBubble();
        disableService();
        enableReply();
        initBroadcast();
    }

    public void clear() {
        clearBroadcast();
        mCtx = null;
        mNM = null;
        mBubbleChannelId = null;
        mSentIdSet = null;
    }

    private void initBroadcast() {
        if (mReceiver != null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatConstant.ACTION_CHAT_REMOTE_INPUT_RECEIVER);
        mReceiver = new BubbleChatBroadcastReceiver();
        mCtx.registerReceiver(mReceiver, filter);
    }

    private void clearBroadcast() {
        if (mReceiver == null) {
            return;
        }
        mCtx.unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    private void initChannels() {
        NotificationChannel bubbleChannelOff = new NotificationChannel(CHANNEL_ID_BUBBLE_OFF, mCtx.getString(R.string.channel_name_bubble_off), NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel bubbleChannelOn = new NotificationChannel(CHANNEL_ID_BUBBLE_ON, mCtx.getString(R.string.channel_name_bubble_on), NotificationManager.IMPORTANCE_HIGH);
        bubbleChannelOn.setAllowBubbles(true);
        bubbleChannelOff.setAllowBubbles(false);
        mNM.createNotificationChannel(bubbleChannelOn);
        mNM.createNotificationChannel(bubbleChannelOff);
    }

    public NotificationSession createNotificationSession(Person person) {
        return new NotificationSession(person);
    }

    public void enableBubble() {
        mBubbleChannelId = CHANNEL_ID_BUBBLE_ON;
    }

    public void disableBubble() {
        mBubbleChannelId = CHANNEL_ID_BUBBLE_OFF;
        for (int id : new HashSet<>(mSentIdSet)) {
            cancelNotification(id);
        }
        mSentIdSet.clear();
    }

    public boolean isBubbleEnabled() {
        return CHANNEL_ID_BUBBLE_ON.equals(mBubbleChannelId);
    }

    public void enableService() {
        isUsingService = true;
        mCtx.startService(new Intent(mCtx, BubbleChatService.class));
    }

    public void disableService() {
        isUsingService = false;
        Service notificationService = BubbleChatService.getInstance();
        if (notificationService != null) {
            notificationService.stopForeground(true);
            notificationService.stopSelf();
        }
    }

    public boolean isServiceEnabled() {
        return isUsingService;
    }

    public void enableReply() {
        isReplyEnabled = true;
    }

    public void disableReply() {
        isReplyEnabled = false;
    }

    public boolean isReplyEnabled() {
        return isReplyEnabled;
    }

    public void enableAutoExpand() {
        isAutoExpandEnabled = true;
    }

    public void disableAutoExpand() {
        isAutoExpandEnabled = false;
    }

    public boolean isAutoExpandEnabled() {
        return isAutoExpandEnabled;
    }

    void cancelNotification(NotificationSession session) {
        cancelNotification(session.getNotificationId());
    }

    private void cancelNotification(int id) {
        mNM.cancel(id);
        mSentIdSet.remove(id);
    }

    void sendNotification(NotificationSession session) {
        sendNotification(createNotification(session), session);
    }

    private Notification.Builder createNotification(NotificationSession session) {
        Notification.Builder builder = createNotificationBuilder(session);
        buildNotificationMessagingStyle(builder, session);
        buildNotificationPeople(builder, session);
        buildNotificationTitle(builder, session);
        buildNotificationBubble(builder, session);
        buildNotificationReplyAction(builder, session);
        return builder;
    }

    private void sendNotification(Notification.Builder builder, NotificationSession session) {
        if (isServiceEnabled() && BubbleChatService.getInstance() != null) {
            BubbleChatService.getInstance().startForeground(session.getNotificationId(), builder.build());
        } else {
            mNM.notify(session.getNotificationId(), builder.build());
        }
        mSentIdSet.add(session.getNotificationId());
    }

    private Notification.Builder createNotificationBuilder(NotificationSession session) {
        // 创建 NotificationBuilder
        List<NotificationMessage> msgList = session.getMsgList();
        Notification.Builder builder = new Notification.Builder(mCtx, mBubbleChannelId);
        builder.setSmallIcon(msgList.size() != 1 ? Icon.createWithResource(mCtx, R.drawable.ic_launcher) : msgList.get(0).getPerson().getIcon())
                .setContentIntent(session.getActivityPendingIntent(mCtx));
        return builder;
    }

    private void buildNotificationMessagingStyle(Notification.Builder builder, NotificationSession session) {
        // 构建 MessagingStyle
        Person msgHost = session.getHostPerson();
        List<NotificationMessage> msgList = session.getMsgList();
        Notification.MessagingStyle msgStyle = new Notification.MessagingStyle(msgHost);
        for (NotificationMessage nMsg : msgList) {
            Notification.MessagingStyle.Message msg = new Notification.MessagingStyle.Message(nMsg.getMessage(), nMsg.getTimestamp(), nMsg.getPerson());
            msgStyle.addMessage(msg);
        }
        builder.setStyle(msgStyle);
    }

    private void buildNotificationPeople(Notification.Builder builder, NotificationSession session) {
        // 构建 Person
        List<NotificationMessage> msgList = session.getMsgList();
        Set<Person> personSet = new HashSet<>();
        for (NotificationMessage msg : msgList) {
            personSet.add(msg.getPerson());
        }
        for (Person person : personSet) {
            builder.addPerson(person);
        }
    }

    private void buildNotificationTitle(Notification.Builder builder, NotificationSession session) {
        // 设置标题
        Notification.Style style = builder.getStyle();
        if (!(style instanceof Notification.MessagingStyle)) {
            return;
        }
        Notification.MessagingStyle msgStyle = (Notification.MessagingStyle) style;
        List<NotificationMessage> msgList = session.getMsgList();
        Set<CharSequence> nameSet = new HashSet<>();
        for (NotificationMessage msg : msgList) {
            nameSet.add(msg.getPerson().getName());
        }
        StringBuilder sbTitle = new StringBuilder();
        boolean isFirst = true;
        for (CharSequence personName : nameSet) {
            if (isFirst) {
                isFirst = false;
            } else {
                sbTitle.append(", ");
            }
            sbTitle.append(personName);
        }
        msgStyle.setConversationTitle(sbTitle.toString());
    }

    private void buildNotificationBubble(Notification.Builder builder, NotificationSession session) {
        if (!isBubbleEnabled()) {
            return;
        }
        // BubbleMetadata 构建
        NotificationMessage lastMsg = session.getLastMsg();
        Person msgHost = session.getHostPerson();
        Notification.BubbleMetadata.Builder bubbleBuilder = new Notification.BubbleMetadata.Builder();
        bubbleBuilder.setIntent(session.getActivityPendingIntent(mCtx))
                .setIcon(Objects.requireNonNull(msgHost.getIcon()))
                .setSuppressNotification(lastMsg == null || !lastMsg.isIncoming())
                .setAutoExpandBubble(isAutoExpandEnabled() && lastMsg != null && lastMsg.isIncoming())
                .setDesiredHeight(600);
        builder.setBubbleMetadata(bubbleBuilder.build());
    }

    private void buildNotificationReplyAction(Notification.Builder builder, NotificationSession session) {
        if (!isReplyEnabled()) {
            return;
        }
        // 构建回复
        RemoteInput remoteInput = new RemoteInput.Builder(session.getRemoteInputResultKey())
                .setAllowFreeFormInput(true)
                .build();
        Notification.Action remoteInputAction = new Notification.Action.Builder(
                Icon.createWithResource(mCtx, R.drawable.ic_send),
                mCtx.getString(R.string.bubble_remote_input_reply),
                session.getBroadcastPendingIntent(mCtx))
                .addRemoteInput(remoteInput)
                .build();
        builder.addAction(remoteInputAction);
    }

}
