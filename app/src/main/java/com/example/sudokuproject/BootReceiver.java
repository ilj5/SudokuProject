package com.example.sudokuproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.sudokuproject.utils.SchedulerUtils;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SchedulerUtils.scheduleJob(context);
        }
    }
}
