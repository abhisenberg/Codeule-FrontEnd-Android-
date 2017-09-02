package com.example.abheisenberg.cccliveschedule.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.abheisenberg.cccliveschedule.R;

/**
 * Created by abheisenberg on 20/8/17.
 */

public class NotificationMaker {
    public static final String TAG = "NotiMaker";

    private Context context;
    private long delay;
    private int notiID;

    public NotificationMaker(Context context) {
        this.context = context;
    }

    public void ScheduleNotification(String contest_name, String link, long delay) {
        this.notiID = contest_name.hashCode();
        this.delay = delay;

        //Preparing and building the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(contest_name)
                .setContentText("Begins in 15 minutes! Click to go the website and register now.")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent webpageIntent = new Intent();
        webpageIntent.setAction(Intent.ACTION_VIEW);
        webpageIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        webpageIntent.setData(Uri.parse(link));

        PendingIntent pi_webpage = PendingIntent.getActivity(
                context,
                notiID,
                webpageIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pi_webpage);
        Notification noti = builder.build();

        //Setting up the future launch of the notification
        Intent notiIntent = new Intent(context, NotificationPublisher.class);
        notiIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notiID);
        notiIntent.putExtra(NotificationPublisher.NOTIFICATION, noti);

        PendingIntent notiLaunchPI = PendingIntent.getBroadcast(
                context,
                notiID,
                notiIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        //Setting up AlarmManager to fire notification at future time
        long firingTimeInMillis = System.currentTimeMillis()+delay;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firingTimeInMillis, notiLaunchPI);

        Log.d(TAG, "ScheduleNotification: "+contest_name);
    }


}
