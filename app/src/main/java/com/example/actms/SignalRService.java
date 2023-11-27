package com.example.actms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

public class SignalRService extends Service {

    private HubConnection hubConnection;
//    List<String> messageList = new ArrayList<String>();
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    GlobalVariable globalVariable;
    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
        Log.d("kevin","service onCreate");
        globalVariable=(GlobalVariable) getApplication();

//        //startMyOwnForeground();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
//            startMyOwnForeground();
//        else
//            startForeground(1, new Notification());
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        Log.d("kevin", "YourService startMyOwnForeground");
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        Log.d("kevin","service onStartCommand");
        try {
            if (String.valueOf(intent.getStringExtra("type")).equals("send"))
                hubConnection.send("SendMessage", String.valueOf(intent.getStringExtra("user")), String.valueOf(intent.getStringExtra("message")));
            else if (String.valueOf(intent.getStringExtra("type")).equals("start")) {
                startSignalR();
                checklinkstatus checklinkstatus=new checklinkstatus();
                checklinkstatus.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onDestroy() {
        hubConnection.stop();
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction(Intent.ACTION_BOOT_COMPLETED);
//        broadcastIntent.setClass(this, bootreceiver.class);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        Log.d("kevin","service onBind");
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            Log.d("kevin","service LocalBinder");
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */
    public void sendMessage(String message) {
        Log.d("kevin","service sendMessage");
        hubConnection.send("SendMessage","kevin", message);
    }

    private void startSignalR() {
//        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        hubConnection = HubConnectionBuilder.create("https://localhost:7168/chatHub").build();
        //hubConnection.setKeepAliveInterval(1000);


        hubConnection.on("ReceiveMessage", (user,message)-> {
            Log.d("kevin",user+":"+message);
//            if(globalVariable.getSignalmessageList()!=null)
//                if(messageList.size()==0)
//                    messageList=globalVariable.getSignalmessageList();
//            globalVariable.setSignalmessageList(messageList);
//            messageList.add(user+":"+message);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("MessageAction");
            broadcastIntent.setClass(this, BroadCastMessage.class);
//            broadcastIntent.putExtra("user",user);
            broadcastIntent.putExtra("message",message);
            broadcastIntent.putExtra("title","!!");
            this.sendBroadcast(broadcastIntent);
        }, String.class,String.class);
        new HubConnectionTask().execute(hubConnection);

//        hubConnection.start().blockingAwait();

//        hubConnection.send("SendMessage","kevin", "test");
    }
    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("kevin","HubConnectionTask onPreExecute");

        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            Log.d("kevin","HubConnectionTask doInBackground");
            try {
                HubConnection hubConnection = hubConnections[0];
                hubConnection.start().blockingAwait();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("kevin","HubConnectionTask onPostExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d("kevin","HubConnectionTask onProgressUpdate");
        }
    }

    class checklinkstatus extends Thread{
        @Override
        public void run() {
            super.run();
            int count=0;
            while(true){
                try {
                    if (hubConnection != null) {
//                        Log.d("kevin", count+":" + hubConnection.getConnectionState());
                        count++;
                        if (count > 10){
                            count=0;
                            //Log.d("kevin", ""+(hubConnection.getConnectionState()== HubConnectionState.DISCONNECTED));
                            if(hubConnection.getConnectionState()== HubConnectionState.DISCONNECTED) {
                                //hubConnection = HubConnectionBuilder.create("https://localhost:7168/chatHub").build();
                                startSignalR();
                                Log.d("kevin", "reconnect");
                            }
                        }

                    }
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
