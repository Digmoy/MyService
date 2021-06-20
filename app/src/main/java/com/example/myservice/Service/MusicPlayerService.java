package com.example.myservice.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myservice.MainActivity;
import com.example.myservice.R;

public class MusicPlayerService extends Service {

    private String TAG = "MyTag";
    public static String MUSIC_COMPLETE ="musicComplete";
    private final Binder mBinder = new MusicServiceBinder();
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mediaPlayer = MediaPlayer.create(this, R.raw.vemaahi);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(MUSIC_COMPLETE);
                intent.putExtra(MainActivity.MESSAGE_KEY,"done");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                stopSelf();
            }
        });
    }

    public class MusicServiceBinder extends Binder{
        public MusicPlayerService getService(){
            return MusicPlayerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mediaPlayer.release();
    }

    public void play(){
        mediaPlayer.start();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
