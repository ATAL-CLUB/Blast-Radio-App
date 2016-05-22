package com.codecanyon.streamradio;

import com.codecanyon.radio.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationPanel {

    private static NotificationManager nManager;
    private Context parent;
    private NotificationCompat.Builder nBuilder;

    //private static RemoteViews remoteView;

    public NotificationPanel(Context parent) {
        this.parent = parent;
    }

    public static void notificationCancel() {
        nManager.cancel(1);
    }

    public void showNotification(String radioName, boolean status) {
        // TODO Auto-generated constructor stub
        Intent intent;
        if(!status)
             intent = new Intent(parent, MainActivity.class);
        else
            intent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //------------------------------------------------
        nBuilder = new NotificationCompat.Builder(parent)
                .setContentTitle(radioName)
                .setContentText(MainActivity.getRadioListLocation().getText().toString())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setWhen(0);

        /*remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationview);
        remoteView.setTextViewText(R.id.text_not, radioName);
        remoteView.setTextViewText(R.id.text_title_not, trackTitle);


        nBuilder.setContent(remoteView);*/

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = nBuilder.build();
        //noti.flags |= Notification.FLAG_AUTO_CANCEL;
        //noti.contentView = remoteView;
        noti.flags |= Notification.FLAG_NO_CLEAR;

        nManager.notify(1, noti);
    }
}