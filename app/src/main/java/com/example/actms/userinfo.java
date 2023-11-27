package com.example.actms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class userinfo extends AppCompatActivity {
    httpthread httpthread;
    GlobalVariable globalVariable;
    LinearLayout linearLayout;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    String id="";
    EditText edname,edad,edemail,edpw;
    ImageButton imback,btsave;
    CheckBox pwck;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("userinfo onCreate");
        linearLayout=(LinearLayout) findViewById(R.id.ll_content);
        pwck=(CheckBox)findViewById(R.id.pwck);
        edname=(EditText)findViewById(R.id.nameed);
        edad=(EditText)findViewById(R.id.adaccounted);
        edemail=(EditText)findViewById(R.id.emailed);
        edpw=(EditText)findViewById(R.id.passworded);
        edpw.setHint("(未更改)");

        imback=(ImageButton) findViewById(R.id.backim);
        imback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                globalVariable.writelog("userinfo imback");
                finish();
            }
        });
        Intent intent = getIntent();
        try {
            id = intent.getStringExtra("id");
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        httpthread=new httpthread(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        pwck.post(new Runnable() {
            @Override
            public void run() {
                pwck.setChecked(false);
            }
        });
        edpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pwck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    edpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    edpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        try {
            globalVariable.setHttpreturn(false);
            httpthread.getroles("", "");
            Thread a = new Thread(new Runnable(){
                @Override
                public void run() {
                    while(!globalVariable.isHttpreturn());
                    for (int i = 0; i < globalVariable.getRoleList().size(); i++) {
                        CheckBox checkBox = new CheckBox(userinfo.this);
                        checkBox.setText(globalVariable.getRoleList().get(i).get("name"));
                        checkBoxs.add(checkBox);
                        checkBox.setTextColor(Color.parseColor("#ffffff"));
                        checkBox.setTextSize(23);
                        checkBox.setButtonTintList(getResources().getColorStateList(R.color.checkboxcolor));
                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp_40);
                        LayoutParams.topMargin = 20;
                        checkBox.setId(View.generateViewId());
                        linearLayout.addView(checkBox, LayoutParams);
                    }
                }
            });
            a.run();
            btsave = new ImageButton(this);
            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LayoutParams.topToBottom=checkBoxs.get(globalVariable.getRoleList().size()-1).getId();
//        LayoutParams.leftToLeft=LinearLayout.LayoutParams.PARENT_ID;
            LayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp_40);
            LayoutParams.topMargin = 20;
            btsave.setBackground(getDrawable(R.drawable.save));
            linearLayout.addView(btsave, LayoutParams);
            btsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    globalVariable.writelog("userinfo btsave");
                    List<String> role = new ArrayList<String>();
                    for (int i = 0; i < checkBoxs.size(); i++) {
                        if (checkBoxs.get(i).isChecked()) {
                            role.add(checkBoxs.get(i).getText().toString());
                        }
                    }

                    if (id.equals("")) {
                        if (passwordValidate()) {
                            globalVariable.setHttpreturn(false);
                            httpthread.createuser(edname.getText().toString(), edemail.getText().toString(), edad.getText().toString(), edpw.getText().toString(), role);
                            Thread a = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (!globalVariable.isHttpreturn()) ;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            finish();
                                        }
                                    });
                                }
                            });
                            a.run();
                        }

                    } else {
                        globalVariable.setHttpreturn(false);
                        httpthread.updatauser(id, edname.getText().toString(), edemail.getText().toString(), edad.getText().toString(), edpw.getText().toString(), role);
                        Thread a = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        finish();

                                    }
                                });
                            }
                        });
                        a.run();
                    }
                }

            });
            if (!id.equals("")) {
                globalVariable.setHttpreturn(false);
                httpthread.getuser(id);
                Thread a1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!globalVariable.isHttpreturn()) ;
                        runOnUiThread(new Runnable() {
                            public void run() {

                                edname.setText(globalVariable.getSelusername());
                                edad.setText(globalVariable.getSeladaccount());
                                edemail.setText(globalVariable.getSelemail());
                                for (int j = 0; j < globalVariable.getRoleList().size(); j++) {
                                    if (globalVariable.getSelrole().indexOf(globalVariable.getRoleList().get(j).get("name")) != -1)
                                        checkBoxs.get(j).setChecked(true);
                                }

                            }
                        });
                    }
                });
                a1.run();
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public boolean passwordValidate(){
        String name = edname.getText().toString();
        String email = edemail.getText().toString();
        String password = edpw.getText().toString();
        if(name.isEmpty())
        {
            Toast.makeText(this,"請輸入使用者名稱",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if (email.isEmpty()) {
                Toast.makeText(this, "請輸入電子信箱", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                // 8 character
                if ((password.length() < 8)||(!password.matches("(.*[0-9].*)"))||(!password.matches("(.*[A-Z].*)"))||(!password.matches("(.*[a-z].*)"))||(!password.matches("^(?=.*[@$#%!*?&]).*$"))) {
                    Toast.makeText(this, "密碼規則\n至少8碼\n至少一碼數字\n至少一碼大寫英文\n至少一碼小寫英文\n至少一碼特殊字元(@$#%!*?&)\n", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        pwck.post(new Runnable() {
            @Override
            public void run() {
                pwck.setChecked(false);
            }
        });

        edpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
