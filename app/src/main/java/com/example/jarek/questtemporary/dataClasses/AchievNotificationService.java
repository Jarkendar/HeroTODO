package com.example.jarek.questtemporary.dataClasses;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.jarek.questtemporary.R;
import com.example.jarek.questtemporary.activityClasses.AchievementActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AchievNotificationService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ACHIEV = "achievMessage";

    public static int NOTIFICATION_ACHIEV_ID = 6544;

    public AchievNotificationService() {
        super("AchievNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            String achievName = intent.getStringExtra(EXTRA_ACHIEV);
            createAchievNotify(message, achievName);
            NOTIFICATION_ACHIEV_ID++;
        }
    }


    private void createAchievNotify(String message, String achievName){
        Intent intent = new Intent(this, AchievementActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(AchievementActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message +" "+achievName)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ACHIEV_ID, notification);
    }


}
