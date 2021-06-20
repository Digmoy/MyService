package com.example.myservice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myservice.Service.MyStartedService;

public class DownloadHandler extends Handler {


    private static final String TAG = "MyTag";
    private MyStartedService mService;
    public static String SERVICE_MESSAGE = "service_message";
    private Context mContext;

    public DownloadHandler() {
    }

    @Override
    public void handleMessage(Message msg) {

        downloadSong(msg.obj.toString());
        mService.stopSelf(msg.arg1);
        Log.d(TAG, "handleMessage: Song Downloaded: "+msg.obj.toString() + " Intent Id: "+msg.arg1);

        sendDataToUi(msg.obj.toString());


    }

    private void sendDataToUi(String toString) {
        Intent intent= new Intent(SERVICE_MESSAGE);
        intent.putExtra(MainActivity.MESSAGE_KEY,toString);

        // Local BroadcastReceiver

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void downloadSong(final String songName){
        Log.d(TAG, "run: staring download");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "downloadSong: "+songName+" Downloaded...");
    }

    public void setService(MyStartedService downloadService) {
        this.mService=downloadService;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
