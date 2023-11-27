package com.example.actms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TPactivity extends AppCompatActivity {
    TextView noinfo;
    RelativeLayout tprl;
//    FloatingActionButton filterbt;
    ImageButton close;
    public static Activity fa;
    httpthread httpthread;
    GlobalVariable globalVariable;
    String pcname="";
    DBHelper DH=null;
    ProgressBar loadingbar;
    RecyclerView mRecyclerView;
    MyTPListAdapter mytpListAdapter;
    List<HashMap<String, String>> tpinfoList=new ArrayList<HashMap<String, String>>();
    boolean frist=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kevin","TPactivity onCreate");
        setContentView(R.layout.tp_main);
        globalVariable=(GlobalVariable) getApplication();
        tprl=(RelativeLayout) findViewById(R.id.tprl);
        Intent intent = getIntent();
        try {
            pcname = intent.getStringExtra("pcname");
            Log.d("kevin",pcname);
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }

        httpthread=new httpthread(this);

        globalVariable.writelog("TPactivity onCreate");
        DH=new DBHelper(this);
        fa=this;
        loadingbar=(ProgressBar) findViewById(R.id.progressBar_Spinner);
        loadingbar.setVisibility(View.INVISIBLE);
        noinfo=(TextView)findViewById(R.id.tvnoinfo);
        mRecyclerView=(RecyclerView)findViewById(R.id.tpcardrcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mytpListAdapter = new MyTPListAdapter();
        mRecyclerView.setAdapter(mytpListAdapter);
        noinfo.setText("");
        close=(ImageButton) findViewById(R.id.btclose);
        close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                globalVariable.writelog("TPactivity close");
               finish();

            }
        });
//        filterbt=(FloatingActionButton)findViewById(R.id.btfilter);
//        filterbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TPactivity.this);
//                View filterview = getLayoutInflater().inflate(R.layout.tpfilterview,null);
//
//                alertDialog.setView(filterview);
//                alertDialog.setPositiveButton("確定",(((dialog, which) -> {})));
//                alertDialog.setNegativeButton("清除",(((dialog, which) -> {})));
//                AlertDialog dialog = alertDialog.create();
//                dialog.show();
//                Spinner spinner =filterview.findViewById(R.id.startsp);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TPactivity.this, R.layout.myspinner) {
//
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//
//                        View v = super.getView(position, convertView, parent);
//                        if (position == getCount()) {
//                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
//                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
//                        }
//                        return v;
//                    }
//                    @Override
//                    public int getCount() {
//                        return super.getCount()-1; // you dont display last item. It is used as hint.
//                    }
//                };
//                adapter.setDropDownViewResource(R.layout.myspinner);
//                adapter.add(getString(R.string.Select));
//                spinner.setAdapter(adapter);
//                spinner.setSelection(adapter.getCount()); //display hint
//
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_6));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
//                    dialog.dismiss();
//                }));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_6));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
//                    dialog.dismiss();
//                }));
//            }
//        });
        globalVariable.setHttpreturn(false);
        loadingbar.setVisibility(View.VISIBLE);
        httpthread.tpinfopage(pcname);
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        initview("");
                        loadingbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        a.run();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin","TPactivity onStart");
    }
    public void initview(String filter){
//        tpcard=new View[globalVariable.getTpcount()];
//        tpcardinfo=new TextView[globalVariable.getTpcount()];
        tpinfoList.clear();
        try {
            SQLiteDatabase db = DH.getReadableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM " + "tpinfo" + filter, null);
            while (c.moveToNext()) {

                HashMap<String, String> tp = new HashMap<String, String>();

                tp.put("id", c.getString(1));
                tp.put("name", c.getString(2));
                tp.put("startTime", c.getString(3));
                tp.put("endTime", c.getString(4));
                tp.put("totalSeq", c.getString(5));
                tp.put("result", c.getString(6));
                tp.put("prograss", c.getString(7));

                Log.d("kevin", "tp:" + tp);
                tpinfoList.add(tp);
            }
            if (globalVariable.getTpcount() == 0) {
                if (!frist)
                    noinfo.setText("查無資料");
            } else {
                frist = true;
                noinfo.setText("");
                mytpListAdapter.notifyDataSetChanged();
            }
//        for(int i=0;i<globalVariable.getTpcount();i++) {
//            int finalI = i;
//            tpcard[i]=new View(this);
//            tpcardinfo[i]=new TextView(this);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            if(i!=0) {
//                lp.addRule(RelativeLayout.BELOW, tpcard[i - 1].getId());
//                lp.topMargin =30;
//            }
//            else {
//                lp.topMargin = 10;
//            }
//            lp.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
////            pccard[i].setBackground(getResources().getDrawable(R.drawable.overview));
//            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            tpcard[i]=inflater.inflate(R.layout.tpticard, null, true);
//            tpcard[i].setLayoutParams(lp);
//            tpcard[i].setOnClickListener(new Button.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent();
//                    intent.setClass(TPactivity.this, TIactivity.class);
//                    intent.putExtra("tp_id",tpinfoList.get(finalI).get("test_program_run_id"));
//                    intent.putExtra("ats_id",pcid);
//                    intent.putExtra("totalSeq",tpinfoList.get(finalI).get("totalSeq"));
//                    startActivity(intent);
//                }
//            });
//            tpcardinfo[i]=(TextView) tpcard[i].findViewById(R.id.cardinfo);
//            tprl.addView(tpcard[i]);
//            tpcard[i].setId(2000+i);
//            tpcardinfo[i].setText("TP Name:"+tpinfoList.get(i).get("tp_name")+"\nTI Count:"+tpinfoList.get(i).get("totalSeq")+"\nStart Time:"+tpinfoList.get(i).get("tp_start_time")+"\nEnd Time:"+tpinfoList.get(i).get("tp_end_time")+"\nStatus:"+tpinfoList.get(i).get("status")+"\nResult:"+tpinfoList.get(i).get("tp_result"));
//        }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kevin","TPactivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("kevin","TPactivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("kevin","TPactivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("kevin","TPactivity onResume");
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
    private class MyTPListAdapter extends RecyclerView.Adapter<MyTPListAdapter.ViewHolder>{


        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView cardinfo;

            private View mView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Log.d("kevin","ViewHolder");

                cardinfo = itemView.findViewById(R.id.cardinfo);
                mView  = itemView;
            }
        }
        @NonNull
        @Override
        public MyTPListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tpticard,parent,false);
            Log.d("kevin","onCreateViewHolder");
            return new MyTPListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyTPListAdapter.ViewHolder holder, int position) {
            Log.d("kevin", "onBindViewHolder");
            int finalI=position;
            SpannableString spannableString=new SpannableString(tpinfoList.get(position).get("result"));
            if(tpinfoList.get(position).get("result").equals("PASS"))
                spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#008000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else
                spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#ff0000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.cardinfo.setText("TP Name:"+tpinfoList.get(position).get("name")+"\nTI Count:"+tpinfoList.get(position).get("totalSeq")+"\nStart Time:"+tpinfoList.get(position).get("startTime")+"\nEnd Time:"+tpinfoList.get(position).get("endTime")+"\nprograss:"+tpinfoList.get(position).get("prograss")+"\nResult: ");
            holder.cardinfo.append(spannableString);
            holder.mView.setOnClickListener((v)->{
                globalVariable.writelog("TPactivity holder.mView.setOnClickListener");
                Intent intent = new Intent();
                intent.setClass(TPactivity.this, TIactivity.class);
                intent.putExtra("tp_id",tpinfoList.get(finalI).get("id"));
//                intent.putExtra("ats_id",pcid);

                startActivity(intent);

            });
        }

        @Override
        public int getItemCount() {
            return tpinfoList.size();
        }
    }
}
