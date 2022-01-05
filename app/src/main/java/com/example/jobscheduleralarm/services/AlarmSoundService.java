package com.example.jobscheduleralarm.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.jobscheduleralarm.R;

public class AlarmSoundService extends Service {
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player != null && !player.isPlaying()) player.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Music", "onCreate: player initiated");
        if (player == null) player = MediaPlayer.create(getApplicationContext(), R.raw.give_me_some_sunshine);
        player.setLooping(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        player = null;
    }

}
