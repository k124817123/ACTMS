package com.example.actms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class manageinfo extends AppCompatActivity {
    GlobalVariable globalVariable;
    httpthread httpthread;
    int title;
    TextView leveltv;
    Spinner levelsp,systypesp;
    ImageButton imback,btsave;
    int spid=0;
    String id="";
    String levelid="";
    EditText edname,eddescription,edpcname,edip,edmac,eddevicekey,edtoken;
    String sys="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariable=(GlobalVariable) getApplication();
        Intent intent = getIntent();
        try {
            title = intent.getIntExtra("title", 0);
            id = intent.getStringExtra("id");
            levelid = intent.getStringExtra("levelid");
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        globalVariable.writelog("manageinfo onCreate "+title);
        httpthread=new httpthread(this);
        if(title==7){
            setContentView(R.layout.ipcinfo);
            levelsp =(Spinner) findViewById(R.id.locatsp);
            systypesp =(Spinner) findViewById(R.id.systypesp);
            edpcname = (EditText)findViewById(R.id.computernameed) ;
            edip = (EditText)findViewById(R.id.iped) ;
            edmac = (EditText)findViewById(R.id.maced) ;
            eddevicekey = (EditText)findViewById(R.id.devicekeyed) ;
            edtoken = (EditText)findViewById(R.id.tokened) ;
            MySpinnerAdapter sysAdapter =new MySpinnerAdapter(Arrays.asList(getResources().getStringArray(R.array.systemtype)),this,0);
            systypesp.setAdapter(sysAdapter);
            systypesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sys=systypesp.getSelectedItem().toString();
                    globalVariable.writelog("manageinfo systypesp "+sys);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            setContentView(R.layout.manageinfo);
            levelsp =(Spinner) findViewById(R.id.levelsp);
            leveltv=(TextView) findViewById(R.id.leveltv);
            if(title>1)
                leveltv.setText(globalVariable.getScope()[title - 2]);
            else {
                leveltv.setVisibility(View.INVISIBLE);
                levelsp.setVisibility(View.INVISIBLE);
            }
        }

        if(title>1) {
            globalVariable.setHttpreturn(false);
            httpthread.getname(globalVariable.getScope()[title - 2]);
            Thread a = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!globalVariable.isHttpreturn()) ;
                        globalVariable.getLevelname().remove(0);
                        MySpinnerAdapter myAdapter = new MySpinnerAdapter(globalVariable.getLevelname(), "fullName", manageinfo.this, 1);
                        levelsp.setAdapter(myAdapter);
                        levelsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                spid = position;
                                globalVariable.writelog("manageinfo levelsp "+spid);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Log.d("kevin", "onNothingSelected");
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                        globalVariable.writeexception(e);
                    }
                }
            });
            a.run();
        }
        imback=(ImageButton) findViewById(R.id.backim);
        imback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                globalVariable.writelog("manageinfo imback");
                finish();
            }
        });
        edname = (EditText)findViewById(R.id.nameed) ;
        eddescription = (EditText)findViewById(R.id.descriptioned) ;
        btsave=(ImageButton) findViewById(R.id.savebt);
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.writelog("manageinfo btsave "+id);
                if(id.equals("")){

                    globalVariable.setHttpreturn(false);
                    switch (title){
                        case 1:
//                            filter.setVisibility(View.INVISIBLE);
                            httpthread.createcountry(edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 2:
                            httpthread.createcity(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 3:
                            httpthread.createsite(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 4:
                            httpthread.createzone(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());

                            break;
                        case 5:
                            httpthread.createline(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 6:
                            httpthread.createstation(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 7:
                            httpthread.createipc(globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString(),edpcname.getText().toString(),sys,edip.getText().toString(),edmac.getText().toString(),"");
                            break;
                        default:break;
                    }
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!globalVariable.isHttpreturn()) ;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    globalVariable.writelog("manageinfo btsave finish");
                                    finish();
                                }
                            });
                        }
                    });
                    a.run();

                }
                else{
                    globalVariable.setHttpreturn(false);
                    switch (title){
                        case 1:
//                            filter.setVisibility(View.INVISIBLE);
                            httpthread.updatacountry(id,edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 2:
                            httpthread.updatacity(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 3:
                            httpthread.updatasite(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 4:
                            httpthread.updatazone(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 5:
                            httpthread.updataline(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 6:
                            httpthread.updatastation(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString());
                            break;
                        case 7:
                            httpthread.updataipc(id,globalVariable.getLevelname().get(spid).get("id"),edname.getText().toString(),eddescription.getText().toString(),edpcname.getText().toString(),sys,edip.getText().toString(),edmac.getText().toString(),"");
                            break;
                        default:break;
                    }
//                    httpthread.updatarole(id,edname.getText().toString(),eddescription.getText().toString(),selscopeid,selfunctionvalue);
                    Thread a = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    globalVariable.writelog("manageinfo btsave finish");
                                    finish();

                                }
                            });
                        }
                    });
                    a.run();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        globalVariable.writelog("manageinfo onStart "+id);
        if(!id.equals("")) {
            globalVariable.setHttpreturn(false);
            switch (title){
                case 1:
//                            filter.setVisibility(View.INVISIBLE);
                    httpthread.getcountry(id);

                    break;
                case 2:
                    httpthread.getcity(id);

                    break;
                case 3:
                    httpthread.getsite(id);

                    break;
                case 4:
                    httpthread.getzone(id);

                    break;
                case 5:
                    httpthread.getline(id);

                    break;
                case 6:
                    httpthread.getstation(id);

                    break;
                case 7:
                    httpthread.getipc(id);
                    break;
                default:break;
            }
            Thread a = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!globalVariable.isHttpreturn()) ;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                edname.setText(globalVariable.getSelmanage().get("name"));
                                eddescription.setText(globalVariable.getSelmanage().get("description"));
                                for (int i = 0; i < globalVariable.getLevelname().size(); i++) {

                                    if (!levelid.equals("")) {
                                        if (globalVariable.getLevelname().get(i).get("id").equals(levelid))
                                            levelsp.setSelection(i);
                                    }
                                }
                                if (title == 7) {
                                    edpcname.setText(globalVariable.getSelmanage().get("computerName"));
                                    edip.setText(globalVariable.getSelmanage().get("ip"));
                                    edmac.setText(globalVariable.getSelmanage().get("macAddress"));
                                    eddevicekey.setText(globalVariable.getSelmanage().get("deviceKey"));
                                    edtoken.setText(globalVariable.getSelmanage().get("token"));
                                }
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
    }
}
