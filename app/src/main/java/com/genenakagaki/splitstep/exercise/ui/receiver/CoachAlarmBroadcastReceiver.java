package com.genenakagaki.splitstep.exercise.ui.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;

/**
 * Created by Gene on 10/10/2017.
 */

public class CoachAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Your tag");

        // Acquire the lock
        wl.acquire();

        // You can do the processing here
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "splitstep_channel";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_check)
                        .setContentTitle("My notification")
                        .setContentText("Hello world!")
                        .setVibrate(new long[] {0, 1000, 100, 100, 100, 100, 500, 1000, 500, 100, 100, 100, 100, 100, 100, 100, 100, 100})
                        .setChannelId(id);

        Intent resultIntent = new Intent(context, ExerciseActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ExerciseActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        nm.notify(1, builder.build());

        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null && extras.getBoolean("a", Boolean.FALSE)) {
            // Make sure this intent has been sent by Splitstep coach fragment
            msgStr.append("adfdadf");
        }

        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        // Release the lock
        wl.release();
    }
}
