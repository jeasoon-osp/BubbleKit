package com.jeasoon.bubblekit.notification;

public class NotificationTimer {

    private static class NotificationTimerHolder {
        private static final NotificationTimer INSTANCE = new NotificationTimer();
    }

    public static NotificationTimer getInstance() {
        return NotificationTimer.NotificationTimerHolder.INSTANCE;
    }

    public long now() {
        return System.currentTimeMillis();
    }

    public long delay(long delay) {
        return System.currentTimeMillis() + delay;
    }

}
