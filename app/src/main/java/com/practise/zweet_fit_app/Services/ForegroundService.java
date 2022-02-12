package com.practise.zweet_fit_app.Services;

import static com.practise.zweet_fit_app.Util.Constant.Shared_userdata;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.practise.zweet_fit_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ForegroundService extends Service {
    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    final static int Duration = 10  * 1000;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    boolean isFirstNotificatio = true;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref = getSharedPreferences(Shared_userdata, Context.MODE_PRIVATE);
        load();
        return START_STICKY;
    }


    void load() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String steps = pref.getString("steps", "");
                if (steps.equals("")) {
                    steps = "0";
                }
                String target = pref.getString("target", "");
                if (target.equals("")) {
                    target = "0";
                }
                int stepsi = Integer.parseInt(steps);
                int targeti = Integer.parseInt(target);
                if (stepsi > targeti) {
                    String estring = pref.getString(getCurrentDateAndTime(), "");
                    if (estring.equals("")) {
                        preferences = pref.edit();
                        preferences.putString(getCurrentDateAndTime(), "true");
                        preferences.apply();
                        startMyOwnForegroundComplete();
                        addcoin();
                    }


                }
                startMyOwnForeground(steps);
            }
        }, Duration);
    }

    private void addcoin() {
    }

    private void startMyOwnForegroundComplete() {
        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (getApplicationContext()).setContentTitle("Congratulations").setContentText("Target Completed").
                setSmallIcon(R.mipmap.ic_launcher).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

    public static String getCurrentDateAndTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    private void startMyOwnForeground(String content) {
        String NOTIFICATION_CHANNEL_ID = "0011";
        String channelName = "steps count";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Notification notification = null;
        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Tracking Your Steps")
                .setContentText("Total Steps : " + content)
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOnlyAlertOnce(true)
                .build();

        startForeground(2, notification);

//        if (isFirstNotificatio) {
//            isFirstNotificatio = false;
//
//
//        }

        manager.notify(Integer.parseInt(NOTIFICATION_CHANNEL_ID), notificationBuilder.build());


    }


}