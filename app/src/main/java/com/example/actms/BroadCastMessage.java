package com.example.actms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadCastMessage extends BroadcastReceiver {
    GlobalVariable globalVariable;
//    private LocalBroadcastManager localBroadcastManager ;
//
//    private IntentFilter intentFilter ;
    @Override
    public void onReceive(Context context, Intent intent) {
        globalVariable=(GlobalVariable)context.getApplicationContext();
//        localBroadcastManager = LocalBroadcastManager.getInstance( context ) ;
        Log.d("Service" , "Message-----------------------------------------------------------------------");
        if(intent.getAction().equals("MessageAction")) {
            try {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                // The first in the list of RunningTasks is the foreground task.
//            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
//            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
//            if(!foregroundTaskPackageName.equals("com.example.actms")) {
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

                if (!powerManager.isInteractive()) { // if screen is not already on, turn it on (get wake_lock for 10 seconds)
                    PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
                    wl.acquire(10000);
                    PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
                    wl_cpu.acquire(10000);
                }

                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            "Coder", "DemoCode", NotificationManager.IMPORTANCE_MAX);
                    channel.enableVibration(true);

                    channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                    mNotifyMgr.createNotificationChannel(channel);
                }
//                PendingIn
//                tent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, menuactivity2.class), PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    builder = new NotificationCompat.Builder(context, "Coder");
                } else {
                    builder = new NotificationCompat.Builder(context);
                }

                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(intent.getStringExtra("title"))
                        .setContentText(intent.getStringExtra("message"))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(intent.getStringExtra("message"))
                        )
                        .setWhen(System.currentTimeMillis())
//                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setOngoing(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);
                mNotifyMgr.notify(intent.getIntExtra("id", 0), builder.build());
                globalVariable.writelog("BroadCastMessage");
//                Log.d("Service" , "Message");
//            }
//                Intent uiintent = new Intent("changui");
//
//                localBroadcastManager.sendBroadcast(uiintent);
            }catch (Exception e){
                e.printStackTrace();
                globalVariable.writeexception(e);
            }
        }

    }
}