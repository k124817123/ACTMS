package com.example.actms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import treeview.Node;
import treeview.TreeListView;

public class roleinfo extends AppCompatActivity {
    httpthread httpthread;
    GlobalVariable globalVariable;
    ImageButton scope,fun;
    RelativeLayout rl;
    ScrollView scrollView;
    boolean isshow=false;
    boolean isclick=false;
    private TreeListView listView;
    List<String>  selscopeid = new ArrayList<String> ();
    List<String>  selscopevalue = new ArrayList<String> ();
    List<String>  selfunctionid = new ArrayList<String> ();
    List<String>  selfunctionvalue = new ArrayList<String> ();
    ImageButton imback;
    ChipGroup scopeGroup;
    ChipGroup finctionGroup;
    ImageButton savebt;
    String id="";
    EditText edname,eddescription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleinfo);
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("roleinfo onCreate");
        httpthread=new httpthread(this);
        globalVariable.setHttpreturn(false);
        scopeGroup=(ChipGroup)findViewById(R.id.scopeList);

        finctionGroup=(ChipGroup)findViewById(R.id.functionList);
        savebt=(ImageButton) findViewById(R.id.savebt);
        edname=(EditText)findViewById(R.id.nameed);
        eddescription=(EditText)findViewById(R.id.descriptioned);
        Intent intent = getIntent();
        try {
            id = intent.getStringExtra("id");
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        httpthread.getentity();
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                globalVariable.setHttpreturn(false);
                httpthread.getfunction();
                while(!globalVariable.isHttpreturn());
            }
        });
        a.run();
        scope=(ImageButton) findViewById(R.id.scopebt);
        fun=(ImageButton) findViewById(R.id.functionsbt);
        rl=(RelativeLayout) findViewById(R.id.roleinforl);

        imback=(ImageButton) findViewById(R.id.backim);
        imback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                globalVariable.writelog("roleinfo imback");
                finish();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            globalVariable.writelog("roleinfo onStart");
            scope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    globalVariable.writelog("roleinfo scope");
                    if (!isshow) {
                        scope.setBackground(getDrawable(R.drawable.negative));
                        scope.setTag("add");
                        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        LayoutParams.leftMargin = 50;
                        LayoutParams.rightMargin = 50;
                        LayoutParams.addRule(RelativeLayout.BELOW, R.id.scopebt);
                        RelativeLayout relativeLayout = new RelativeLayout(roleinfo.this);
                        scrollView = new ScrollView(roleinfo.this);

                        listView = new TreeListView(roleinfo.this, globalVariable.getScopelist());
                        listView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        relativeLayout.addView(listView);
                        relativeLayout.setBackgroundColor(Color.WHITE);
//                    relativeLayout.setHorizontalScrollBarEnabled(true);
//                    relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        scrollView.addView(relativeLayout);
                        rl.addView(scrollView, LayoutParams);
                        listView.setSelect(selscopeid);
                        isshow = true;
                    } else {

                        if (scope.getTag().equals("add")) {
                            scope.setTag("done");
                            scope.setBackground(getDrawable(R.drawable.add));
//                        selscopenode.clear();
                            getselectvalues(listView.get(), selscopeid, selscopevalue);
                            addNewChip(scopeGroup, selscopeid, selscopevalue);
//                        selscopenode.addAll(listView.get());
                        } else {
                            fun.setBackground(getDrawable(R.drawable.add));
                            fun.getTag().equals("done");
                        }
                        rl.removeView(scrollView);
                        isshow = false;
                    }
                }
            });
            fun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    globalVariable.writelog("roleinfo fun");
                    if (!isshow) {
                        fun.setBackground(getDrawable(R.drawable.negative));
                        fun.setTag("add");
                        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        LayoutParams.leftMargin = 50;
                        LayoutParams.rightMargin = 50;
                        LayoutParams.addRule(RelativeLayout.BELOW, R.id.functionsbt);
                        RelativeLayout relativeLayout = new RelativeLayout(roleinfo.this);
                        scrollView = new ScrollView(roleinfo.this);

                        listView = new TreeListView(roleinfo.this, globalVariable.getFunctionslist());
                        listView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        relativeLayout.addView(listView);
                        relativeLayout.setBackgroundColor(Color.WHITE);
                        scrollView.addView(relativeLayout);
                        rl.addView(scrollView, LayoutParams);
                        listView.setSelect(selfunctionid);
                        isshow = true;
                    } else {

                        if (fun.getTag().equals("add")) {
                            fun.setTag("done");
                            fun.setBackground(getDrawable(R.drawable.add));
                            getselectvalues(listView.get(), selfunctionid, selfunctionvalue);
                            addNewChip(finctionGroup, selfunctionid, selfunctionvalue);
//                        selfunctionnode.clear();
//                        selfunctionnode.addAll(listView.get());
                        } else {
                            scope.setBackground(getDrawable(R.drawable.add));
                            scope.getTag().equals("done");
                        }
                        rl.removeView(scrollView);
                        isshow = false;
                    }
                }
            });
            savebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    globalVariable.writelog("roleinfo savebt");
                    if (id.equals("")) {

                        globalVariable.setHttpreturn(false);
                        httpthread.createrole(edname.getText().toString(), eddescription.getText().toString(), selscopeid, selfunctionvalue);
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

                    } else {
                        globalVariable.setHttpreturn(false);
                        httpthread.updatarole(id, edname.getText().toString(), eddescription.getText().toString(), selscopeid, selfunctionvalue);
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
                httpthread.getrole(id);
                Thread a = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!globalVariable.isHttpreturn()) ;
                        runOnUiThread(new Runnable() {
                            public void run() {

                                edname.setText(globalVariable.getSelrolename());
                                eddescription.setText(globalVariable.getSeldescription());
                                selscopeid.clear();
                                selscopeid.addAll(globalVariable.getSelscope());
                                addNewChip(scopeGroup, selscopeid, globalVariable.getSelscopelabel());
                                selfunctionid.clear();
                                selfunctionid.addAll(globalVariable.getSelfinction());
                                addNewChip(finctionGroup, selfunctionid, globalVariable.getSelfinctionlabel());
                            }
                        });
                    }
                });
                a.run();
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getselectvalues(List<Node> nodelist,List<String> id,List<String> valus){
        globalVariable.writelog("roleinfo getselectvalues");
        id.clear();
        valus.clear();
        for(int i=0;i<nodelist.size();i++){
            id.add(nodelist.get(i).getCurId());
            valus.add(nodelist.get(i).getValue());
            Log.d("kevin","getselectvalues"+nodelist.get(i).getValue());
        }

    }
    private void addNewChip(ChipGroup chipGroup,List<String> id,List<String> valus) {
        try {
            globalVariable.writelog("roleinfo addNewChip");
            LayoutInflater inflater = LayoutInflater.from(this);
            chipGroup.removeAllViews();
            // Create a Chip from Layout.
            for(int i=0;i<valus.size();i++){
                Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, chipGroup, false);
                newChip.setText(valus.get(i));
                chipGroup.addView(newChip);
                newChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        handleChipCheckChanged((Chip) buttonView, isChecked);
                    }
                });

                newChip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleChipCloseIconClicked((Chip) v,id,valus);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);

        }
    }
    private void handleChipCloseIconClicked(Chip chip,List<String> id,List<String> valus) {
        ChipGroup parent = (ChipGroup) chip.getParent();

        parent.removeView(chip);
        int index=valus.indexOf(chip.getText());
        valus.remove(index);
        id.remove(index);
    }

    // Chip Checked Changed
    private void handleChipCheckChanged(Chip chip, boolean isChecked) {
    }
}
