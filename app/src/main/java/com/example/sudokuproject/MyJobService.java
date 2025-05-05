package com.example.sudokuproject;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.sudokuproject.utils.NotificationsUtils;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        // Do some work here
        NotificationsUtils.showNotification(this);

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false; // Answers the question: "Should this job be retried?"
    }
}
