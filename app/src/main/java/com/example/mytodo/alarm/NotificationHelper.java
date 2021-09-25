package com.example.mytodo.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mytodo.Database.Task;

public class NotificationHelper {
    private Context context;
    private AlarmManager alarmManager;
    public NotificationHelper(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Task task){
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra("title",task.getTitle());
        intent.putExtra("des",task.getDescription());
        intent.putExtra("id",task.getNotificationId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context
                , (int) task.getNotificationId()
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,task.getDateLong(), pendingIntent);

        Log.i("TAG", "Alarm Create ");
    }

    public void deleteNotification(Task task){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int) task.getNotificationId());
    }
    public void deleteAlarm(Task task){
        Intent intent =new Intent(context,AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) task.getNotificationId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

}
