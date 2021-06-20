package com.example.myservice.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.myservice.DownloadThread;
import com.example.myservice.MainActivity;

public class MyStartedService extends Service {

    private static final String TAG = "MyTag";
    private DownloadThread mDownlaodThread;

    //this is started service

    public MyStartedService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: called");

        mDownlaodThread=new DownloadThread();
        mDownlaodThread.start();

        while (mDownlaodThread.mHandler == null){

        }
        mDownlaodThread.mHandler.setService(this);
        mDownlaodThread.mHandler.setContext(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: called with Song Name: "+
                intent.getStringExtra(MainActivity.MESSAGE_KEY)+ " Intent Id: "+startId);
        final String songName=intent.getStringExtra(MainActivity.MESSAGE_KEY);


        Message message=Message.obtain();
        message.obj=songName;
        message.arg1=startId;

        mDownlaodThread.mHandler.sendMessage(message);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: called");
        return null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }
}