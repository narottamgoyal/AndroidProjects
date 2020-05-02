package com.ng.pushnotificationapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;

public class NotificationHelper {

    public static final String Channel_Id = "notification_Channel_Id";
    public static final String Channel_Name = "notification_Channel_Name";
    public static final String Channel_Des = "notification Channel des";

    public static void displayNotification(Context context, String title, String body) {

        Intent intent;
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            intent = new Intent(context, MainActivity.class);
        else
            intent = new Intent(context, ProfileActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, Channel_Id)
                        .setSmallIcon(R.drawable.ic_note)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }

    public static void displayAppNotification(Context context, String title, String body) {

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
