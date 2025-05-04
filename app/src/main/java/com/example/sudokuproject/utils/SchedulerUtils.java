package com.example.sudokuproject.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.example.sudokuproject.MyJobService;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {
    private static final int NOTIFICATION_JOB_ID = 0;
    private static final int NOTIFICATION_INTERVAL_MILLIS = (int) TimeUnit.DAYS.toMillis(7);

    public static void scheduleJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler == null) {
            return;
        }

        ComponentName jobService = new ComponentName(context, MyJobService.class);

        JobInfo jobInfo = new JobInfo.Builder(NOTIFICATION_JOB_ID, jobService)
                .setPeriodic(NOTIFICATION_INTERVAL_MILLIS)
                .setPersisted(true).build();

        jobScheduler.schedule(jobInfo);
    }
    public static void unscheduleJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        jobScheduler.cancel(NOTIFICATION_JOB_ID);
    }
}
