package com.alphabetastudios.codeule.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String TAG = "NotiPublisher";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        NotificationManager nm = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        int notification_id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        nm.notify(notification_id, notification);
    }
}
