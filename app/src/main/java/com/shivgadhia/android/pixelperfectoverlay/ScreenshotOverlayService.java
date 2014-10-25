package com.shivgadhia.android.pixelperfectoverlay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static com.shivgadhia.android.pixelperfectoverlay.NotificationReceiver.ACTION_STOP_SERVICE;

public class ScreenshotOverlayService extends Service {

    public static final String EXTRA_SCREENSHOT_URI = "extra_screenshot_uri";

    private static final int X_OFFSET = 0;
    private static final int Y_OFFSET = 24;// I have no idea why!
    private static final int REQUEST_CODE = R.id.request_code_pending_intent;
    private static final int NOTIFICATION_ID = R.id.notification;

    private NotificationManager notificationManager;
    private ScreenshotView screenshotView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupServiceNotification();
        setupScreenshotView(intent);
        return START_STICKY;
    }

    private void setupServiceNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.drawable.ic_launcher)
                .setDeleteIntent(createStopServiceIntent()) // Intent to fire when notification is dismissed
                .setContentIntent(createStopServiceIntent())
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private PendingIntent createStopServiceIntent() {
        Intent intent = new Intent(ACTION_STOP_SERVICE);
        return PendingIntent.getBroadcast(this.getApplicationContext(), REQUEST_CODE, intent, 0);
    }

    private void setupScreenshotView(Intent intent) {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = getLayoutParams(wm);

        Uri screenshotUri = intent.getParcelableExtra(EXTRA_SCREENSHOT_URI);
        screenshotView = new ScreenshotView(this, screenshotUri);
        wm.addView(screenshotView, params);
    }

    private WindowManager.LayoutParams getLayoutParams(WindowManager wm) {
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);

        return new WindowManager.LayoutParams(
                metrics.widthPixels,
                metrics.heightPixels,
                X_OFFSET,
                Y_OFFSET,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (screenshotView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(screenshotView);
            screenshotView = null;
        }
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
