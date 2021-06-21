package com.example.myservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myservice.Service.MyJobService;

public class MainActivity2 extends AppCompatActivity {

    public static int JOB_ID =101;
    private String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    //this method schedules the job
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleService(View view){
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setMinimumLatency(0)
                .setPersisted(true)
                .build();

       int result = jobScheduler.schedule(jobInfo);

       if (result == JobScheduler.RESULT_SUCCESS)
           Log.d(TAG, "scheduleService: Job scheduled");
       else
           Log.d(TAG, "scheduleService: Job not scheduled");
    }

    //this method cancels the scheduled job
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelService(View view){

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        jobScheduler.cancel(JOB_ID);
        Log.d(TAG, "cancelService: job cancelled");
    }
}