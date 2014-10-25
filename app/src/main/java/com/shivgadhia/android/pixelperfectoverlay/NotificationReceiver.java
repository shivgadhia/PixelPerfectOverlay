package com.shivgadhia.android.pixelperfectoverlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (ACTION_STOP_SERVICE.equals(action)) {
            context.stopService(new Intent(context, ScreenshotOverlayService.class));
        }
    }


}