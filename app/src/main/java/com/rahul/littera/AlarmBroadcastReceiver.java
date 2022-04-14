package com.rahul.littera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmNotificaton")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setAutoCancel(true) // the notification will disappear when user taps on it
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(9875, builder.build()); // notification id will be required if I want to update or remove the notification

    }
}
