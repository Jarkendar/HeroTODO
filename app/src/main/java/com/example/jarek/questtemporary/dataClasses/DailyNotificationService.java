package com.example.jarek.questtemporary.dataClasses;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.activityClasses.QuestPanelMain;

import java.util.Calendar;

public class DailyNotificationService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";

    public static final int NOTIFICATION_DAILY_ID = 6543;

    public DailyNotificationService() {
        super("DailyNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            if (calendar.getTimeInMillis() < System.currentTimeMillis()){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
            long time = calendar.getTimeInMillis() - System.currentTimeMillis();
            Log.d("++++++", "onHandleIntent: "+time/1000+"s");
            synchronized (this) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            createDailyNotify(message);
        }
    }

    private void createDailyNotify(String message) {
        Intent intent = new Intent(this, QuestPanelMain.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(QuestPanelMain.class);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_DAILY_ID, notification);
    }

}
