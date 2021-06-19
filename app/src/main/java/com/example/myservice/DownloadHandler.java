package com.example.myservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myservice.Service.MyStartedService;

public class DownloadHandler extends Handler {

    private static final String TAG = "MyTag";
    private final MainActivity mActivity;

    public DownloadHandler(MainActivity activity) {
        this.mActivity=activity;
    }

    @Override
    public void handleMessage(Message msg) {

        downloadSong(msg.obj.toString());
    }

    private void downloadSong(final String songName){
        Log.d(TAG, "run: staring download");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.log("Downlaod Complete "+songName);
                mActivity.displayProgressBar(false);
            }
        });



        Log.d(TAG, "downloadSong: "+songName+" Downloaded...");
    }
}
