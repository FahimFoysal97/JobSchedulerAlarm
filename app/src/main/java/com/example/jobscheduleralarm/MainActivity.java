package com.example.jobscheduleralarm;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.jobscheduleralarm.databinding.ActivityMainBinding;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private static final String TAG = "MainActivity";
    AlarmBroadcastReceiver alarmBroadcastReceiver;
    AlarmStopBroadcastReceiver alarmStopBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        initUI();
    }

    private void initUI() {
        initButtons();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {
        //AlarmBroadcastReceiver register
        IntentFilter intentFilter = new IntentFilter("com.example.jobscheduleralarm.ACTION_ALARM");
        alarmBroadcastReceiver = new AlarmBroadcastReceiver();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(alarmBroadcastReceiver, intentFilter);

        //AlarmStopBroadcastReceiver register
        /*IntentFilter intentFilter2 = new IntentFilter("com.example.jobscheduleralarm.ACTION_STOP_ALARM");
        alarmStopBroadcastReceiver = new AlarmStopBroadcastReceiver();
        registerReceiver(alarmStopBroadcastReceiver, intentFilter2);*/
    }

    private void initButtons() {
        activityMainBinding.buttonStart.setOnClickListener(v -> {
            ComponentName componentName = new ComponentName(this, AlarmJobScheduler.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setPersisted(true)
                    .setPeriodic(15*60*1000)
                    .build();

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
            } else {
                Log.d(TAG, "Job scheduling failed");
            }
        });
        activityMainBinding.buttonStop.setOnClickListener(v -> {
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(123);
            Log.d(TAG, "Job cancelled");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(alarmBroadcastReceiver);
    }
}