package com.example.actms;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class managemain extends AppCompatActivity {


    httpthread httpthread;
    GlobalVariable globalVariable;
    ImageButton create,export,filter;
    TableLayout tableLayout,tableLayouthead;
    List<String> csvList=new ArrayList<String>();
    int title=0;
    TextView titletv;
    int filterspid=0;
    Spinner sppage;
    String name="";
    String page="";
    boolean issearch=false;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_main);
        globalVariable=(GlobalVariable) getApplication();

        titletv=(TextView) findViewById(R.id.managetitle);
        httpthread=new httpthread(this);
        try{
        Intent intent = getIntent();
        title=intent.getIntExtra("title",0);
        titletv.setText("Manage - "+globalVariable.getScope()[title-1]);
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        globalVariable.writelog("managemain onCreate "+title);
        globalVariable.setHttpreturn(false);
        sppage=(Spinner)findViewById(R.id.sppage);
        sppage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                page=sppage.getSelectedItem().toString();
                globalVariable.writelog("managemain sppage "+page);
//                globalVariable.setNowpage(position+1);
                if(!issearch) {
                    globalVariable.setHttpreturn(false);

                    initviewhead();
                    switch (title){
                        case 1:
                            filter.setVisibility(View.INVISIBLE);
                            httpthread.getcountries(page,"");

                            break;
                        case 2:
                            httpthread.getcites(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        case 3:
                            httpthread.getsites(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        case 4:
                            httpthread.getzones(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        case 5:
                            httpthread.getlines(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        case 6:
                            httpthread.getstations(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        case 7:
                            httpthread.getipcs(globalVariable.getLevelname().get(filterspid).get("id"),name,page,"");

                            break;
                        default:break;
                    }
                    Thread a1 = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        switch (title) {
                                            case 1:
                                                createview(globalVariable.getCountrylist());
                                                break;
                                            case 2:
                                                createview(globalVariable.getCitylist());
                                                break;
                                            case 3:
                                                createview(globalVariable.getSitelist());
                                                break;
                                            case 4:
                                                createview(globalVariable.getZonelist());
                                                break;
                                            case 5:
                                                createview(globalVariable.getLinelist());
                                                break;
                                            case 6:
                                                createview(globalVariable.getStationlist());
                                                break;
                                            case 7:
                                                createview(globalVariable.getIpclist());
                                                break;
                                            default:
                                                break;
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        globalVariable.writeexception(e);
                                    }
                                }
                            });
                        }
                    });
                    a1.run();
                }
                issearch=false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tableLayout=(TableLayout) findViewById(R.id.tablelayout);
        tableLayouthead=(TableLayout) findViewById(R.id.tablelayouthead);
        create=(ImageButton) findViewById(R.id.createbt);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.writelog("managemain create");
                Intent intent = new Intent();
                intent.setClass(managemain.this, manageinfo.class);
                intent.putExtra("title",title);
                intent.putExtra("id","");
                startActivity(intent);
            }
        });
        export=(ImageButton) findViewById(R.id.exportbt);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    globalVariable.writelog("managemain export");
                    globalVariable.setHttpreturn(false);
                    switch (title){
                        case 1:
                            filter.setVisibility(View.INVISIBLE);
                            httpthread.getcountries("",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 2:
                            httpthread.getcites("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 3:
                            httpthread.getsites("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 4:
                            httpthread.getzones("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 5:
                            httpthread.getlines("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 6:
                            httpthread.getstations("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        case 7:
                            httpthread.getipcs("","","",String.valueOf(globalVariable.getTotalcount()));

                            break;
                        default:break;
                    }
                    csvList.clear();
                    Thread a1 = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try{
                                        String csv = "Id";
                                        for (int i = 0; i < title - 1; i++) {
                                            csv += "," + globalVariable.getScope()[i];
                                        }
                                        if (title == 7) {
                                            csv += ",Name,IP,MAC,Description,Create,Update\n";
                                        }
                                        else{
                                            csv += ",Name,Description,Create,Update\n";
                                        }

                                        csvList.add(csv);
                                        switch (title) {
                                            case 1:
                                                insertcsv(globalVariable.getCountrylist());
                                                break;
                                            case 2:
                                                insertcsv(globalVariable.getCitylist());
                                                break;
                                            case 3:
                                                insertcsv(globalVariable.getSitelist());
                                                break;
                                            case 4:
                                                insertcsv(globalVariable.getZonelist());
                                                break;
                                            case 5:
                                                insertcsv(globalVariable.getLinelist());
                                                break;
                                            case 6:
                                                insertcsv(globalVariable.getStationlist());
                                                break;
                                            case 7:
                                                insertcsv(globalVariable.getIpclist());
                                                break;
                                            default:
                                                break;
                                        }

                                        Date current = new Date();
                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
                                        String filename=dateformat.format(current)+"_manage_"+globalVariable.getScope()[title-1]+".csv";

                                        File csvfile=new File(Environment.getExternalStorageDirectory()+"/ACTMS/",filename);
                                        Log.d("kevin","csvfile:"+csvfile.getAbsolutePath());
                                        if(!csvfile.exists()) {
                                            csvfile.getParentFile().mkdirs();
                                            csvfile.createNewFile();
                                        }
                                        FileOutputStream fos = new FileOutputStream(csvfile,true);

                                        for(int i=0;i<csvList.size();i++){
                                            fos.write(csvList.get(i).getBytes());
                                        }
                                        fos.close();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        globalVariable.writeexception(e);
                                    }
                                }
                            });
                        }
                    });
                    a1.run();


                }catch (Exception e){
                    e.printStackTrace();
                    globalVariable.writeexception(e);
                }
            }
        });
        filter=(ImageButton) findViewById(R.id.filterbt);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.setHttpreturn(false);
                globalVariable.writelog("managemain filter");
                httpthread.getname(globalVariable.getScope()[title-2]);
                Thread a = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!globalVariable.isHttpreturn()) ;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(managemain.this);
                                View filterview = getLayoutInflater().inflate(R.layout.managefilter,null);

                                alertDialog.setView(filterview);
                                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));
                                alertDialog.setNegativeButton("取消",(((dialog, which) -> {})));
                                AlertDialog dialog = alertDialog.create();
                                dialog.show();
                                TextView filter =filterview.findViewById(R.id.filtertv);
                                Spinner filtersp =filterview.findViewById(R.id.filtersp);
                                EditText nameed =filterview.findViewById(R.id.nameed);

                                filter.setText(globalVariable.getScope()[title-2]);
                                MySpinnerAdapter myAdapter =new MySpinnerAdapter(globalVariable.getLevelname(),"fullName",managemain.this,1);
                                filtersp.setAdapter(myAdapter);
                                filtersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        filterspid=position;

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
                                    name=nameed.getText().toString();
                                    setview(globalVariable.getLevelname().get(filterspid).get("id"),name);
                                    dialog.dismiss();

                                }));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
                                    dialog.dismiss();
//                    spinner.setSelection(adapter.getCount());

                                }));
                            }
                        });
                    }
                });
                a.run();


            }
        });
    }

    public void insertcsv(List<HashMap<String, String>> list){
        String csv="";
        for (int i = 0; i < list.size(); i++) {
            csv = list.get(i).get("id");

            for (int j = 0; j < title - 1; j++) {
                csv += "," + list.get(i).get(globalVariable.getScopename()[j]);
            }
            if (title == 7) {
                csv += "," +list.get(i).get("name") + "," + list.get(i).get("ip") + "," + list.get(i).get("macAddress") + "," + list.get(i).get("description") + "," + list.get(i).get("createTime") + "," + list.get(i).get("updateTime") + "\n";
            } else
                csv += ","+list.get(i).get("name") + "," + list.get(i).get("description") + "," + list.get(i).get("createTime") + "," + list.get(i).get("updateTime") + "\n";
            csvList.add(csv);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        globalVariable.writelog("managemain onStart");
        setview("","");
        try {
            mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scale = 1 - detector.getScaleFactor();

                    float prevScale = mScale;
                    mScale += scale;

                    if (mScale < 0.1f) // Minimum scale condition:
                        mScale = 0.1f;

                    if (mScale > 10f) // Maximum scale condition:
                        mScale = 10f;
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, 0, 0);
                    scaleAnimation.setDuration(0);
                    scaleAnimation.setFillAfter(true);
//                ScrollView layout =(ScrollView) findViewById(R.id.scrollViewZoom);
                    tableLayout.startAnimation(scaleAnimation);
                    tableLayouthead.startAnimation(scaleAnimation);

//                tableLayout.setLayoutParams(horizontalScrollView.getLayoutParams());
                    return true;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);

        return mScaleDetector.onTouchEvent(event);
    }
    public void setview(String id,String name){
        globalVariable.writelog("managemain setview");
        issearch=true;

        initviewhead();
        globalVariable.setHttpreturn(false);
        switch (title){
            case 1:
                filter.setVisibility(View.INVISIBLE);
                httpthread.getcountries("","");

                break;
            case 2:
                httpthread.getcites(id,name,"","");

                break;
            case 3:
                httpthread.getsites(id,name,"","");

                break;
            case 4:
                httpthread.getzones(id,name,"","");

                break;
            case 5:
                httpthread.getlines(id,name,"","");

                break;
            case 6:
                httpthread.getstations(id,name,"","");

                break;
            case 7:
                httpthread.getipcs(id,name,"","");

                break;
            default:break;
        }
        Thread a1 = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            switch (title) {
                                case 1:
                                    createview(globalVariable.getCountrylist());
                                    break;
                                case 2:
                                    createview(globalVariable.getCitylist());
                                    break;
                                case 3:
                                    createview(globalVariable.getSitelist());
                                    break;
                                case 4:
                                    createview(globalVariable.getZonelist());
                                    break;
                                case 5:
                                    createview(globalVariable.getLinelist());
                                    break;
                                case 6:
                                    createview(globalVariable.getStationlist());
                                    break;
                                case 7:
                                    createview(globalVariable.getIpclist());
                                    break;
                                default:
                                    break;
                            }
                            MySpinnerAdapter myAdapter =new MySpinnerAdapter(globalVariable.getPagelist(),managemain.this,0);
                            sppage.setAdapter(myAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                            globalVariable.writeexception(e);
                        }
                    }
                });
            }
        });
        a1.run();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("kevin","usermain onDestroy");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("kevin","usermain onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //monitorrl.removeAllViews();
        Log.d("kevin","usermain onStop");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("kevin","usermain onResume");
    }
    public void initviewhead(){
        try {
            globalVariable.writelog("managemain initviewhead");
            tableLayout.removeAllViews();
            tableLayouthead.removeAllViews();
            TableRow row = new TableRow(this);
            TableRow rowhead = new TableRow(this);

            row.addView(puttpdataintv("Id"));
            rowhead.addView(puttpdataintv("Id"));
            row.addView(puttpdataintv("Name"));
            rowhead.addView(puttpdataintv("Name"));
            if (title == 7) {
                row.addView(puttpdataintv("IP"));
                rowhead.addView(puttpdataintv("IP"));
                row.addView(puttpdataintv("MAC"));
                rowhead.addView(puttpdataintv("MAC"));
            }
            row.addView(puttpdataintv("Description"));
            row.addView(puttpdataintv("Create"));
            row.addView(puttpdataintv("Update"));
            row.addView(puttpdataintv("Action"));
            rowhead.addView(puttpdataintv("Description"));
            rowhead.addView(puttpdataintv("Create"));
            rowhead.addView(puttpdataintv("Update"));
            rowhead.addView(puttpdataintv("Action"));


            for (int i = 0; i < title - 1; i++) {
                row.addView(puttpdataintv(globalVariable.getScope()[i]), i + 1);
                rowhead.addView(puttpdataintv(globalVariable.getScope()[i]), i + 1);

            }


            row.setDividerDrawable(getDrawable(R.drawable.table_divider));
            row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            row.setBackgroundColor(Color.parseColor("#98fb98"));
            rowhead.setDividerDrawable(getDrawable(R.drawable.table_divider));
            rowhead.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            rowhead.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayout.addView(row);
            tableLayouthead.addView(rowhead);

        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createview(List<HashMap<String, String>> list){
        try {
            globalVariable.writelog("managemain createview");
            for (int i = 0; i < list.size(); i++) {
                final int pos;
                pos = i;

                TableRow row = new TableRow(this);
                row.addView(puttpdataintv(list.get(i).get("id")));

                row.addView(puttpdataintv(list.get(i).get("name")));
                if (title == 7) {
                    row.addView(puttpdataintv(list.get(i).get("ip")));
                    row.addView(puttpdataintv(list.get(i).get("macAddress")));
                }
                row.addView(puttpdataintv(list.get(i).get("description")));
                row.addView(puttpdataintv(list.get(i).get("createTime")));
                row.addView(puttpdataintv(list.get(i).get("updateTime")));
                for (int j = 0; j < title - 1; j++) {
                    Log.d("kevin", list.get(i).get(globalVariable.getScopename()[j]));
                    row.addView(puttpdataintv(list.get(i).get(globalVariable.getScopename()[j])), j + 1);

                }
                LinearLayout l = new LinearLayout(this);
                TableRow.LayoutParams LayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                ImageButton editbt = new ImageButton(this);
                editbt.setImageDrawable(getDrawable(R.drawable.edit));

                l.addView(editbt);
                editbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        globalVariable.writelog("managemain editbt");
                        Intent intent = new Intent();
                        intent.setClass(managemain.this, manageinfo.class);
                        intent.putExtra("id", list.get(pos).get("id"));
                        intent.putExtra("levelid", list.get(pos).get(globalVariable.getScopeid()[title - 2]));
                        intent.putExtra("title", title);
                        startActivity(intent);
                    }
                });
                ImageButton deletebt = new ImageButton(this);
                deletebt.setImageDrawable(getDrawable(R.drawable.remove));
                l.addView(deletebt);
                deletebt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        globalVariable.writelog("managemain deletebt");
                        AlertDialog.Builder alertDialog =
                                new AlertDialog.Builder(managemain.this);
                        alertDialog.setTitle("警告");
                        alertDialog.setMessage("是否刪除此資料");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                globalVariable.writelog("managemain deletebt ok");
                                globalVariable.setHttpreturn(false);
                                switch (title) {
                                    case 1:

                                        httpthread.deletecountry(list.get(pos).get("id"));

                                        break;
                                    case 2:
                                        httpthread.deletecity(list.get(pos).get("id"));

                                        break;
                                    case 3:
                                        httpthread.deletesite(list.get(pos).get("id"));

                                        break;
                                    case 4:
                                        httpthread.deletezone(list.get(pos).get("id"));

                                        break;
                                    case 5:
                                        httpthread.deleteline(list.get(pos).get("id"));

                                        break;
                                    case 6:
                                        httpthread.deletestation(list.get(pos).get("id"));

                                        break;
                                    case 7:
                                        httpthread.deleteipc(list.get(pos).get("id"));

                                        break;
                                    default:
                                        break;
                                }
                                Thread a1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (!globalVariable.isHttpreturn()) ;
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                tableLayout.removeViewAt(pos + 1);
                                            }
                                        });
                                    }
                                });
                                a1.run();
                            }
                        });
                        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                });
                row.addView(l, LayoutParams);
                row.setDividerDrawable(getDrawable(R.drawable.table_divider));
                row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
                row.setBackgroundColor(Color.parseColor("#ffffff"));


                tableLayout.addView(row);
                int count =row.getChildCount();
                for(int j=0;j<count;j++){
                    row.getChildAt(j).measure(0, 0);
                    int w=row.getChildAt(j).getMeasuredWidth();
                    ((TableRow)tableLayouthead.getChildAt(0)).getChildAt(j).measure(0, 0);
                    int w1=((TableRow)tableLayouthead.getChildAt(0)).getChildAt(j).getMeasuredWidth();
                    if(w>w1)
                        ((TextView)((TableRow)tableLayouthead.getChildAt(0)).getChildAt(j)).setWidth(w);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public TextView puttpdataintv(String data){
        TextView tv = new TextView(this);
        tv.setText(data);
        tv.setPadding(5,5,5,5);
        tv.setTextColor(Color.parseColor("#434343"));
        tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_12));
        tv.setGravity(Gravity.CENTER);
        return  tv;
    }
}
