package com.example.actms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class Loginactivity extends AppCompatActivity {
    ImageButton login;
    TextView versiontv;
    GlobalVariable globalVariable;
    EditText accounted,passworded;
    public static Activity loginfa;
    Spinner typesp;
    httpthread httpthread;
    String type,account,password;
    CheckBox pwck;
    boolean isckper=false;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        try {
            PackageInfo info = null;
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = info.requestedPermissions;//This array contains the requested permissions.

            checkPermission(permissions, 999);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        dialog=new ProgressDialog(this);
        httpthread=new httpthread(this);
        loginfa=this;
        globalVariable=(GlobalVariable)getApplicationContext();
        accounted=(EditText) findViewById(R.id.aced);
        passworded=(EditText) findViewById(R.id.pwed);
        versiontv=(TextView)findViewById(R.id.versiontv);
        typesp=(Spinner)findViewById(R.id.sptype);
        pwck=(CheckBox)findViewById(R.id.pwck);
        if(isckper) {
            globalVariable.cklogfile();
            globalVariable.writelog("Loginactivity onCreate");
        }
        versiontv.setText("Ver "+globalVariable.getVersion());
        login=(ImageButton)findViewById(R.id.loginim);
        login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                globalVariable.writelog("按 login");
                runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("Loading...");
                        dialog.show();
                    }
                });

                account=accounted.getText().toString();
                password=passworded.getText().toString();
//                account="admin";
//                password="Ielob666";
                globalVariable.setHttpreturn(false);
                httpthread.login(type,account,password);
                Thread a = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        while(!globalVariable.isHttpreturn());
                        runOnUiThread(new Runnable() {
                            public void run() {

                                if(globalVariable.getHttptoken().equals("error")){
                                    globalVariable.writelog("login 錯誤");
                                    AlertDialog.Builder alertDialog =
                                            new AlertDialog.Builder(Loginactivity.this);
                                    alertDialog.setTitle("錯誤");
                                    alertDialog.setMessage("帳號或密碼錯誤");
                                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.setCancelable(false);
                                    alertDialog.show();
                                }else{
                                    httpthread.deleteAlltp();
                                    Intent intent = new Intent();
                                    intent.setClass(Loginactivity.this, menuactivity.class);
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                });
                a.run();

            }
        });



//        ArrayAdapter<CharSequence> resultList = ArrayAdapter.createFromResource(this,
//                R.array.type, R.layout.myspinner);
        MySpinnerAdapter myAdapter =new MySpinnerAdapter(Arrays.asList(getResources().getStringArray(R.array.type)),Loginactivity.this,0);
        typesp.setAdapter(myAdapter);
        typesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=(String) typesp.getSelectedItem();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isckper)
            globalVariable.writelog("Loginactivity onStart");
        pwck.post(new Runnable() {
            @Override
            public void run() {
                pwck.setChecked(false);
            }
        });
        passworded.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pwck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    passworded.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    passworded.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        Log.d("kevin","login onstart "+pwck.isChecked());
    }
    public void checkPermission(String[] permission, int requestCode) {
        boolean isck=false;
        for(int i=0;i<permission.length;i++){
            if (ContextCompat.checkSelfPermission(this, permission[i])!= PackageManager.PERMISSION_GRANTED) {
                Log.d("kevin","login checkPermission "+permission[i]);
                // Requesting the permission
                isck=true;
            }
        }
        Log.d("kevin","login checkPermission ");
        if(isck) {
            ActivityCompat.requestPermissions(this, permission, requestCode);

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("kevin","login onRequestPermissionsResult ");
        isckper=true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
        if(isckper)
            globalVariable.writelog("Loginactivity onStop");
        pwck.post(new Runnable() {
            @Override
            public void run() {
                pwck.setChecked(false);
            }
        });

        passworded.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Log.d("kevin","login onStop "+pwck.isChecked());

    }

}
