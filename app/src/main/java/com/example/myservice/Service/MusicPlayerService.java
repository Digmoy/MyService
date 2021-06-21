package com.example.myservice.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myservice.Constants.Constants;
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
                stopForeground(true);
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

        switch (intent.getAction()){
            case Constants.MUSIC_SERVICE_ACTION_PLAY:{
                play();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_PAUSE:{
                pause();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_STOP:{
                stopForeground(true);
                stopSelf();
                break;
            }
            case Constants.MUSIC_SERVICE_ACTION_START:{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startMyOwnForeground();
                else
                    startForeground(1, new Notification());

                break;
            }
            default:{
                stopSelf();
            }
        }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.myservice";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        // intent for play
        Intent pIntent = new Intent(this,MusicPlayerService.class);
        pIntent.setAction(Constants.MUSIC_SERVICE_ACTION_PLAY);

        PendingIntent playIntent = PendingIntent.getService(this,100,pIntent,0);

        // intent for pause
        Intent psIntent = new Intent(this,MusicPlayerService.class);
        psIntent.setAction(Constants.MUSIC_SERVICE_ACTION_PAUSE);

        PendingIntent pauseIntent = PendingIntent.getService(this,100,psIntent,0);

        // intent for stop
        Intent sIntent = new Intent(this,MusicPlayerService.class);
        sIntent.setAction(Constants.MUSIC_SERVICE_ACTION_STOP);

        PendingIntent stopIntent = PendingIntent.getService(this,100,sIntent,0);

        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hello")
                .setContentText("This is service notification")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Play",playIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause,"Pause",pauseIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Stop",stopIntent))
                .build();
        startForeground(2, notification);
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
