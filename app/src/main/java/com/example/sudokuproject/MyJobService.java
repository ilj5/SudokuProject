package com.example.sudokuproject;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        // Do some work here
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false; // Answers the question: "Should this job be retried?"
    }
}
