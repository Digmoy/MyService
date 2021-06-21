package com.example.myservice.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private boolean mRunning = false;
    private boolean mSuccess = false;
    private String TAG = "MyTag";

    public MyJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "onStartJob: called");
        Log.d(TAG, "onStartJob: Thread name "+Thread.currentThread().getName());
        new Thread(new Runnable() {
            @Override
            public void run() {

                int i=0;
                Log.d(TAG, "run: download started");
                while (i<10){
                    if (mRunning)
                        return;

                    Log.d(TAG, "run: download progress.."+(i+1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                Log.d(TAG, "run: download completed");
                mSuccess = true;
                jobFinished(params,mSuccess);

            }
        }).start();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mRunning = true;
        return true;
    }


}