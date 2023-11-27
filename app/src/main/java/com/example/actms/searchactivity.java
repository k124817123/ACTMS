package com.example.actms;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class searchactivity extends AppCompatActivity {
    TextView monitormessage;
    TextView tvstart,tvend;
    Spinner spresult,sppage;
    ImageButton button;
    String startdate="",enddate="",result="",page="",tpid="";
    TableLayout tableLayout,tableLayouthead;
    httpthread httpthread;
    GlobalVariable globalVariable;
    List<String> csvList=new ArrayList<String>();
    List<Integer> width=new ArrayList<Integer>();
    EditText eddevice;
    boolean isti=false;
    boolean issearch=false;

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kevin","searchactivity onCreate");
        setContentView(R.layout.search_main);
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("searchactivity onCreate");
        monitormessage=(TextView)findViewById(R.id.monitormessage);

        spresult=(Spinner)findViewById(R.id.spresult);
        sppage=(Spinner)findViewById(R.id.sppage);
        sppage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                page=sppage.getSelectedItem().toString();
//                globalVariable.setNowpage(position+1);
                if(!issearch) {
                    globalVariable.setHttpreturn(false);

                    if (!isti) {
                        inittpviewhead();
                        httpthread.tpinfos(startdate, enddate, result, eddevice.getText().toString(),page,"");
                        Thread a = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        isti = false;
                                        creattpview();
                                    }
                                });
                            }
                        });
                        a.run();
                    } else {

                        inittiviewhead();
                        page = "";
                        globalVariable.setHttpreturn(false);
                        httpthread.tiinfos(tpid, page,"");
                        Thread a = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        isti = true;
                                        creattiview();
                                        button.setBackground(getDrawable(R.drawable.backarrow));
                                    }
                                });
                            }
                        });
                        a.run();
                    }
                }
                issearch=false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        eddevice=(EditText)findViewById(R.id.edpc);
        button=(ImageButton)findViewById(R.id.btsearch);
        tableLayout=(TableLayout)findViewById(R.id.tablelayout);
        tableLayouthead=(TableLayout)findViewById(R.id.tablelayouthead);
//        tableLayouthead.setStretchAllColumns(true);
//        tableLayout.setStretchAllColumns(true);

        inittpviewhead();
        Date now=new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        tvstart=(TextView) findViewById(R.id.tvdatestart);
        tvstart.setText(sdFormat.format(now));
        startdate=sdFormat.format(now);
        tvstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.writelog("searchactivity tvstart");
                Calendar calendar=Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(searchactivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateformat.format(calendar.getTime());
                        startdate=strDate;
                        tvstart.setText(strDate);
                    }
                }, year,month, day).show();
            }
        });
        tvend=(TextView) findViewById(R.id.tvdateend);
        tvend.setText(sdFormat.format(now));
        enddate=sdFormat.format(now);
        tvend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.writelog("searchactivity tvend");
                Calendar calendar=Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(searchactivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = dateformat.format(calendar.getTime());
                        enddate=strDate;
                        tvend.setText(strDate);
                    }
                }, year,month, day).show();
            }
        });
        monitormessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        httpthread=new httpthread(this);
        globalVariable.setHttpreturn(false);
        MySpinnerAdapter myAdapter =new MySpinnerAdapter(Arrays.asList(getResources().getStringArray(R.array.result)),searchactivity.this,0);
        spresult.setAdapter(myAdapter);
        spresult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                result=(String) spresult.getSelectedItem();
                switch ((String) spresult.getSelectedItem()){
                    case "Pass":result="1";break;
                    case "Fail":result="0";break;
                    default:result="";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin","searchactivity onStart");
        globalVariable.writelog("searchactivity onStart");
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
    public void inittiviewhead(){
        try {
            tableLayout.removeAllViews();
            tableLayouthead.removeAllViews();
            TableRow row = new TableRow(getApplicationContext());
            row.addView(puttpdataintv("Start"));
            row.addView(puttpdataintv("End"));
            row.addView(puttpdataintv("Seq."));
            row.addView(puttpdataintv("Name"));
            row.addView(puttpdataintv("Result"));
            row.addView(puttpdataintv("Update"));
            row.setDividerDrawable(getDrawable(R.drawable.table_divider));
            row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            row.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayout.addView(row);
            TableRow rowhead = new TableRow(getApplicationContext());
            rowhead.addView(puttpdataintv("Start"));
            rowhead.addView(puttpdataintv("End"));
            rowhead.addView(puttpdataintv("Seq."));
            rowhead.addView(puttpdataintv("Name"));
            rowhead.addView(puttpdataintv("Result"));
            rowhead.addView(puttpdataintv("Update"));
            rowhead.setDividerDrawable(getDrawable(R.drawable.table_divider));
            rowhead.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            rowhead.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayouthead.addView(rowhead);
//            tableLayouthead.addView(row);
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }

    }
    public void inittpviewhead(){
        try {
            tableLayout.removeAllViews();
            tableLayouthead.removeAllViews();
            TableRow row = new TableRow(getApplicationContext());

            row.addView(puttpdataintv("IPC name"));
            row.addView(puttpdataintv("Start"));
            row.addView(puttpdataintv("End"));
            row.addView(puttpdataintv("Name"));
            row.addView(puttpdataintv("Result"));
            row.addView(puttpdataintv("Total Seq."));
            row.addView(puttpdataintv("Done Seq."));
            row.addView(puttpdataintv("Update"));

            row.setDividerDrawable(getDrawable(R.drawable.table_divider));
            row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            row.setBackgroundColor(Color.parseColor("#98fb98"));

            tableLayout.addView(row);
            TableRow rowhead = new TableRow(getApplicationContext());

            rowhead.addView(puttpdataintv("IPC name"));
            rowhead.addView(puttpdataintv("Start"));
            rowhead.addView(puttpdataintv("End"));
            rowhead.addView(puttpdataintv("Name"));
            rowhead.addView(puttpdataintv("Result"));
            rowhead.addView(puttpdataintv("Total Seq."));
            rowhead.addView(puttpdataintv("Done Seq."));
            rowhead.addView(puttpdataintv("Update"));

            rowhead.setDividerDrawable(getDrawable(R.drawable.table_divider));
            rowhead.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            rowhead.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayouthead.addView(rowhead);

        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public TextView puttpdataintv(String data){
        TextView tv = new TextView(getApplicationContext());
        tv.setText(data);
        tv.setPadding(5,5,5,5);
        tv.setTextColor(Color.parseColor("#434343"));
        tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_12));
        tv.setGravity(Gravity.CENTER);
        return  tv;
    }
    public TextView puttpresultintv(String data){
        TextView tv = new TextView(getApplicationContext());
        tv.setText(data);
        tv.setPadding(5,5,5,5);
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_12));
        tv.setGravity(Gravity.CENTER);
        if(data.equals("PASS"))
            tv.setBackgroundColor(Color.parseColor("#008000"));
        else if(data.equals("FAIL"))
            tv.setBackgroundColor(Color.parseColor("#ff0000"));
        else
            tv.setTextColor(Color.parseColor("#434343"));

        return  tv;
    }
    public void creattiview(){
        try {
            for (int i = 0; i < globalVariable.getTiinfoList().size(); i++) {
                TableRow row = new TableRow(getApplicationContext());
                row.addView(puttpdataintv(globalVariable.getTiinfoList().get(i).get("startTime")));
                row.addView(puttpdataintv(globalVariable.getTiinfoList().get(i).get("endTime")));
                row.addView(puttpdataintv(globalVariable.getTiinfoList().get(i).get("seq")));
                row.addView(puttpdataintv(globalVariable.getTiinfoList().get(i).get("name")));
                row.addView(puttpresultintv(globalVariable.getTiinfoList().get(i).get("result")));
                row.addView(puttpdataintv(globalVariable.getTiinfoList().get(i).get("updateTime")));

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
   public void creattpview(){
        try {
            for (int i = 0; i < globalVariable.getTpinfoList().size(); i++) {
                TableRow row = new TableRow(getApplicationContext());
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("ipcName")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("startTime")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("endTime")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("name")));
                row.addView(puttpresultintv(globalVariable.getTpinfoList().get(i).get("result")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("totalSeq")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("doneSeq")));
                row.addView(puttpdataintv(globalVariable.getTpinfoList().get(i).get("updateTime")));
                row.setDividerDrawable(getDrawable(R.drawable.table_divider));
                row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
                row.setBackgroundColor(Color.parseColor("#ffffff"));
                String runid = globalVariable.getTpinfoList().get(i).get("id");
                row.setOnClickListener((v) -> {
                    globalVariable.writelog("searchactivity row onclick");

                    inittiviewhead();
                    page="";
                    globalVariable.setHttpreturn(false);
                    tpid=runid;
                    httpthread.tiinfos(runid,page,"");
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!globalVariable.isHttpreturn()) ;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    isti=true;
                                    creattiview();
                                    button.setBackground(getDrawable(R.drawable.backarrow));
                                    MySpinnerAdapter myAdapter =new MySpinnerAdapter(globalVariable.getPagelist(),searchactivity.this,0);
                                    sppage.setAdapter(myAdapter);
                                }
                            });
                        }
                    });
                    a.run();
                });
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
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kevin","searchactivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("kevin","searchactivity onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("kevin","searchactivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("kevin","searchactivity onPause");
    }
    public void datasearch(View view) {
        Log.d("kevin","datesearch");
        globalVariable.writelog("searchactivity datasearch");
        issearch=true;
        button.setBackground(getDrawable(R.drawable.search1));
        monitormessage.setText("Searching...");
        globalVariable.setHttpreturn(false);

        inittpviewhead();
        httpthread.tpinfos(startdate,enddate,result,eddevice.getText().toString(),page,"");
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        isti=false;
                        creattpview();

                        MySpinnerAdapter myAdapter =new MySpinnerAdapter(globalVariable.getPagelist(),searchactivity.this,0);
                        sppage.setAdapter(myAdapter);

                        monitormessage.setText("Searching done");
                    }
                });
            }
        });
        a.run();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.d("kevin","Configuration "+newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 什麼都不用寫
            Log.d("kevin","Configuration.ORIENTATION_LANDSCAPE");
        }
        else {
            // 什麼都不用寫
            Log.d("kevin","Configuration.ORIENTATION_LANDSCAPE 1");
        }
    }
    public void dataexport(View v){

        try{
            globalVariable.writelog("searchactivity dataexport");
            monitormessage.setText("Exporting...");
            csvList.clear();
            globalVariable.setHttpreturn(false);
            if(isti){
                httpthread.tiinfos(tpid,"",String.valueOf(globalVariable.getTotalcount()));
            }else{
                httpthread.tpinfos(startdate,enddate,result,eddevice.getText().toString(),"",String.valueOf(globalVariable.getTotalcount()));
            }
            Thread a = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        while (!globalVariable.isHttpreturn()) ;

                        if (isti) {
                            csvList.add("Start,End,Seq.,Name,Result,Update\n");
                            for (int i = 0; i < globalVariable.getTiinfoList().size(); i++)
                                csvList.add(globalVariable.getTiinfoList().get(i).get("startTime") + "," + globalVariable.getTiinfoList().get(i).get("endTime") + "," + globalVariable.getTiinfoList().get(i).get("seq") + "," + globalVariable.getTiinfoList().get(i).get("name") + "," + globalVariable.getTiinfoList().get(i).get("result") + "," + globalVariable.getTiinfoList().get(i).get("updateTime") + "," + "\n");
                        } else {
                            csvList.add("IPC name,Start,End,Name,Result,Total Seq,Done Seq,Update\n");
                            for (int i = 0; i < globalVariable.getTpinfoList().size(); i++)
                                csvList.add(globalVariable.getTpinfoList().get(i).get("ipcName") + "," + globalVariable.getTpinfoList().get(i).get("startTime") + "," + globalVariable.getTpinfoList().get(i).get("endTime") + "," + globalVariable.getTpinfoList().get(i).get("name") + "," + globalVariable.getTpinfoList().get(i).get("result") + "," + globalVariable.getTpinfoList().get(i).get("totalSeq") + "," + globalVariable.getTpinfoList().get(i).get("doneSeq") + "," + globalVariable.getTpinfoList().get(i).get("updateTime") + "\n");
                        }
                        Date current = new Date();
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String filename = "";
                        if (isti)
                            filename = dateformat.format(current) + "_TI.csv";
                        else
                            filename = dateformat.format(current) + "_TP.csv";

                        File csvfile = new File(Environment.getExternalStorageDirectory() + "/ACTMS/", filename);

                        Log.d("kevin", "csvfile:" + csvfile.getAbsolutePath());
                        if (!csvfile.exists()) {
                            csvfile.getParentFile().mkdirs();
                            csvfile.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(csvfile, true);
                        for (int i = 0; i < csvList.size(); i++) {
                            fos.write(csvList.get(i).getBytes());
                        }
                        fos.close();
                        monitormessage.setText("Exporting done");
                    }catch (Exception e){
                        e.printStackTrace();
                        globalVariable.writeexception(e);
                        monitormessage.setText("Exporting fail");
                    }
                }
            });
            a.run();

        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
            monitormessage.setText("Exporting fail");
        }

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);

        return mScaleDetector.onTouchEvent(event);
    }

}