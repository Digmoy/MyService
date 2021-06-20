package com.example.myservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myservice.Service.MusicPlayerService;
import com.example.myservice.Service.MyForegroundService;
import com.example.myservice.Service.MyStartedService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyTag";
    public static final String MESSAGE_KEY = "message_key";
    private ScrollView mScroll;
    private TextView mLog;
    private Button mPlayButton;
    private ProgressBar mProgressBar;

    private MusicPlayerService musicPlayerService;
    private boolean mBound = true;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            MusicPlayerService.MusicServiceBinder musicServiceBinder = (MusicPlayerService.MusicServiceBinder) iBinder;
            musicPlayerService=musicServiceBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String musicResult = intent.getStringExtra(MESSAGE_KEY);
            if (musicResult=="done")
                mPlayButton.setText("Play");

            Log.d(TAG, "onReceive: Thread name "+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void onBtnMusicClicked(View view) {

        if (mBound){

            if (musicPlayerService.isPlaying()){
                musicPlayerService.pause();
                mPlayButton.setText("Play");
            }
            else
            {
                Intent intent = new Intent(MainActivity.this,MusicPlayerService.class);
                startService(intent);
                musicPlayerService.play();
                mPlayButton.setText("Pause");
            }
        }

    }

    public void runCode(View v) {
        log("Running code");
        displayProgressBar(true);

        //send intent to download service
        Intent intent = new Intent(MainActivity.this, MyForegroundService.class);
        startService(intent);

    }

    private void initViews() {
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (TextView) findViewById(R.id.tvLog);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mPlayButton=findViewById(R.id.btnPlayMusic);
    }

    public void clearOutput(View v) {

        Intent intent=new Intent(MainActivity.this,MyStartedService.class);
        stopService(intent);

        mLog.setText("");
        scrollTextToEnd();
    }

    public void log(String message) {
        Log.i(TAG, message);
        mLog.append(message + "\n");
        scrollTextToEnd();
    }

    private void scrollTextToEnd() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void displayProgressBar(boolean display) {
        if (display) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this,MusicPlayerService.class);
        bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(mBroadcastReceiver,new IntentFilter(MusicPlayerService.MUSIC_COMPLETE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(mServiceConnection);
            mBound = false;
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }
}