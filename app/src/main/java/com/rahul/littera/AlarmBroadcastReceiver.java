package com.rahul.littera;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
          //  SharedPreferences sharedPreferences = .getSharedPreferences("com.rahul.littera", Context.MODE_PRIVATE);
//            SharedPreferences sp = sharedPreferences;
//            String s = sp.getString("data", null);
//            Log.i("retrieve","String: "+ s);
//            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s.getBytes()));
//            ObjectInputStream in = new ObjectInputStream(bis);
//            Data.instance = (Data) in.readObject();
//            Log.i("retrieval","successful - local");

            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            Log.i("Title and description", title + " " + description);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play(); //this will play the notification sound.

            // Create an explicit intent for an Activity in your app
            Intent inte = new Intent(context, FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, inte, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmNotificaton")
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)// the notification will disappear when user taps on it
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(9875, builder.build()); // notification id will be required if I want to update or remove the notification

        } catch (Exception e ) {
            Log.i("Error",e.toString());

        }
//

    }
}
