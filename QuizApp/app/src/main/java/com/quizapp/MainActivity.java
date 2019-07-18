package com.quizapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quizapp.services.MyService;
import com.quizapp.utils.Constants;
import com.quizapp.utils.base.BaseActivity;

public class MainActivity extends BaseActivity {
    //UI TextView which will show build variant flavor
    private TextView buildFlavorTV;
    // Intent for Notification Service
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews(); //Initialize all the UI widget of MainActivity
        updateData();  //it will set build variant flavor into TextView
        startNotificationService(); // it will start service if not started yet. Service for 5 mint count down timer for the first time.
    }

    private void initViews() {
        buildFlavorTV = findViewById(R.id.buildFlavorTV); // initialize the TextView
    }

    private void updateData() {
        buildFlavorTV.setText(getString(R.string.flavour));
    }

    private void startNotificationService() {
        //Check is service stopped by me and Already completed
        boolean isServiceStopped = prefManager.getMyBooleanPref(mAct, Constants.STOP_SERVICE_PREF);

        MyService mService = new MyService(mAct); // MyService init
        mServiceIntent = new Intent(mAct, mService.getClass());
        // it will check is service already running or not and is service stopped by me or not
        if (!isServiceStopped && !isServiceAlreadyRunning(mService.getClass()))
            startService(mServiceIntent); // here i am starting service
    }

    //Check is service already running or not
    private boolean isServiceAlreadyRunning(Class<?> mServiceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo mService : manager.getRunningServices(Integer.MAX_VALUE))
            if (mServiceClass.getName().equals(mService.service.getClassName()))
                return true;

        return false;
    }
}