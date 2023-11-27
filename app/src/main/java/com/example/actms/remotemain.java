package com.example.actms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class remotemain extends AppCompatActivity {
    httpthread httpthread;
    GlobalVariable globalVariable;
    DBHelper DH=null;
    Spinner spdevice,spfunction;
    EditText edtpname;
    List<String> pclist=new ArrayList<String>();
    Button btsend;
    String pcname,fun;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_main);
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("remotemain onCreate");
        edtpname=(EditText)findViewById(R.id.edtpname);
        btsend=(Button) findViewById(R.id.btsend);
        httpthread=new httpthread(this);
        DH=new DBHelper(this);
        initview("");
        spfunction=(Spinner)findViewById(R.id.spfunction);
        spdevice=(Spinner)findViewById(R.id.spdevice);
        MySpinnerAdapter functionAdapter =new MySpinnerAdapter(Arrays.asList(getResources().getStringArray(R.array.remotecontrol)),remotemain.this,0);
        spfunction.setAdapter(functionAdapter);
        spfunction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fun=spfunction.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MySpinnerAdapter deviceAdapter =new MySpinnerAdapter(pclist,remotemain.this,0);
        spdevice.setAdapter(deviceAdapter);
        spdevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pcname=spdevice.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    globalVariable.writelog("remotemain btsend");
                    globalVariable.setHttpreturn(false);
                    if(fun.equals("OPENFILE"))
                        httpthread.getremo(pcname,fun,"/"+edtpname.getText().toString());
                    else
                        httpthread.getremo(pcname,fun,"");
                    Thread a1 = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            while(!globalVariable.isHttpreturn());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d("kevin","rolemain createview");

                                }
                            });
                        }
                    });
                    a1.run();

                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public void initview(String filter){
        Log.d("kevin","getPcinfo "+globalVariable.getPccount());
        pclist.clear();
        SQLiteDatabase db = DH.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + "pcinfo"+filter+" ORDER BY sPcName", null);
        while (c.moveToNext()) {

            pclist.add(c.getString(2));

        }
    }
}
