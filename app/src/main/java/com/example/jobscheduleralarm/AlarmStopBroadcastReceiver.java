package com.example.jobscheduleralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.jobscheduleralarm.services.AlarmSoundService;
import com.example.jobscheduleralarm.util.Constant;

public class AlarmStopBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmStopBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.jobscheduleralarm.ACTION_STOP_ALARM")) {
            Log.d(TAG, "onReceive: "+"ACTION_STOP_ALARM");
            context.getApplicationContext().stopService(new Intent(context.getApplicationContext(), AlarmSoundService.class));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(Constant.PUSH_NOTIFICATION_ID);
        }
    }
}
