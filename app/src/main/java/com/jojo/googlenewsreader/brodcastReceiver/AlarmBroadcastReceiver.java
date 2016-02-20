package com.jojo.googlenewsreader.brodcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.utils.NetworkUtil;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public AlarmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String content;

        if (NetworkUtil.getConnectivityStatusBoolean(context)) {
            MainActivity.search(MainActivity.INIT_SEARCH, MainActivity.context);
            switch (MainActivity.articleCounter) {
                case 0:
                    content = "Aucun nouvel article chargé";
                    break;
                case 1:
                    content = "1 nouvel article chargé";
                    break;
                default:
                    content = MainActivity.articleCounter + " nouveaux articles chargés";
                    break;
            }
        } else {
            content = "Chargement Impossible: Pas de Réseau";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Google News Reader")
                .setAutoCancel(true)
                .setContentText(content);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());


    }


}
