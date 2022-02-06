package com.practise.zweet_fit_app.Services;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;
import java.util.Random;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage, remoteMessage.getData().get("click_action"));
    }


    //text Notification
    private void showNotification(RemoteMessage remoteMessage, String click_action) {
        Map<String, String> Data = remoteMessage.getData();
        Intent intent = new Intent(click_action);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        intent.putExtra("uid", Data.get("uid"));
        intent.putExtra("sid", Data.get("sid"));
        intent.putExtra("cid", Data.get("cid"));
        intent.putExtra("name", Data.get("name"));
        intent.putExtra("nid", Data.get("nid"));
        String channelIdd = Data.get("cid").replaceAll("[-+^]*", " ");
        intent.putExtra("profile", Data.get("profile"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        int noificationId = new Random().nextInt(100);
        String channelId = "notification_channel_3";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), channelId
        );
//        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle(remoteMessage.getNotification().getTitle()); // make suer change the channel for image
        builder.setContentText(remoteMessage.getNotification().getBody());
        //notification for image
//        builder.setStyle(new NotificationCompat.BigPictureStyle().
//                bigPicture(bitmap));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.
                    getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId, "Notification channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This notification channel is used to notify user for chats activity");
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        assert notificationManager != null;
        if (remoteMessage.getData().get("icon") == null) {

            notificationManager.notify(noificationId, notification);
        } else {
            final Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(() -> Picasso.get()
                    .load(remoteMessage.getData().get("icon"))
                    .resize(200, 200)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                            builder.setLargeIcon(bitmap);
                            notificationManager.notify(0, builder.build());
                        }

                        @Override
                        public void onBitmapFailed(Exception e, final Drawable errorDrawable) {
                            // Do nothing?
                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {
                            // Do nothing?
                        }
                    }));


        }


    }


    private void sendNotification(RemoteMessage remoteMessage, String clickAction) {
        Map<String, String> Data = remoteMessage.getData();
        Intent intent = new Intent(clickAction);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        intent.putExtra("uid", Data.get("uid"));
        intent.putExtra("sid", Data.get("sid"));
        intent.putExtra("cid", Data.get("cid"));
        intent.putExtra("name", Data.get("name"));
        intent.putExtra("nid", Data.get("nid"));
        String channelIdd = Data.get("cid").replaceAll("[-+^]*", " ");
        intent.putExtra("profile", Data.get("profile"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Data.get("uid"))
//                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLights(Color.GREEN, 3000, 3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelIdd, "Hunch Direct", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(false);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            final Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(() -> Picasso.get()
                    .load(remoteMessage.getData().get("icon"))
                    .resize(200, 200)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                            notificationBuilder.setLargeIcon(bitmap);
                            notificationManager.notify(0, notificationBuilder.build());
                        }

                        @Override
                        public void onBitmapFailed(Exception e, final Drawable errorDrawable) {
                            // Do nothing?
                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {
                            // Do nothing?
                        }
                    }));


        } else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            final Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get()
                            .load(remoteMessage.getData().get("icon"))
                            .resize(200, 200)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                                    notificationBuilder.setLargeIcon(bitmap);
//                                    notificationBuilder.setSmallIcon(IconCompat.createWithBitmap(bitmap));
                                    notificationManager.notify(0, notificationBuilder.build());
                                }

                                @Override
                                public void onBitmapFailed(Exception e, final Drawable errorDrawable) {
                                    // Do nothing?
                                }

                                @Override
                                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                    // Do nothing?
                                }
                            });
                }
            });

//            notificationManager.notify(0, notificationBuilder.build());
        }

    }


    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }
}
