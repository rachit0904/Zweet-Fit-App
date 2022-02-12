package com.practise.zweet_fit_app.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BrodcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ForegroundService.class));
    }
}
