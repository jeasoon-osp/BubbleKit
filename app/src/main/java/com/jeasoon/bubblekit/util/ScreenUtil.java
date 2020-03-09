package com.jeasoon.bubblekit.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;

public class ScreenUtil {

    private ScreenUtil() {
    }

    public static void setOrientationPortrait(Activity context) {
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void setOrientationLandscape(Activity context) {
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void setScreenNormal(Activity context) {
        WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context.getWindow().setAttributes(attrs);
    }

    public static void setScreenFull(Activity context) {
        WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        context.getWindow().setAttributes(attrs);
    }

}
