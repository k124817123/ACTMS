package com.example.actms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class bootreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d("kevin","boot");
            Intent signalservice=new Intent(context, new SignalRService().getClass());
            signalservice.putExtra("type","start");
            try{
                Thread.sleep(500);
            }catch (Exception e){e.printStackTrace();}
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(signalservice);
            } else {
//                context.startService(signalservice);
            }

        }
    }
}