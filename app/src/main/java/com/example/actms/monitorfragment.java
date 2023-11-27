package com.example.actms;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class monitorfragment extends Fragment {
    TextView monitormessage;
    RelativeLayout monitorrl;
    View [] pccard=new View[10];
    ProgressBar[] progressBar;
    TextView[] progressText;
    TextView[] pcname;
    TextView[] pcinfo;
    int count=0;
    Handler handler=new Handler();
    FloatingActionButton filterbt,addbt;
    View monitorview;
    DBHelper DH=null;
    httpthread httpthread;
    GlobalVariable globalVariable;
    ProgressBar loadingbar;
    String atsid="";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("kevin","monitorfragment onCreate");
        globalVariable=(GlobalVariable)getContext().getApplicationContext();
        httpthread=new httpthread(getContext());
        DH=new DBHelper(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        Log.d("kevin","monitorfragment onCreateView");
        monitorview = inflater.inflate(R.layout.monitor_main,container,false);
        loadingbar=(ProgressBar) monitorview.findViewById(R.id.progressBar_Spinner);
        loadingbar.setVisibility(View.INVISIBLE);
        monitormessage=(TextView)monitorview.findViewById(R.id.monitormessage);
        monitormessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        monitormessage.setText("這裡是訊息\n123\n456\n789這裡是訊息\n123\n456\n789");
        monitorrl=(RelativeLayout) monitorview.findViewById(R.id.monitorrl);

//        FloatingActionMenu menu = monitorview.findViewById(R.id.floatingActionMenu);
//        menu.setClosedOnTouchOutside(true);
//        filterbt=(FloatingActionButton)monitorview.findViewById(R.id.btfilter);
//        addbt=(FloatingActionButton)monitorview.findViewById(R.id.btadd);
////        filterbt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
////                View filterview = getActivity().getLayoutInflater().inflate(R.layout.monitorfilterview,null);
////
////                alertDialog.setView(filterview);
////                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));
////                alertDialog.setNegativeButton("清除",(((dialog, which) -> {})));
////
////                AlertDialog dialog = alertDialog.create();
////                dialog.show();
////                Spinner spinner =filterview.findViewById(R.id.countrysp);
////                ArrayAdapter<CharSequence> comList = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
////                        R.array.list,   R.layout.myspinner);
////                comList.setDropDownViewResource(  R.layout.myspinner);
////                spinner.setAdapter(comList);
////                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_6));
////                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
////                    dialog.dismiss();
////                }));
////                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_6));
////                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
////                    dialog.dismiss();
////                }));
////            }
////        });
//        addbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.addpccard), Toast.LENGTH_SHORT).show();
//            }
//        });
        count=0;
        globalVariable.setHttpreturn(false);
        loadingbar.setVisibility(View.VISIBLE);
        httpthread.pcinfopage();
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        initview("");
                        loadingbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        a.run();
        return monitorview;
    }
    public void initview(String filter){
        pccard=new View[globalVariable.getPccount()];
        progressBar=new ProgressBar[globalVariable.getPccount()];
        progressText=new TextView[globalVariable.getPccount()];
        pcname=new TextView[globalVariable.getPccount()];
        pcinfo=new TextView[globalVariable.getPccount()];
        Log.d("kevin","getPcinfo "+globalVariable.getPccount());
        List<HashMap<String, String>> pcinfoList=new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = DH.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + "pcinfo"+filter, null);
        while (c.moveToNext()) {
//            Log.d("kevin","db:"+c.getString(2));
            HashMap<String, String> pc = new HashMap<String, String>();
            pc.put("pc_name",c.getString(6));
            pc.put("ats_id",c.getString(8));
            pc.put("online_status",c.getString(7));
            pc.put("last_tp_name",c.getString(1));
            pc.put("last_end_time",c.getString(2));
            pc.put("tp_status",c.getString(5));
            pc.put("tp_result",c.getString(4));
            pc.put("tp_progress",c.getString(3));
            Log.d("kevin","pc:"+pc);
            pcinfoList.add(pc);
        }
        for(int i=0;i<globalVariable.getPccount();i++) {
            int finalI = i;
            pccard[i]=new View(getActivity().getApplicationContext());
            pcname[i]=new TextView(getActivity().getApplicationContext());
            pcinfo[i]=new TextView(getActivity().getApplicationContext());
            progressBar[i]=new ProgressBar(getActivity().getApplicationContext());
            progressText[i]=new TextView(getActivity().getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            if(i!=0) {
                lp.addRule(RelativeLayout.BELOW, pccard[i - 1].getId());
                lp.topMargin =50;
            }
            else {
                lp.topMargin = 10;
            }
            lp.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
//            pccard[i].setBackground(getResources().getDrawable(R.drawable.overview));
            LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pccard[i]=inflater.inflate(R.layout.pccard , null, true);
            pccard[i].setLayoutParams(lp);
            registerForContextMenu(pccard[i]);
            pccard[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    atsid=pcinfoList.get(finalI).get("ats_id");
                    return false;
                }
            });
//            pccard[i].setOnClickListener(new Button.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity().getApplicationContext(), TPactivity.class);
//                    intent.putExtra("ats_id",pcinfoList.get(finalI).get("ats_id"));
//                    startActivity(intent);
//                }
//            });
            monitorrl.addView(pccard[i]);
//            Log.d("kevin","monitorfragment initview"+i);
            pccard[i].setId(2000+i);
            progressBar[i] = pccard[i].findViewById(R.id.progressBar);
            progressText[i] = pccard[i].findViewById(R.id.progress_text);
            pcname[i] = pccard[i].findViewById(R.id.pccardname);
            pcinfo[i] = pccard[i].findViewById(R.id.pccardinfo);
            pcname[i].setText(pcinfoList.get(i).get("pc_name"));
            pcinfo[i].setText("RUN\n"+pcinfoList.get(i).get("online_status")+"\nLast End-Time:"+pcinfoList.get(i).get("last_end_time")+"\nLast TP Name:"+pcinfoList.get(i).get("last_tp_name"));
            progressText[i].setText(pcinfoList.get(i).get("tp_progress")+"%");

            progressBar[i].post(new Runnable() {
                @Override
                public void run() {
                    progressBar[finalI].setProgress(Math.round(Float.valueOf(pcinfoList.get(finalI).get("tp_progress"))));
                }
            });
//            Log.d("kevin","progressBar "+i+":"+progressBar[i].getProgress());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("kevin","monitorfragment onDestroy");
        monitorrl.removeAllViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("kevin","monitorfragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //monitorrl.removeAllViews();
        Log.d("kevin","monitorfragment onStop");

    }
    Runnable MainTime = new Runnable() {
        @Override
        public void run() {
            try {
                //Log.d("kevin","monitorfragment MainTime"+count);
                if (count < 100){
                    count++;
                    progressBar[0].setProgress(count);
                    progressText[0].setText(count + "%");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            handler.postDelayed(MainTime, 100);
        }};

    @Override
    public void onResume() {
        super.onResume();
        Log.d("kevin","monitorfragment onResume");
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(getActivity().getApplicationContext());   //產生一個 MenuInflater物件
        inflater.inflate(R.menu.cm_menu, menu);  //解析 menu 選單檔
        menu.setHeaderTitle("請選擇:");   //為菜單頭設置標題
    }
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m1:
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), TPactivity.class);
                intent.putExtra("ats_id",atsid);
                startActivity(intent);
                break;
            case R.id.m2:
//                finish();
        }
        return super.onContextItemSelected(item);
    }
}
