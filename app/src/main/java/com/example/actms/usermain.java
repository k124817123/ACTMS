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
import java.util.List;


public class usermain extends AppCompatActivity {


    httpthread httpthread;
    GlobalVariable globalVariable;
    ImageButton create,export;
    TableLayout tableLayout,tableLayouthead;
    List<String> csvList=new ArrayList<String>();
    Spinner sppage;
    String page="";
    boolean issearch=false;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);
        Log.d("kevin","usermain onCreate");
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("usermain onCreate");
        httpthread=new httpthread(this);
        sppage=(Spinner)findViewById(R.id.sppage);
        sppage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                page=sppage.getSelectedItem().toString();
//                globalVariable.setNowpage(position+1);
                if(!issearch) {
                    globalVariable.setHttpreturn(false);

                    initviewhead();
                    httpthread.getusers(page,"");
                    Thread a1 = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d("kevin","usermain createview");
                                    createview();
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
        globalVariable.setHttpreturn(false);
        httpthread.getroles("","");
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());

            }
        });
        a.run();
        tableLayout=(TableLayout) findViewById(R.id.tablelayout);
        tableLayouthead=(TableLayout) findViewById(R.id.tablelayouthead);
        create=(ImageButton) findViewById(R.id.createbt);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.writelog("usermain create");
                Intent intent = new Intent();
                intent.setClass(usermain.this, userinfo.class);
                intent.putExtra("id","");
                startActivity(intent);
            }
        });
        export=(ImageButton) findViewById(R.id.exportbt);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    globalVariable.writelog("usermain export");
                    globalVariable.setHttpreturn(false);
                    httpthread.getusers("",String.valueOf(globalVariable.getTotalcount()));
                    csvList.clear();
                    Thread a1 = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                   try{
                                       csvList.add("Name,AdAccount,E-mail,Update\n");
                                       for (int i = 0; i < globalVariable.getUserList().size(); i++)
                                           csvList.add(globalVariable.getUserList().get(i).get("userName") + "," + globalVariable.getUserList().get(i).get("adAccount") + "," + globalVariable.getUserList().get(i).get("email") + "," + globalVariable.getUserList().get(i).get("updateTime") + "\n");
                                       Date current = new Date();
                                       SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
                                       String filename=dateformat.format(current)+"_User.csv";

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

    }


    @Override
    protected void onStart() {
        super.onStart();
        globalVariable.writelog("usermain onStart");
        issearch=true;

        initviewhead();
        globalVariable.setHttpreturn(false);
        httpthread.getusers(page,"");
        Thread a1 = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("kevin","usermain createview");
                        createview();
                        MySpinnerAdapter myAdapter =new MySpinnerAdapter(globalVariable.getPagelist(),usermain.this,0);
                        sppage.setAdapter(myAdapter);
                    }
                });
            }
        });
        a1.run();
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
            tableLayout.removeAllViews();
            tableLayouthead.removeAllViews();
            TableRow row = new TableRow(this);

            row.addView(puttpdataintv("Name"));
            row.addView(puttpdataintv("AdAccount"));
            row.addView(puttpdataintv("E-mail"));
            row.addView(puttpdataintv("Update"));
            row.addView(puttpdataintv("Action"));

            row.setDividerDrawable(getDrawable(R.drawable.table_divider));
            row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            row.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayout.addView(row);
            TableRow rowhead = new TableRow(this);

            rowhead.addView(puttpdataintv("Name"));
            rowhead.addView(puttpdataintv("AdAccount"));
            rowhead.addView(puttpdataintv("E-mail"));
            rowhead.addView(puttpdataintv("Update"));
            rowhead.addView(puttpdataintv("Action"));

            rowhead.setDividerDrawable(getDrawable(R.drawable.table_divider));
            rowhead.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            rowhead.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayouthead.addView(rowhead);

        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createview(){
        try {
            for (int i = 0; i < globalVariable.getUserList().size(); i++) {
                final int pos;
                pos = i;
                TableRow row = new TableRow(this);
                row.addView(puttpdataintv(globalVariable.getUserList().get(i).get("userName")));
                row.addView(puttpdataintv(globalVariable.getUserList().get(i).get("adAccount")));
                row.addView(puttpdataintv(globalVariable.getUserList().get(i).get("email")));
                row.addView(puttpdataintv(globalVariable.getUserList().get(i).get("updateTime")));
                LinearLayout l = new LinearLayout(this);
                ImageButton editbt = new ImageButton(this);
                editbt.setImageDrawable(getDrawable(R.drawable.edit));
                l.addView(editbt);
                editbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        globalVariable.writelog("usermain editbt");
                        Intent intent = new Intent();
                        intent.setClass(usermain.this, userinfo.class);
                        intent.putExtra("id", globalVariable.getUserList().get(pos).get("id"));
                        startActivity(intent);
                    }
                });
                ImageButton deletebt = new ImageButton(this);
                deletebt.setImageDrawable(getDrawable(R.drawable.remove));
                l.addView(deletebt);
                deletebt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        globalVariable.writelog("usermain deletebt");
                        AlertDialog.Builder alertDialog =
                                new AlertDialog.Builder(usermain.this);
                        alertDialog.setTitle("警告");
                        alertDialog.setMessage("是否刪除此資料");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                globalVariable.writelog("usermain deletebt ok");
                                globalVariable.setHttpreturn(false);
                                httpthread.deleteuser(globalVariable.getUserList().get(pos).get("id"));
                                Thread a = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (!globalVariable.isHttpreturn()) ;
                                        runOnUiThread(new Runnable() {
                                            public void run() {
//                                            globalVariable.getUserList().remove(pos);
                                                tableLayout.removeViewAt(pos + 1);
                                            }
                                        });
                                    }
                                });
                                a.run();
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
                row.addView(l);
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);

        return mScaleDetector.onTouchEvent(event);
    }
}
