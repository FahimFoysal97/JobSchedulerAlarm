package com.example.jobscheduleralarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jobscheduleralarm.services.AlarmSoundService;
import com.example.jobscheduleralarm.util.Constant;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.jobscheduleralarm.ACTION_ALARM")) {
            Log.d(TAG, "onReceive: "+"ACTION_ALARM");
            showNotification(context, intent);
            startAlarmSound(context);
        }
    }

    private void showNotification(Context context, Intent intent) {
        long[] pattern = {0, 1000, 500, 1000};
        String NOTIFICATION_CHANNEL_ID = Constant.CHANNEL_ID_PUSH_NOTIFICATION;
        String title = "Alarm";
        String content = "This is a message from JobSchedulerAlarm";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationChannel notificationChannel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID, Constant.CHANNEL_NAME_PUSH_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("");
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(pattern);
        notificationManager.createNotificationChannel(notificationChannel);

        Intent snoozeIntent = new Intent(context, AlarmStopBroadcastReceiver.class);
        snoozeIntent.setAction("com.example.jobscheduleralarm.ACTION_STOP_ALARM");
        snoozeIntent.setPackage("com.example.jobscheduleralarm");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .addAction(R.drawable.ic_baseline_mail_24, "Snooze", snoozePendingIntent)
                .setColor(Color.BLUE)
                .setSmallIcon(R.drawable.ic_baseline_mail_24);
        notificationManager.notify(Constant.PUSH_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void startAlarmSound(Context context) {
        context.getApplicationContext().startService(new Intent(context.getApplicationContext(), AlarmSoundService.class));
    }
}
