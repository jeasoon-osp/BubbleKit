package com.jeasoon.bubblekit.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.constant.ChatConstant;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.util.ScreenUtil;

public abstract class BaseChatActivity extends AppCompatActivity implements ChatConstant {

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_SCREEN, (getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(EXTRA_SCREEN)) {
            ScreenUtil.setScreenFull(this);
        } else {
            ScreenUtil.setScreenNormal(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bubble_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            menu.findItem(R.id.menu_notification_bubble_enable).setChecked(NotificationEmitter.getInstance().isBubbleEnabled());
            menu.findItem(R.id.menu_notification_bubble_service).setChecked(NotificationEmitter.getInstance().isServiceEnabled());
            menu.findItem(R.id.menu_notification_bubble_reply).setChecked(NotificationEmitter.getInstance().isReplyEnabled());
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification_orientation_portrait:
                ScreenUtil.setOrientationPortrait(this);
                return true;
            case R.id.menu_notification_orientation_landscape:
                ScreenUtil.setOrientationLandscape(this);
                return true;
            case R.id.menu_notification_screen_normal:
                ScreenUtil.setScreenNormal(this);
                return true;
            case R.id.menu_notification_screen_full:
                ScreenUtil.setScreenFull(this);
                return true;
            case R.id.menu_notification_bubble_enable:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    NotificationEmitter.getInstance().enableBubble();
                } else {
                    NotificationEmitter.getInstance().disableBubble();
                }
                return true;
            case R.id.menu_notification_bubble_service:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    NotificationEmitter.getInstance().enableService();
                } else {
                    NotificationEmitter.getInstance().disableService();
                }
                return true;
            case R.id.menu_notification_bubble_reply:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    NotificationEmitter.getInstance().enableReply();
                } else {
                    NotificationEmitter.getInstance().disableReply();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
