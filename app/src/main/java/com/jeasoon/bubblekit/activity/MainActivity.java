package com.jeasoon.bubblekit.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.util.ScreenUtil;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_SCREEN = "extra_screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_self, R.id.navigation_friend, R.id.navigation_group)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

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
        }
        return super.onOptionsItemSelected(item);
    }

}
