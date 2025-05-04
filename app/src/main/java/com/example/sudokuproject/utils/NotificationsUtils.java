package com.example.sudokuproject.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
//import android.support.v4.app.NotificationCompact;
//import android.support.v4.app.NotificationCompact.Builder;

import com.example.sudokuproject.R;

public class NotificationsUtils {
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNEL_ID = "main";
    private static final int PENDING_INTENT_REQUEST_CODE = 0;

    public static void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "reminder",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(mChannel);
        }


    }
}
