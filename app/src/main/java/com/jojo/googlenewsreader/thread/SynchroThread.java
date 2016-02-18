//package com.jojo.googlenewsreader.thread;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import com.jojo.googlenewsreader.R;
//import com.jojo.googlenewsreader.activities.MainActivity;
//import com.jojo.googlenewsreader.utils.NetworkUtil;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.jojo.googlenewsreader.R.string.message;
//
//public class SynchroThread extends Thread{
//
//    private Context context;
//
//    public SynchroThread(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public void run() {
////        boolean isFirstTime = false;
////        while(true){
////            try {
////                this.sleep(5000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            if(NetworkUtil.getConnectivityStatusBoolean(context) && isFirstTime){
////                Notification notification;
////                notification = new Notification("This is message from Dipak Keshariya (Android Application Developer)", context.getString(message));
////            } else {
////
////            }
////            isFirstTime = false;
////        }
//
//
//
//
//
//    }
//
//    private void showNotification(String eventtext, Context ctx) {
//
//
//
//        // Set the icon, scrolling text and timestamp
//        Notification notification = new Notification(R.mipmap.ic_launcher, "notif", System.currentTimeMillis());
//
//        // The PendingIntent to launch our activity if the user selects this
//        // notification
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MainActivity.class), 0);
//
//        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(ctx, "Title", eventtext, contentIntent);
//
//        // Send the notification.
//        mNM.notify("Title", 0, notification);
//    }
//
//}

