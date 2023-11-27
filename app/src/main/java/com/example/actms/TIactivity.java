package com.example.actms;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TIactivity extends AppCompatActivity {
//    View[] ticard;
//    TextView[] ticardinfo;
    TextView noinfo;
    RelativeLayout tirl;
    ImageButton close;
    httpthread httpthread;
    GlobalVariable globalVariable;
    String tpid="";
//    String pcid="";
//    int totalseq=0;
    DBHelper DH=null;
    ProgressBar loadingbar;
    RecyclerView mRecyclerView;
    MyTIListAdapter mytiListAdapter;
    List<HashMap<String, String>> tiinfoList=new ArrayList<HashMap<String, String>>();
    boolean frist=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("kevin","TIactivity onCreate");
        setContentView(R.layout.ti_main);
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("TIactivity onCreate");
        Intent intent = getIntent();
        try {
            tpid = intent.getStringExtra("tp_id");
//            pcid = intent.getStringExtra("ats_id");
//            totalseq = Integer.valueOf(intent.getStringExtra("totalSeq"));
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
//        Log.d("kevin",""+totalseq);
        httpthread=new httpthread(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.ticardrcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mytiListAdapter = new MyTIListAdapter();
        mRecyclerView.setAdapter(mytiListAdapter);
        DH=new DBHelper(this);
        loadingbar=(ProgressBar) findViewById(R.id.progressBar_Spinner);
        loadingbar.setVisibility(View.INVISIBLE);
        tirl=(RelativeLayout) findViewById(R.id.tirl);
        close=(ImageButton) findViewById(R.id.btclose);
        noinfo=(TextView)findViewById(R.id.tvnoinfo);
        noinfo.setText("");
        close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                TPactivity.fa.finish();
                globalVariable.writelog("TIactivity close");
                finish();

            }
        });
        globalVariable.setHttpreturn(false);
        loadingbar.setVisibility(View.VISIBLE);
        httpthread.tiinfopage(tpid);
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
        Log.d("kevin","TIactivity onStart");
    }
    public void initview(String filter){
//        ticard=new View[totalseq];
//        ticardinfo=new TextView[totalseq];
        Log.d("kevin","TIactivity initview "+filter);
        globalVariable.writelog("TIactivity initview "+filter);
        try {
            SQLiteDatabase db = DH.getReadableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM " + "tiinfo" + filter, null);
            while (c.moveToNext()) {

                HashMap<String, String> ti = new HashMap<String, String>();

                ti.put("id", c.getString(1));

                ti.put("seq", c.getString(2));
                ti.put("name", c.getString(3));
                ti.put("ext_name", c.getString(4));
                ti.put("start_time", c.getString(5));
                ti.put("end_time", c.getString(6));
                ti.put("result", c.getString(7));

                Log.d("kevin", "tp:" + ti);
                tiinfoList.add(ti);
            }
            if (globalVariable.getTicount() == 0) {
                if (!frist)
                    noinfo.setText("查無資料");
            } else {
                frist = true;
                noinfo.setText("");
                mytiListAdapter.notifyDataSetChanged();
            }
//        if(totalseq==0)
//            noinfo.setText("查無資料");
//        for(int i=0;i<totalseq;i++) {
//            ticard[i]=new View(this);
//            ticardinfo[i]=new TextView(this);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            if(i!=0) {
//                lp.addRule(RelativeLayout.BELOW, ticard[i - 1].getId());
//                lp.topMargin =30;
//            }
//            else {
//                lp.topMargin = 10;
//            }
//            lp.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
////            pccard[i].setBackground(getResources().getDrawable(R.drawable.overview));
//            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            ticard[i]=inflater.inflate(R.layout.tpticard, null, true);
//            ticardinfo[i]=(TextView) ticard[i].findViewById(R.id.cardinfo);
//            ticard[i].setLayoutParams(lp);
//            tirl.addView(ticard[i]);
//            ticard[i].setId(2000+i);
//            ticardinfo[i].setText("TI Seq:"+tiinfoList.get(i).get("seq")+"\nTI Name:"+tiinfoList.get(i).get("name")+"\nTI Name(Ext.):"+tiinfoList.get(i).get("ext_name")+"\nStart Time:"+tiinfoList.get(i).get("start_time")+"\nEnd Time:"+tiinfoList.get(i).get("end_time")+"\nResult:"+tiinfoList.get(i).get("pass"));
//        }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kevin","TIactivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("kevin","TIactivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("kevin","TIactivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("kevin","TIactivity onResume");
    }
    private class MyTIListAdapter extends RecyclerView.Adapter<MyTIListAdapter.ViewHolder>{


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
        public MyTIListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tpticard,parent,false);
            Log.d("kevin","onCreateViewHolder");
            return new MyTIListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyTIListAdapter.ViewHolder holder, int position) {
            Log.d("kevin", "onBindViewHolder");
            SpannableString spannableString=new SpannableString(tiinfoList.get(position).get("result"));
            if(tiinfoList.get(position).get("result").equals("PASS"))
                spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#008000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else
                spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#ff0000")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.cardinfo.setText("TI Seq:"+tiinfoList.get(position).get("seq")+"\nTI Name:"+tiinfoList.get(position).get("name")+"\nTI Name(Ext.):"+tiinfoList.get(position).get("ext_name")+"\nStart Time:"+tiinfoList.get(position).get("start_time")+"\nEnd Time:"+tiinfoList.get(position).get("end_time")+"\nResult:");
            holder.cardinfo.append(spannableString);
        }

        @Override
        public int getItemCount() {
            return tiinfoList.size();
        }
    }
}
