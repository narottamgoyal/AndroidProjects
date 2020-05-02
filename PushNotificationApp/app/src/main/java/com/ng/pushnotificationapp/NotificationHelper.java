package com.ng.pushnotificationapp;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static final String Channel_Id = "notification_Channel_Id";
    public static final String Channel_Name = "notification_Channel_Name";
    public static final String Channel_Des = "notification Channel des";

    public static void displayNotification(Context context, String title, String body) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, Channel_Id)
                        .setSmallIcon(R.drawable.ic_note)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
