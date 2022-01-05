package com.example.jobscheduleralarm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AlarmJobScheduler extends JobService {
    private static final String TAG = "AlarmJobScheduler";
    private boolean isAlarmCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(() -> {
            getInterval();
            executeBroadcast();
            Log.d(TAG, "Job finished");
            jobFinished(params, false);
        }).start();
        return true;
    }

    private void executeBroadcast() {
        Intent intent = new Intent("com.example.jobscheduleralarm.ACTION_ALARM");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void executeInterval() {
        try {
            //Thread.sleep(getInterval()):
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getInterval() {
        int alarmHour = 10, alarmMinute = 17, alarmSecond = 0;
        int alarmTimeInSeconds = ((alarmHour % 12) * 60 + alarmMinute) * 60 + alarmSecond;
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        int currentSecond = currentTime.get(Calendar.SECOND);
        int currentTimeInSecond = ((currentHour % 12) * 60 + currentMinute) * 60 + currentSecond;
        long interval = alarmTimeInSeconds - currentTimeInSecond;
        try {
            Thread.sleep(interval*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        isAlarmCancelled = true;
        return true;
    }
}
