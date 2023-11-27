package com.example.actms;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class menuactivity extends AppCompatActivity {
    ImageButton logout;//,monitor,search,analyze,identity,manage,remote;
    View[] menu_button;
    TextView versiontv;
    GlobalVariable globalVariable;
    private SignalRService mService;
    private UIBroadcastReceiver uibroadcastReceiver ;
    Calendar calendar;
    httpthread httpthread;
    ConstraintLayout constraintLayout;
    Handler handler=new Handler();
    String id="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        globalVariable=(GlobalVariable)getApplicationContext();
//        versiontv=(TextView)findViewById(R.id.versiontv);
        httpthread=new httpthread(this);


        //        connectim=(ImageView) findViewById(R.id.connect);
//        connectim.setImageDrawable(getDrawable(R.drawable.unavailablecloud));
//        versiontv.setText("Ver "+globalVariable.getVersion());
//        uibroadcastReceiver = new UIBroadcastReceiver() ;
//        mService=new SignalRService();
        globalVariable.writelog("menuactivity onCreate");
//        handler.postDelayed(MainTime, 10000);
        calendar = Calendar.getInstance();
//        Intent signalservice=new Intent(getApplicationContext(), mService.getClass());
//        signalservice.putExtra("type","start");
//        if (!isMyServiceRunning(new SignalRService().getClass())) {
//            startService(signalservice);
//
//        }
        //installapk();
    }

    public void initview(){
        constraintLayout=(ConstraintLayout) findViewById(R.id.menucl);
        constraintLayout.removeAllViews();
        try {
            menu_button = new View[globalVariable.getSelfinction().size()];
            for (int i = 0; i < globalVariable.getSelfinction().size(); i++) {
                menu_button[i] = getLayoutInflater().inflate(R.layout.menu_button, null);
                menu_button[i].setId(i + 100);
                constraintLayout.addView(menu_button[i]);


            }
            for (int i = 0; i < globalVariable.getSelfinction().size(); i++) {

                ConstraintLayout.LayoutParams LayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                if ((i / 3) == 0) {
                    LayoutParams.topToTop = constraintLayout.getId();
                    LayoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.dp_30);

                } else {
                    if ((i - 3) >= 0) {
                        LayoutParams.topToBottom = menu_button[i - 3].getId();
                        LayoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.dp_30);
                    }
                }

                if ((i % 3) == 0) {
                    LayoutParams.leftToLeft = constraintLayout.getId();
                    if ((i + 1) < globalVariable.getSelfinction().size())
                        LayoutParams.rightToLeft = menu_button[i + 1].getId();
                    else
                        LayoutParams.rightToRight = constraintLayout.getId();
                } else if ((i % 3) == 2) {
                    LayoutParams.leftToRight = menu_button[i - 1].getId();
                    LayoutParams.rightToRight = constraintLayout.getId();
                } else {
                    LayoutParams.leftToRight = menu_button[i - 1].getId();
                    if ((i + 1) < globalVariable.getSelfinction().size())
                        LayoutParams.rightToLeft = menu_button[i + 1].getId();
                    else
                        LayoutParams.rightToRight = constraintLayout.getId();
                }
                menu_button[i].setLayoutParams(LayoutParams);
                switch (globalVariable.getSelfinction().get(i)) {
                    case "monitor":
                        ImageView monitorimageView = menu_button[i].findViewById(R.id.im);
                        monitorimageView.setBackground(getDrawable(R.drawable.monitor));
                        TextView monitortextView = menu_button[i].findViewById(R.id.tv);
                        monitortextView.setText(getResources().getString(R.string.Monitor));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 monitor");
                                Intent intent = new Intent();
                                intent.setClass(menuactivity.this, monitoractivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "history":
                        ImageView historyimageView = menu_button[i].findViewById(R.id.im);
                        historyimageView.setBackground(getDrawable(R.drawable.search));
                        TextView historytextView = menu_button[i].findViewById(R.id.tv);
                        historytextView.setText(getResources().getString(R.string.Search));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 history");
                                Intent intent = new Intent();
                                intent.setClass(menuactivity.this, searchactivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "data-analysis":
                        ImageView analysisimageView = menu_button[i].findViewById(R.id.im);
                        analysisimageView.setBackground(getDrawable(R.drawable.report));
                        TextView analysistextView = menu_button[i].findViewById(R.id.tv);
                        analysistextView.setText(getResources().getString(R.string.Analyze));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 data-analysis");
                                Intent intent = new Intent();
                                intent.setClass(menuactivity.this, analyzeactivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "manage":
                        ImageView manageimageView = menu_button[i].findViewById(R.id.im);
                        manageimageView.setBackground(getDrawable(R.drawable.manage));
                        TextView managetextView = menu_button[i].findViewById(R.id.tv);
                        managetextView.setText(getResources().getString(R.string.Manage));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 manage");
                                PopupMenu popupMenu = new PopupMenu(menuactivity.this, arg0);
                                MenuInflater inflater = popupMenu.getMenuInflater();   //產生一個 MenuInflater物件
                                inflater.inflate(R.menu.manage_menu, popupMenu.getMenu());  //解析 menu 選單檔
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.m1:
                                                globalVariable.writelog("按 country");
                                                Intent Countryintent = new Intent();
                                                Countryintent.setClass(menuactivity.this, managemain.class);
                                                Countryintent.putExtra("title", 1);
                                                startActivity(Countryintent);
                                                break;
                                            case R.id.m2:
                                                globalVariable.writelog("按 city");
                                                Intent Cityintent = new Intent();
                                                Cityintent.setClass(menuactivity.this, managemain.class);
                                                Cityintent.putExtra("title", 2);
                                                startActivity(Cityintent);
                                                break;
                                            case R.id.m3:
                                                globalVariable.writelog("按 site");
                                                Intent Siteintent = new Intent();
                                                Siteintent.setClass(menuactivity.this, managemain.class);
                                                Siteintent.putExtra("title", 3);
                                                startActivity(Siteintent);
                                                break;
                                            case R.id.m4:
                                                globalVariable.writelog("按 zone");
                                                Intent Zoneintent = new Intent();
                                                Zoneintent.setClass(menuactivity.this, managemain.class);
                                                Zoneintent.putExtra("title", 4);
                                                startActivity(Zoneintent);
                                                break;
                                            case R.id.m5:
                                                globalVariable.writelog("按 line");
                                                Intent Lineintent = new Intent();
                                                Lineintent.setClass(menuactivity.this, managemain.class);
                                                Lineintent.putExtra("title", 5);
                                                startActivity(Lineintent);
                                                break;
                                            case R.id.m6:
                                                globalVariable.writelog("按 station");
                                                Intent Stationintent = new Intent();
                                                Stationintent.setClass(menuactivity.this, managemain.class);
                                                Stationintent.putExtra("title", 6);
                                                startActivity(Stationintent);
                                                break;
                                            case R.id.m7:
                                                globalVariable.writelog("按 ipc");
                                                Intent IPCintent = new Intent();
                                                IPCintent.setClass(menuactivity.this, managemain.class);
                                                IPCintent.putExtra("title", 7);
                                                startActivity(IPCintent);
                                                break;
                                            default:
                                                break;
                                        }
                                        return true;
                                    }
                                });
                                popupMenu.show();   //為菜單頭設置標題
                            }
                        });
                        break;
                    case "identity":
                        ImageView identityimageView = menu_button[i].findViewById(R.id.im);
                        identityimageView.setBackground(getDrawable(R.drawable.permission));
                        TextView identitytextView = menu_button[i].findViewById(R.id.tv);
                        identitytextView.setText(getResources().getString(R.string.Identity));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 identity");
                                PopupMenu popupMenu = new PopupMenu(menuactivity.this, arg0);
                                MenuInflater inflater = popupMenu.getMenuInflater();   //產生一個 MenuInflater物件
                                inflater.inflate(R.menu.id_menu, popupMenu.getMenu());  //解析 menu 選單檔
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.m1:
                                                globalVariable.writelog("按 user");
                                                Intent userintent = new Intent();
                                                userintent.setClass(menuactivity.this, usermain.class);
                                                startActivity(userintent);
                                                break;
                                            case R.id.m2:
                                                globalVariable.writelog("按 role");
                                                Intent roleintent = new Intent();
                                                roleintent.setClass(menuactivity.this, rolemain.class);
                                                startActivity(roleintent);
                                                break;
                                        }
                                        return true;
                                    }
                                });
                                popupMenu.show();   //為菜單頭設置標題
                            }
                        });
                        break;
                    case "remote":
                        ImageView remoteimageView = menu_button[i].findViewById(R.id.im);
                        remoteimageView.setBackground(getDrawable(R.drawable.remote));
                        TextView remotetextView = menu_button[i].findViewById(R.id.tv);
                        remotetextView.setText(getResources().getString(R.string.Remote));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 remote");
                                Intent intent = new Intent();
                                intent.setClass(menuactivity.this, remotemain.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case "schedule":
                        ImageView scheduleimageView = menu_button[i].findViewById(R.id.im);
                        scheduleimageView.setBackground(getDrawable(R.drawable.schedule));
                        TextView scheduletextView = menu_button[i].findViewById(R.id.tv);
                        scheduletextView.setText(getResources().getString(R.string.Remote));
                        menu_button[i].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                globalVariable.writelog("按 schedule");
                                Intent intent = new Intent();
                                intent.setClass(menuactivity.this, taskmain.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    default:
                        break;
                }

            }
            logout = new ImageButton(this);
            ConstraintLayout.LayoutParams logoutLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            logoutLayoutParams.rightToRight = constraintLayout.getId();
            logoutLayoutParams.bottomToBottom = constraintLayout.getId();
            logoutLayoutParams.width = getResources().getDimensionPixelSize(R.dimen.dp_48);
            logoutLayoutParams.height = getResources().getDimensionPixelSize(R.dimen.dp_48);
            logout.setBackground(getDrawable(R.drawable.logout));
            constraintLayout.addView(logout, logoutLayoutParams);
            logout.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    globalVariable.writelog("按 logout");
                    handler.removeCallbacks(MainTime);
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                Log.d("kevin ", "Service status Running");
                return true;
            }
        }
        Log.d("kevin ", "Service status Not running");
//        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin","menuactivity onStart ");
        globalVariable.writelog("menuactivity onStart");
        globalVariable.setHttpreturn(false);
        httpthread.myinfo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!globalVariable.isHttpreturn()) ;
                runOnUiThread(new Runnable() {
                    public void run() {
                        initview();
                    }
                });
            }
        });
        a.run();



    }
    Runnable MainTime = new Runnable() {
        @Override
        public void run() {
            try{
                globalVariable.setHttpnotireturn(false);
                httpthread.getnotification();
                while (!globalVariable.isHttpnotireturn()) ;
                if(!id.equals(globalVariable.getNotificationlist().get(0).get("id"))){
                    id=globalVariable.getNotificationlist().get(0).get("id");

                    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdFormat.parse(globalVariable.getNotificationlist().get(0).get("createTime")));
                    if (cal.after(calendar)){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                globalVariable.writelog("menuactivity Notification");
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("MessageAction");
                                broadcastIntent.setClass(globalVariable, BroadCastMessage.class);

//                            broadcastIntent.putExtra("message","123\n456\n788\n");
                                broadcastIntent.putExtra("message",globalVariable.getNotificationlist().get(0).get("content"));
                                broadcastIntent.putExtra("title",globalVariable.getNotificationlist().get(0).get("title"));
                                broadcastIntent.putExtra("id",Integer.valueOf(id));
                                globalVariable.sendBroadcast(broadcastIntent);

                            }
                        });
                    }


            }
//                    Thread.sleep(100000);
            }catch (Exception e){
                e.printStackTrace();
            }
            handler.postDelayed(MainTime, 10000);
        }
    };
    public void installapk(String path){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            File file=new File("/storage/emulated/0/app-debug1.apk");
            if(file.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Log.d("kevin", "menuactivity installapk >=N");
                    String packageName = getApplicationContext().getPackageName();
                    String authority = new StringBuilder(packageName).append(".provider").toString();
                    Uri uri = FileProvider.getUriForFile(this, authority, file);
                    Log.d("kevin", "uri:"+uri);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");

                } else {
                    Log.d("kevin", "menuactivity installapk <N");
                    Uri uri = Uri.fromFile(file);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                }
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class UIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() ;
            Log.d("kevin","UIBroadcastReceiver");
            if ( "changui".equals( action )){
                //arrayAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            globalVariable.writelog("menuactivity KEYCODE_BACK");
            new AlertDialog.Builder(menuactivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    globalVariable.writelog("menuactivity KEYCODE_BACK 確定");
                                    finish();
                                    Loginactivity.loginfa.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    globalVariable.writelog("menuactivity KEYCODE_BACK 取消");
                                }
                            }).show();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kevin","menuactivity onStop ");
        globalVariable.writelog("menuactivity onStop");
    }
}
