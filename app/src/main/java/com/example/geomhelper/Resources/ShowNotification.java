package com.example.geomhelper.Resources;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.R;

public class ShowNotification extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "0")
                        .setSmallIcon(R.drawable.ic_menu_leaderboard)
                        .setColor(getResources().getColor(R.color.leaderboard))
                        .setContentTitle("GeomHelper")
                        .setContentText("Ты уже давно не занимался!")
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true);

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}