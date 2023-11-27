package com.example.actms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import weekview.DayAdapter;
import weekview.DayBean;

public class taskmain extends AppCompatActivity {


    private TextView tvCurrentDate;

    ImageButton prem,nextm;
    private GridView gv;
    httpthread httpthread;
    GlobalVariable globalVariable;
    Spinner spinner;
    String ipcid="";
    List<String> daylist=new ArrayList<String>();
    List<DayBean> dataList = new ArrayList<>();
    DayAdapter adapter;
    String selday="";
    ImageButton create;
    TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_main);
        Log.d("kevin","taskmain onCreate");
        globalVariable=(GlobalVariable)getApplicationContext();
        globalVariable.writelog("taskmain onCreate");
        httpthread=new httpthread(this);
        spinner=(Spinner)findViewById(R.id.spfilter) ;
        tableLayout=(TableLayout)findViewById(R.id.tablelayout);
        tableLayout.setStretchAllColumns(true);
        create=(ImageButton) findViewById(R.id.btcreate) ;
        create.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
            try {
                globalVariable.writelog("taskmain create");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(taskmain.this);
                View filterview = getLayoutInflater().inflate(R.layout.taskinfo, null);

                alertDialog.setView(filterview);
                alertDialog.setPositiveButton("新增", (((dialog, which) -> {
                })));
                alertDialog.setNegativeButton("取消", (((dialog, which) -> {
                })));
                AlertDialog dialog = alertDialog.create();
                dialog.show();

                TextView tvtitle = filterview.findViewById(R.id.tvtitle);
                TextView tvdatesel = filterview.findViewById(R.id.tvdatesel);
                TextView tvstarttimesel = filterview.findViewById(R.id.tvstarttimesel);
                TextView tvendtimesel = filterview.findViewById(R.id.tvendtimesel);
                EditText edtpname = filterview.findViewById(R.id.edtpname);
                EditText edtpversion = filterview.findViewById(R.id.edtpversion);
                EditText edusername = filterview.findViewById(R.id.edusername);
                EditText eddpname = filterview.findViewById(R.id.eddpname);
                EditText edremark = filterview.findViewById(R.id.edremark);
                tvtitle.setText("Add Task");
                tvdatesel.setText(selday);
                tvdatesel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(taskmain.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day);
                                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = dateformat.format(calendar.getTime());

                                tvdatesel.setText(strDate);
                            }
                        }, year, month, day).show();
                    }
                });
                tvstarttimesel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        new TimePickerDialog(taskmain.this, 1, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, min);
                                SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
                                String strDate = dateformat.format(calendar.getTime());

                                tvstarttimesel.setText(strDate);
                            }
                        }, hour, minute, true).show();

                    }
                });
                tvendtimesel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        new TimePickerDialog(taskmain.this, 1, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, min);
                                SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
                                String strDate = dateformat.format(calendar.getTime());

                                tvendtimesel.setText(strDate);
                            }
                        }, hour, minute, true).show();

                    }
                });
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
                    globalVariable.writelog("taskmain create ok");
                    globalVariable.setHttpreturn(false);
                    httpthread.createschedule(ipcid, edtpname.getText().toString(), tvdatesel.getText().toString() + " " + tvstarttimesel.getText().toString(), tvdatesel.getText().toString() + " " + tvendtimesel.getText().toString(), edusername.getText().toString(), eddpname.getText().toString(), edtpversion.getText().toString(), edremark.getText().toString());
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!globalVariable.isHttpreturn()) ;
                            globalVariable.setHttpreturn(false);
                            httpthread.getscheldule(ipcid);
                            while (!globalVariable.isHttpreturn()) ;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    settextview();
                                    settaskday();
                                }
                            });
                        }
                    });
                    a.run();
                    dialog.dismiss();
                }));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
                    dialog.dismiss();

                }));
                }catch (Exception e){
                    e.printStackTrace();
                    globalVariable.writeexception(e);
                }
            }
        });

        // 初始化布局

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin","taskmain onStart");
        globalVariable.writelog("taskmain onStart");
        Date date=new Date();
        selday=getFormatTime("yyyy-MM-dd",date);
        initView();
        settextview();

        globalVariable.setHttpreturn(false);
        httpthread.getipcs("","","","");
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!globalVariable.isHttpreturn()) ;
                runOnUiThread(new Runnable() {
                    public void run() {
                        globalVariable.setHttpreturn(false);
                        httpthread.getipcs("","","",String.valueOf(globalVariable.getTotalcount()));
                        Thread a1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
//                                        httpthread.getipcs("","","",String.valueOf(globalVariable.getTotalcount()));

                                        initspinner();

                                    }
                                });
                            }
                        });
                        a1.run();


                    }
                });
            }
        });
        a.run();

    }
    public void inithead(){
        try {
            TableRow row = new TableRow(getApplicationContext());
            row.addView(puttpdataintv("Start"));
            row.addView(puttpdataintv("End"));
            row.addView(puttpdataintv("TP name"));
            row.setDividerDrawable(getDrawable(R.drawable.table_divider));
            row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
            row.setBackgroundColor(Color.parseColor("#98fb98"));
            tableLayout.addView(row);
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
    public void initspinner(){
        try {
            MySpinnerAdapter myAdapter = new MySpinnerAdapter(globalVariable.getIpclist(), "name", taskmain.this, 1);
//                        myAdapter.setDropDownViewResource(R.layout.myspinner);
            spinner.setAdapter(myAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ipcid = globalVariable.getIpclist().get(position).get("id");
                    globalVariable.setHttpreturn(false);
                    httpthread.getscheldule(ipcid);
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!globalVariable.isHttpreturn()) ;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Date date = new Date();
                                    selday = getFormatTime("yyyy-MM-dd", date);
                                    settextview();
                                    settaskday();
                                }
                            });
                        }
                    });
                    a.run();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }


    private void initView() {
        tvCurrentDate = (TextView) findViewById(R.id.tvCurrentDate);
        prem=(ImageButton)findViewById(R.id.btpremonth);
        nextm=(ImageButton)findViewById(R.id.btnextmonth);

        gv = (GridView) findViewById(R.id.gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                globalVariable.writelog("taskmain initView gv onclick");
//                Log.d("kevin",""+daylist.get(i));
//                Log.d("kevin",""+i);
                selday=daylist.get(i);
                settextview();
            }
        });
        // 初始化适配器
        initAdapter();
    }
    public void settextview(){
        tableLayout.removeAllViews();

        inithead();
        try {
            for (int i = 0; i < globalVariable.getPcschedulelist().size(); i++) {

                if (selday.equals(globalVariable.getPcschedulelist().get(i).get("planStartTime").split(" ")[0])) {
                    final int pos=i;
                    TableRow row = new TableRow(getApplicationContext());
                    row.addView(puttpdataintv(globalVariable.getPcschedulelist().get(i).get("planStartTime").split(" ")[1]));
                    row.addView(puttpdataintv(globalVariable.getPcschedulelist().get(i).get("planEndTime").split(" ")[1]));
                    row.addView(puttpdataintv( globalVariable.getPcschedulelist().get(i).get("testProgramName")));


                    row.setDividerDrawable(getDrawable(R.drawable.table_divider));
                    row.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING|TableRow.SHOW_DIVIDER_MIDDLE|TableRow.SHOW_DIVIDER_END);
                    row.setBackgroundColor(Color.parseColor("#ffffff"));
                    row.setOnClickListener((v)->{
                        globalVariable.writelog("taskmain row onclick");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(taskmain.this);
                        View filterview = getLayoutInflater().inflate(R.layout.taskinfo,null);

                        alertDialog.setView(filterview);
                        alertDialog.setPositiveButton("刪除",(((dialog, which) -> {})));
                        alertDialog.setNegativeButton("取消",(((dialog, which) -> {})));
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();

                        TextView tvtitle=filterview.findViewById(R.id.tvtitle);
                        TextView tvdatesel=filterview.findViewById(R.id.tvdatesel);
                        TextView tvstarttimesel=filterview.findViewById(R.id.tvstarttimesel);
                        TextView tvendtimesel=filterview.findViewById(R.id.tvendtimesel);
                        EditText edtpname=filterview.findViewById(R.id.edtpname);
                        EditText edtpversion=filterview.findViewById(R.id.edtpversion);
                        EditText edusername=filterview.findViewById(R.id.edusername);
                        EditText eddpname=filterview.findViewById(R.id.eddpname);
                        EditText edremark=filterview.findViewById(R.id.edremark);
                        tvtitle.setText("Task Info");
                        tvdatesel.setText(selday);
                        tvstarttimesel.setText(globalVariable.getPcschedulelist().get(pos).get("planStartTime").split(" ")[1].substring(0,5));
                        tvendtimesel.setText(globalVariable.getPcschedulelist().get(pos).get("planEndTime").split(" ")[1].substring(0,5));
                        edtpname.setText(globalVariable.getPcschedulelist().get(pos).get("testProgramName"));
                        edtpname.setEnabled(false);
                        edtpversion.setText(globalVariable.getPcschedulelist().get(pos).get("testProgramVersion"));
                        edtpversion.setEnabled(false);
                        edusername.setText(globalVariable.getPcschedulelist().get(pos).get("userName"));
                        edusername.setEnabled(false);
                        eddpname.setText(globalVariable.getPcschedulelist().get(pos).get("departmentName"));
                        eddpname.setEnabled(false);
                        edremark.setText(globalVariable.getPcschedulelist().get(pos).get("remark"));
                        edremark.setEnabled(false);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v1 -> {
                            globalVariable.writelog("taskmain row onclick ok");
                            globalVariable.setHttpreturn(false);
                            httpthread.deleteschedule(globalVariable.getPcschedulelist().get(pos).get("id"));

                            Thread a = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (!globalVariable.isHttpreturn()) ;
                                    globalVariable.setHttpreturn(false);
                                    httpthread.getscheldule(ipcid);
                                    while (!globalVariable.isHttpreturn()) ;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            settextview();
                                            settaskday();
                                        }
                                    });
                                }
                            });
                            a.run();
                            dialog.dismiss();
                        }));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(getResources().getDimension(R.dimen.sp_15));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v1 -> {
                            dialog.dismiss();

                        }));
                    });
                    tableLayout.addView(row);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    private void initAdapter() {

        adapter = new DayAdapter(dataList, this);
        gv.setAdapter(adapter);

        // 拿到日历对象，动态设置时间
        // 使用日历对象可以帮我们避免一些问题，如 月数 的临界点问题，到的 12 月是再加 1 的话会自动
        // 帮我们加到下一年去，同理从 1 月到 12 月也一样。
        final Calendar calendar = Calendar.getInstance();
        try {
            setCurrentData(calendar);

            updateAdapter(calendar, dataList, adapter);

            prem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                    updateAdapter(calendar, dataList, adapter);
                    settaskday();
                }
            });

            nextm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                    updateAdapter(calendar, dataList, adapter);
                    settaskday();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }

    private void updateAdapter(Calendar calendar, List<DayBean> dataList, DayAdapter adapter) {
        globalVariable.writelog("taskmain updateAdapter");
        dataList.clear();
        daylist.clear();
        try {
            setCurrentData(calendar);
            // 得到本月一号的星期索引
            // 索引从 1 开始，第一个为星期日,减1是为了与星期对齐，如星期一对应索引1，星期二对应索引二
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;


            // 将日期设为上个月
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            int preMonthDays = getMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            // 拿到上一个月的最后几天的天数
            for (int i = 0; i < weekIndex; i++) {
                DayBean bean = new DayBean();
                String day = "";
                bean.setYear(calendar.get(Calendar.YEAR));
                bean.setMonth(calendar.get(Calendar.MONTH) + 1);
                bean.setDay(preMonthDays - weekIndex + i + 1);
                bean.setCurrentDay(false);
                bean.setCurrentMonth(false);
                bean.setHastask(false);
                day = calendar.get(Calendar.YEAR) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String.format("%02d", (preMonthDays - weekIndex + i + 1));
                dataList.add(bean);
                daylist.add(day);
            }

            // 将日期设为当月
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            int currentDays = getMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            // 拿到当月的天数
            for (int i = 0; i < currentDays; i++) {
                DayBean bean = new DayBean();
                String day = "";
                bean.setYear(calendar.get(Calendar.YEAR));
                bean.setMonth(calendar.get(Calendar.MONTH) + 1);
                bean.setDay(i + 1);
                // 当前日期
                String nowDate = getFormatTime("yyyy-MM-d", Calendar.getInstance().getTime());
                // 选择的日期
                String selectDate = getFormatTime("yyyy-MM-", calendar.getTime()) + (i + 1);

                // 假如相等的话，那么就是今天的日期了
                if (nowDate.contentEquals(selectDate)) {
                    bean.setCurrentDay(true);
                    selday = calendar.get(Calendar.YEAR) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String.format("%02d", (i + 1));
                } else {
                    bean.setCurrentDay(false);
                }
                bean.setCurrentMonth(true);
                bean.setHastask(false);
                day = calendar.get(Calendar.YEAR) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String.format("%02d", (i + 1));
                dataList.add(bean);
                daylist.add(day);
            }

            // 拿到下个月第一周的天数
            // 先拿到下个月第一天的星期索引
            // 之前设为了1号，所以将日历对象的月数加 1 就行了
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        Log.d("kevin",""+weekIndex);
            if (weekIndex > 0) {
                for (int i = 0; i < 7 - weekIndex; i++) {
                    DayBean bean = new DayBean();
                    String day = "";
                    bean.setYear(calendar.get(Calendar.YEAR));
                    bean.setMonth(calendar.get(Calendar.MONTH) + 1);
                    bean.setDay(i + 1);
                    bean.setCurrentDay(false);
                    bean.setCurrentMonth(false);
                    bean.setHastask(false);
                    day = calendar.get(Calendar.YEAR) + "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String.format("%02d", (i + 1));
                    dataList.add(bean);
                    daylist.add(day);
                }
            }

            adapter.notifyDataSetChanged();
            // 最后将日期设为当月
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    // 设置当前的时间
    private void setCurrentData(Calendar calendar) {
        tvCurrentDate.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
    }
    // 判断是否为闰年
    public boolean isRunYear(int y) {
        return y % 4 == 0 && y % 100 != 0 || y % 400 == 0;
    }
    // 格式化时间，设置时间很方便，也比较简单，学的很快
    public static String getFormatTime(String p, Date t) {
        return new SimpleDateFormat(p, Locale.CHINESE).format(t);
    }
    // 传入年和月得出当月的天数
    public int getMonth(int m, int y) {
        switch (m) {
            case 2:
                return isRunYear(y) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }
    public void settaskday(){
        try {
            for (int a = 0; a < dataList.size(); a++)
                dataList.get(a).setHastask(false);
            for (int j = 0; j < globalVariable.getPcschedulelist().size(); j++) {
                int i = daylist.indexOf(globalVariable.getPcschedulelist().get(j).get("planStartTime").split(" ")[0]);
                if (i != -1) {
                    dataList.get(i).setHastask(true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
        adapter.notifyDataSetChanged();
    }
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.d("kevin","Configuration "+newConfig.orientation);
        Date date=new Date();
        selday=getFormatTime("yyyy-MM-dd",date);
        settextview();
    }
}


