package com.example.actms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class monitoractivity extends AppCompatActivity {
    TextView monitormessage;
    RelativeLayout monitorrl;
    TextView noinfo;
    DBHelper DH=null;
    Handler handler=new Handler();
    String sel="";
//    FloatingActionButton filterbt,addbt;
    httpthread httpthread;
    GlobalVariable globalVariable;
    ProgressBar loadingbar;
    String atsid="";
    String pcname="";
    private LocalBroadcastManager localBroadcastManager ;
    private UIBroadcastReceiver uibroadcastReceiver ;
    private IntentFilter intentFilter ;
    RecyclerView mRecyclerView;
    MyPCListAdapter mypcListAdapter;
//    List<String> vidlist=new ArrayList<String>();
    List<HashMap<String, String>> pcinfoList=new ArrayList<HashMap<String, String>>();
    boolean frist=false;
    boolean island=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kevin","monitoractivity onCreate");
        setContentView(R.layout.monitor_main);
        Configuration configuration = getResources().getConfiguration();
        if (savedInstanceState != null && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            island=true;
            Log.d("kevin","monitoractivity island "+island);
        }else if (savedInstanceState != null && configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            island=false;
            Log.d("kevin","monitoractivity island "+island);
        }
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("monitoractivity onCreate");
        monitormessage=(TextView)findViewById(R.id.monitormessage);
        mRecyclerView=(RecyclerView)findViewById(R.id.pccardrcv);
        try {
            if (island)
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            else
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mypcListAdapter = new MyPCListAdapter();
            mRecyclerView.setAdapter(mypcListAdapter);
            noinfo = (TextView) findViewById(R.id.tvnoinfo);
            noinfo.setText("");
            loadingbar = (ProgressBar) findViewById(R.id.progressBar_Spinner);
            loadingbar.setVisibility(View.INVISIBLE);
            monitormessage.setMovementMethod(ScrollingMovementMethod.getInstance());
//            monitormessage.setText("這裡是訊息\n123\n456\n789這裡是訊息\n123\n456\n789");
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        monitorrl=(RelativeLayout) findViewById(R.id.monitorrl);
//        filterbt=(FloatingActionButton)findViewById(R.id.btfilter);
//        addbt=(FloatingActionButton)findViewById(R.id.btadd);
        httpthread=new httpthread(this);
//        uibroadcastReceiver = new UIBroadcastReceiver() ;
//        intentFilter = new IntentFilter( "changui") ;
//        localBroadcastManager.registerReceiver( uibroadcastReceiver , intentFilter );
        DH=new DBHelper(this);
//        filterbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(monitoractivity.this);
//                View filterview = getLayoutInflater().inflate(R.layout.monitorfilterview,null);
//
//                alertDialog.setView(filterview);
//                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));
//                alertDialog.setNegativeButton("清除",(((dialog, which) -> {})));
//                AlertDialog dialog = alertDialog.create();
//                dialog.show();
//
//                Spinner spinner =filterview.findViewById(R.id.countrysp);
//
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(monitoractivity.this, R.layout.myspinner,wifissid) {
////
////                    @Override
////                    public View getView(int position, View convertView, ViewGroup parent) {
////
////                        View v = super.getView(position, convertView, parent);
////                        if (position == getCount()) {
////                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
////                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
////                        }
////                        return v;
////                    }
////                    @Override
////                    public int getCount() {
////                        return super.getCount()-1; // you dont display last item. It is used as hint.
////                    }
////                };
////                adapter.setDropDownViewResource(R.layout.myspinner);
////                Log.d("kevin","sel "+sel);
////                Log.d("kevin","adapter "+adapter.getCount()+";"+wifissid.size());
////                spinner.setAdapter(adapter);
////                if(sel.equals("")) {
////                    if((adapter.getCount()+1)==number)
////                        adapter.add(getString(R.string.Select));
////                    spinner.setAdapter(adapter);
////                    spinner.setSelection(adapter.getCount());
////                }else{
////
////                    spinner.setSelection(adapter.getPosition(sel));
////                }
//
//
//
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
//                    dialog.dismiss();
//                    sel=(String) spinner.getSelectedItem();
//                }));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
//                    dialog.dismiss();
////                    spinner.setSelection(adapter.getCount());
//                    sel="";
//                }));
//
//            }
//        });
//        addbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                globalVariable.writelog("monitoractivity addbt");
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(monitoractivity.this);
//                View filterview = getLayoutInflater().inflate(R.layout.monitoraddview,null);
//
//                alertDialog.setView(filterview);
//                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));
//                alertDialog.setNegativeButton("取消",(((dialog, which) -> {})));
//                AlertDialog dialog = alertDialog.create();
//                dialog.show();
//                EditText tooled =filterview.findViewById(R.id.tooled);
//                EditText configed =filterview.findViewById(R.id.configed);
//                EditText pcnameed =filterview.findViewById(R.id.pcnameed);
//                EditText iped =filterview.findViewById(R.id.iped);
//                EditText systypeed =filterview.findViewById(R.id.systypeed);
//                EditText maced =filterview.findViewById(R.id.maced);
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
//
//                    globalVariable.writelog("monitoractivity addbt ok");
//                    httpthread.adeq(tooled.getText().toString(),configed.getText().toString(),pcnameed.getText().toString(),iped.getText().toString(),systypeed.getText().toString(),maced.getText().toString());
//                    dialog.dismiss();
//                }));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
//                    dialog.dismiss();
////                    spinner.setSelection(adapter.getCount());
//                    sel="";
//                }));
//
//            }
//        });
        globalVariable.setHttpreturn(false);
        loadingbar.setVisibility(View.VISIBLE);
        httpthread.pcinfopage();
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            initview("");
                            loadingbar.setVisibility(View.INVISIBLE);
                        }catch (Exception e){
                            e.printStackTrace();
                            globalVariable.writeexception(e);
                        }
                    }
                });
            }
        });
        a.run();

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        FloatingActionMenu menu = findViewById(R.id.floatingActionMenu);
//
//        if(menu!=null) {
//            if (ev.getAction() == MotionEvent.ACTION_UP && menu.isOpened()) {
//                menu.close(true);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin","monitoractivity onStart");
        globalVariable.writelog("monitoractivity onStart");
//        Log.d("kevin",""+relativeLayout.getResources().getDisplayMetrics().widthPixels);


        handler.postDelayed(MainTime, 10000);

    }
    Runnable MainTime = new Runnable() {
        @Override
        public void run() {
            globalVariable.setHttpreturn(false);
            httpthread.pcinfopage();
            Thread a = new Thread(new Runnable(){
                @Override
                public void run() {
                    while(!globalVariable.isHttpreturn());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            initview("");

                        }
                    });
                }
            });
            a.run();
            handler.postDelayed(MainTime, 10000);
        }};
    public void initview(String filter){
        Log.d("kevin","getPcinfo "+globalVariable.getPccount());
        try {
            globalVariable.writelog("monitoractivity initview");
            pcinfoList.clear();
            SQLiteDatabase db = DH.getReadableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM " + "pcinfo" + filter + " ORDER BY id", null);
            while (c.moveToNext()) {
//            Log.d("kevin","db:"+c.getString(2));
                HashMap<String, String> pc = new HashMap<String, String>();
//                vidlist.add(c.getString(1));
//            pc.put("nVId",c.getString(1));
                pc.put("name", c.getString(2));
                pc.put("computerName", c.getString(3));
                pc.put("status", c.getString(4));
                pc.put("updateTime", c.getString(5));
                pc.put("LastTpName", c.getString(6));
                pc.put("LastTpresult", c.getString(7));
                pc.put("LastTpProgress", c.getString(8));
                Log.d("kevin", "pc:" + pc);
                pcinfoList.add(pc);
            }
            if (globalVariable.getPccount() == 0) {
                if (!frist) {
                    noinfo.setText("查無資料");
                    Log.d("kevin", "pc:查無資料");
                }
            } else {
                frist = true;
                noinfo.setText("");
                monitormessage.setText("IPC\n Online: "+globalVariable.getIpcOnlineQty()+"\n Offline: "+globalVariable.getIpcOfflineQty());
                monitormessage.append("\nTP\n Pass: "+globalVariable.getTpPassQty()+"\n Fail: "+globalVariable.getTpFailQty()+"\n Finished: "+globalVariable.getTpFinishedQty()+"\n Running: "+globalVariable.getTpRunningQty());
                mypcListAdapter.notifyDataSetChanged();
                Log.d("kevin", "myListAdapter " + pcinfoList.size());
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(this);   //產生一個 MenuInflater物件
        inflater.inflate(R.menu.cm_menu, menu);  //解析 menu 選單檔
        menu.setHeaderTitle("請選擇:");   //為菜單頭設置標題
    }
    public boolean onContextItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.m1:
                    globalVariable.writelog("monitoractivity to TP");
                    Intent intent = new Intent();
                    intent.setClass(monitoractivity.this, TPactivity.class);
                    intent.putExtra("pcname", pcname);
                    startActivity(intent);
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kevin","monitoractivity onStop");
        handler.removeCallbacks(MainTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("kevin","monitoractivity onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("kevin","monitoractivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("kevin","monitoractivity onPause");
    }
    private class UIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() ;
            Log.d("kevin","UIBroadcastReceiver");
            if ( "changui".equals( action )){
//                vidlist.indexOf("12");
            }
        }
    }
    private class MyPCListAdapter extends RecyclerView.Adapter<MyPCListAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView pccardname,progress_text,pccardinfo;
            private ProgressBar progressBar;
            private View mView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Log.d("kevin","ViewHolder");

                pccardname = itemView.findViewById(R.id.pccardname);
                pccardinfo = itemView.findViewById(R.id.pccardinfo);
                progressBar = itemView.findViewById(R.id.progressBar);
                progress_text = itemView.findViewById(R.id.progress_text);
                mView  = itemView;
            }
        }
        @NonNull
        @Override
        public MyPCListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pccard,parent,false);
            Log.d("kevin","onCreateViewHolder");
            return new MyPCListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyPCListAdapter.ViewHolder holder, int position) {
            Log.d("kevin", "onBindViewHolder");
            int finalI=position;
            try {
                MyPCListAdapter.ViewHolder finalholder=holder;
                holder.pccardname.setText(pcinfoList.get(position).get("name")+"\n"+pcinfoList.get(position).get("computerName"));
                SpannableString spannableString=new SpannableString(pcinfoList.get(position).get("status"));
                if(pcinfoList.get(position).get("status").equals("ONLINE"))
                    spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#008000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                else
                    spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#ff0000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.pccardinfo.setText(spannableString);
                holder.pccardinfo.append("\n" + pcinfoList.get(position).get("updateTime") + "\nTP Name:" + pcinfoList.get(position).get("LastTpName")+ "\nTP Run Status:" + pcinfoList.get(position).get("LastTpresult"));
                if (pcinfoList.get(position).get("LastTpProgress") != null)
                    if (!pcinfoList.get(position).get("LastTpProgress").equals("null")) {
                        holder.progress_text.setText(pcinfoList.get(position).get("LastTpProgress") + "%");
                        Log.d("kevin",pcinfoList.get(position).get("name")+":"+pcinfoList.get(position).get("LastTpProgress"));
                    }

                holder.progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pcinfoList.get(finalI).get("LastTpProgress") != null)
                            if (!pcinfoList.get(finalI).get("LastTpProgress").equals("null"))
                                finalholder.progressBar.setProgress(Math.round(Float.valueOf(pcinfoList.get(finalI).get("LastTpProgress"))));
                    }
                });
                registerForContextMenu(holder.mView);
                holder.mView.setOnTouchListener(new Button.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent motionEvent) {
                        try {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                                if (pcinfoList.get(finalI).get("computerName") != null) {
                                    pcname = pcinfoList.get(finalI).get("computerName");
                                } else {
                                    atsid = "";
                                    pcname = "";
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            globalVariable.writeexception(e);
                        }

                        return false;
                    }
                });
//            holder.mView.setOnClickListener((v)->{
//                if(pcinfoList.get(finalI).get("sConfig")!=null)
//                    atsid=pcinfoList.get(finalI).get("sConfig");
//                else
//                    atsid="";
////                Intent intent = new Intent();
////                intent.setClass(monitoractivity.this, TPactivity.class);
////                intent.putExtra("ats_id",atsid);
////                startActivity(intent);
//            });
            }catch (Exception e){
                e.printStackTrace();
                globalVariable.writeexception(e);
            }
        }

        @Override
        public int getItemCount() {
            return pcinfoList.size();
        }
    }
}