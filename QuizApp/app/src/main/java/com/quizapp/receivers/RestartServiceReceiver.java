package com.quizapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quizapp.services.MyService;
import com.quizapp.utils.Constants;
import com.quizapp.utils.PrefManager;

public class RestartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //It will check is stopped by me or other. If stopped by some other reason then restart it
        if (!new PrefManager().getMyBooleanPref(context, Constants.STOP_SERVICE_PREF))
            context.startService(new Intent(context, MyService.class));
    }
}
