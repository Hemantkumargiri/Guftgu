package com.guftgue.others;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.guftgue.R;


public class MyNotificationManager {

    private static final int ID_SMALL_NOTIFICATION = 235;
    private MyPreferences mPrefsFile;

    private Context mCtx;
    private Ringtone r;

    MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
        mPrefsFile = new MyPreferences(mCtx);

    }


    void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        String channelId = "com.todaystatement.one";
        String channelName = "Today Statement";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, channelId);
        mBuilder.setSmallIcon(R.drawable.logo);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setAutoCancel(false)
                .setContentInfo("TS")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)

                .setColor(Color.parseColor("#FF0000"))

                .setColorized(true)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo))
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, channelName, importance);


            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            mBuilder.setChannelId(channelId);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        assert notificationManager != null;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);

       /* if (myPrefsFile.isLoggedIn() && myPrefsFile.isNotiifcationEnabled()) {
            assert notificationManager != null;
            notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
        }*/

    }


}
