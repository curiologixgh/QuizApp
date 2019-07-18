package com.quizapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.quizapp.R;
import com.quizapp.utils.Constants;
import com.quizapp.utils.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    //It will use to count down timer at every 1 sec
    private Timer timer;
    private TimerTask timerTask;
    // It will use to manage notification
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;

    //Use to save data
    private PrefManager prefManager;

    private int remainingNotificationTime = 300; // Remaining break time Total time will 5 mint
    private int delayInMilliseconds = 1; // 1 sec // Delay for second to check remaining time
    private int notificationId = 1; // It will used to show different notification. For i am showing only 1 notification so its fixed 1

    public MyService(Context mContext) {
        super();
    }

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        prefManager = new PrefManager();
        //Get remaining notification time if service stop for some reason.
        remainingNotificationTime = prefManager.getMyIntPref(getApplicationContext(), Constants.REMAINING_TIME_PREF, 300);
        //Start time for every 1 sec
        startTimer();

        return START_STICKY; // Stick intent will restart the service if kill by some reason
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //if service stopped for some reason. It will send broadcast to re-start it again
        Intent broadcastIntent = new Intent("quiz.app.restart.service");
        sendBroadcast(broadcastIntent);
    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000);
    }

    //Timer method which will recall every 1 second and will stopped after 5 mint
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                remainingNotificationTime -= delayInMilliseconds;
                if (remainingNotificationTime <= 0) {
                    //Save into local storage Remaining time 0 and Stopped service value to true
                    prefManager.setMyIntPref(getApplicationContext(), Constants.REMAINING_TIME_PREF, 0);
                    prefManager.setMyBooleanPref(getApplicationContext(), Constants.STOP_SERVICE_PREF, true);

                    if (notificationManager != null)
                        notificationManager.cancelAll();

                    //stop timer and service because 5 mint completed
                    stopTimerTask();
                    stopSelf();
                } else {
                    prefManager.setMyIntPref(getApplicationContext(), Constants.REMAINING_TIME_PREF, remainingNotificationTime);

                    String leftTime = convertSecondsToHMmSs(remainingNotificationTime);
                    String body = getString(R.string.time_left) + leftTime;
                    showNotification(body);
                }
            }
        };
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // It will show the notification with some title and text. It will also work for >= Android version Oreo
    public void showNotification(String body) {
        String title = getString(R.string.you_are_on_the_break);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    // it will convert seconds into hours, mints and sec
    private String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
