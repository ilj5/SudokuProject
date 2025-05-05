package com.example.sudokuproject.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
//import android.support.v4.app.NotificationCompact;
//import android.support.v4.app.NotificationCompact.Builder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.sudokuproject.LogIn;
import com.example.sudokuproject.MainActivity;
import com.example.sudokuproject.R;

public class NotificationsUtils {
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNEL_ID = "main";
    private static final int PENDING_INTENT_REQUEST_CODE = 0;

    public static void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sudoku Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }


        Intent intent2 = new Intent(context, LogIn.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, PENDING_INTENT_REQUEST_CODE, intent2, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello, world!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();

        notificationManager.notify(NOTIFICATION_ID, notification);

    }
}
