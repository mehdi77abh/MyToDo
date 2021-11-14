package com.example.mytodo.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.mytodo.R;
import com.example.mytodo.mainActivity.MainActivity;

public class AlarmService extends BroadcastReceiver {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Tasks";

    private NotificationManager mManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String des =intent.getStringExtra("des");
        int id = (int) intent.getLongExtra("id",0);
        Intent resultIntent = new Intent(context, MainActivity.class);
    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //Date.Time
        PendingIntent pendingIntent =PendingIntent.getActivity(context
                , id, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.i("TAG", "createNotificationChannel: Done ");

            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            if (mManager == null) {
                mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            mManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, channelID)
                .setContentTitle(title)
                .setContentText(des)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_check_24_black);
        mManager.notify(id, nb.build());


    }
}
