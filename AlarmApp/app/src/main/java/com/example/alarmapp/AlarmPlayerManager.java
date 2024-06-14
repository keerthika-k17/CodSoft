package com.example.alarmapp;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmPlayerManager {
    private static MediaPlayer mediaPlayer;

    public static void startAlarm(Context context)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        }
        mediaPlayer.start();
    }

    public static void stopAlarm()
    {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
