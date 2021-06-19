package com.example.myservice;

import android.os.Looper;

public class DownloadThread extends Thread {

    private static final String TAG = "MyTag";
    private final MainActivity mActivity;
    public DownloadHandler mHandler;

    public DownloadThread(MainActivity activity) {
        this.mActivity=activity;
    }

    @Override
    public void run() {

        Looper.prepare();
        mHandler=new DownloadHandler(mActivity);
        Looper.loop();

    }
}
