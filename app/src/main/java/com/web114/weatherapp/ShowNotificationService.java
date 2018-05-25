package com.web114.weatherapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

/**
 * Created by user on 5/17/2018.
 */

public class ShowNotificationService extends IntentService {
    private static final String ACTION_SHOW_NOTIFICATION = "my.app.service.action.show";
    private static final String ACTION_HIDE_NOTIFICATION = "my.app.service.action.hide";
    private static int STATUS_ICON_REQUEST_CODE;

    public ShowNotificationService() {
        super("ShowNotificationIntentService");
    }

    public static void startActionShow(Context context) {
        Intent intent = new Intent(context, ShowNotificationService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        context.startService(intent);
    }

    public static void startActionHide(Context context) {
        Intent intent = new Intent(context, ShowNotificationService.class);
        intent.setAction(ACTION_HIDE_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NOTIFICATION.equals(action)) {
                handleActionShow();
            } else if (ACTION_HIDE_NOTIFICATION.equals(action)) {
                handleActionHide();
            }
        }
    }

    private void handleActionShow() {
        showStatusBarIcon(ShowNotificationService.this);
    }

    private void handleActionHide() {
       // hideStatusBarIcon(ShowNotificationService.this);
    }

    public void showStatusBarIcon(Context ctx) {
        Context context = ctx;
        SharedPreferences prefs = getSharedPreferences("context", MODE_PRIVATE);
        String city = prefs.getString("city", null);
        String weatherTemp= prefs.getString("weatherTemp", null);
        String current_time = prefs.getString("Time", null);
        String weather_description = prefs.getString("weatherDescription", null);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setContentTitle(weatherTemp+""+weather_description).setContentText(city+"                  "+current_time)
                .setSmallIcon(R.drawable.sun)
                .setOngoing(true);
        Intent intent = new Intent(context,MainActivity.class);


        PendingIntent pIntent = PendingIntent.getActivity(context, STATUS_ICON_REQUEST_CODE, intent, 0);
        builder.setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = builder.build();
        notif.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(STATUS_ICON_REQUEST_CODE, notif);
    }
}