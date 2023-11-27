package com.example.actms;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class analyzeactivity extends AppCompatActivity {
    GlobalVariable globalVariable;
    LineChart dailyChar,monthlyChar,yearlyChar;
    httpthread httpthread;
    ArrayList<String> dailydevicename = new ArrayList<>();
    ArrayList<String> monthlydevicename = new ArrayList<>();
    ArrayList<String> yearlydevicename = new ArrayList<>();
    ArrayList<String> dayx = new ArrayList<>();
    ArrayList<String> monthx = new ArrayList<>();
    ArrayList<String> yearx = new ArrayList<>();
    List<ArrayList<Entry>> dayy=new ArrayList<ArrayList<Entry>>();
    List<ArrayList<Entry>> monthy=new ArrayList<ArrayList<Entry>>();
    List<ArrayList<Entry>> yeary=new ArrayList<ArrayList<Entry>>();
    ArrayList<String> datayear = new ArrayList<>();
    ArrayList<String> datamonth = new ArrayList<>();
    TextView monitormessage;
    Spinner spdailyyear,spdailymonth,spmonthlyyear,spdailyfilter,spmonthlyfilter,spyearlyfilter;
    String datafrom,datato;
    List<ILineDataSet> dailysetlist=new ArrayList<ILineDataSet>() ;
    List<ILineDataSet> monthlysetlist=new ArrayList<ILineDataSet>() ;
    List<ILineDataSet> yearlysetlist=new ArrayList<ILineDataSet>() ;
    boolean dailyfrist=false;
    boolean island=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze_main);
        Configuration configuration = getResources().getConfiguration();
        if (savedInstanceState != null && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            island=true;
//            Log.d("kevin","monitoractivity island "+island);
        }else if (savedInstanceState != null && configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            island=false;
//            Log.d("kevin","monitoractivity island "+island);
        }
        globalVariable=(GlobalVariable) getApplication();
        globalVariable.writelog("analyzeactivity onCreate");
        spdailyyear=(Spinner)findViewById(R.id.spdailyyear);
        spdailymonth=(Spinner)findViewById(R.id.spdailymonth);
        spmonthlyyear=(Spinner)findViewById(R.id.spmonthlyyear);
        spdailyfilter=(Spinner)findViewById(R.id.spdailyfilter);
        spmonthlyfilter=(Spinner)findViewById(R.id.spmonthlyfilter);
        spyearlyfilter=(Spinner)findViewById(R.id.spyearlyfilter);
        dailyChar = findViewById(R.id.dailylineChart);
        monthlyChar = findViewById(R.id.monthlylineChart);
        yearlyChar = findViewById(R.id.yearlylineChart);

        monitormessage=(TextView)findViewById(R.id.monitormessage);
        httpthread=new httpthread(this);
        Calendar calendar=Calendar.getInstance();
//        Log.d("kevin",""+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        for(int i=0;i<11;i++){
            datayear.add(""+(calendar.get(Calendar.YEAR)-10+i));
        }
        for(int i=1;i<=12;i++){
            datamonth.add(""+i);
        }
        MySpinnerAdapter yearlist =new MySpinnerAdapter(datayear,analyzeactivity.this,0);
        spdailyyear.setAdapter(yearlist);
        spdailyyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                cal.set(Calendar.YEAR, Integer.valueOf(spdailyyear.getSelectedItem().toString()));
                cal.set(Calendar.MONTH, spdailymonth.getSelectedItemPosition());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                datato = sdFormat.format(cal.getTime());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                datafrom = sdFormat.format(cal.getTime());
                globalVariable.writelog("analyzeactivity spdailyyear "+datafrom+" to "+datato);
                if (!dailyfrist) {
                    globalVariable.setHttpreturn(false);
                    httpthread.tpdailyactivation(datafrom, datato);
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dailyfrist = true;
                                        initchar(false, cal.getActualMaximum(Calendar.DAY_OF_MONTH), dayx, dailydevicename, dayy, dailyChar);
                                        setChar(false, spdailyfilter, cal.getActualMaximum(Calendar.DAY_OF_MONTH), dailydevicename, dayx, dayy, dailyChar, globalVariable.getDailyList(), dailysetlist);
                                        dailyfrist = false;
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spdailyyear.setSelection(datayear.size()-1);
        spmonthlyyear.setAdapter(yearlist);
        spmonthlyyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                globalVariable.writelog("analyzeactivity spmonthlyyear "+spmonthlyyear.getSelectedItem().toString());
                globalVariable.setHttpreturn(false);
                httpthread.tpmonthlyactivation(spmonthlyyear.getSelectedItem().toString(),spmonthlyyear.getSelectedItem().toString(),"1","12");
                Thread a = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            while (!globalVariable.isHttpreturn()) ;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    initchar(false, 12, monthx, monthlydevicename, monthy, monthlyChar);
                                    setChar(false, spmonthlyfilter, 12, monthlydevicename, monthx, monthy, monthlyChar, globalVariable.getMonthlyList(), monthlysetlist);
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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spmonthlyyear.setSelection(datayear.size()-1);
        MySpinnerAdapter monthlist =new MySpinnerAdapter(datamonth,analyzeactivity.this,0);

        spdailymonth.setAdapter(monthlist);
        spdailymonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal=Calendar.getInstance();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                cal.set(Calendar.YEAR,Integer.valueOf(spdailyyear.getSelectedItem().toString()));
                cal.set(Calendar.MONTH,spdailymonth.getSelectedItemPosition());
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                datato=sdFormat.format(cal.getTime());
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                datafrom=sdFormat.format(cal.getTime());
                globalVariable.writelog("analyzeactivity spdailymonth "+datafrom+" to "+datato);
                if (!dailyfrist) {
                    globalVariable.setHttpreturn(false);
                    httpthread.tpdailyactivation(datafrom, datato);
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!globalVariable.isHttpreturn()) ;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dailyfrist = true;
                                        initchar(false, cal.getActualMaximum(Calendar.DAY_OF_MONTH), dayx, dailydevicename, dayy, dailyChar);
                                        setChar(false, spdailyfilter, cal.getActualMaximum(Calendar.DAY_OF_MONTH), dailydevicename, dayx, dayy, dailyChar, globalVariable.getDailyList(), dailysetlist);
                                        dailyfrist = false;
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        Log.d("kevin",""+calendar.get(Calendar.MONTH));
        spdailymonth.setSelection(calendar.get(Calendar.MONTH));

        globalVariable.setHttpreturn(false);
        httpthread.tpyearlyactivation("2013",datayear.get(datayear.size()-1));
        Thread a = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!globalVariable.isHttpreturn());
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            initchar(true, (calendar.get(Calendar.YEAR) - 2013 + 1), yearx, yearlydevicename, yeary, yearlyChar);
                            setChar(true, spyearlyfilter, (calendar.get(Calendar.YEAR) - 2013 + 1), yearlydevicename, yearx, yeary, yearlyChar, globalVariable.getYearlyList(), yearlysetlist);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###");//Y軸數值格式及小數點位數
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value)+"%";
        }
    }
    public void initchar(boolean yearflag,int size,ArrayList<String> x,ArrayList<String> name,List<ArrayList<Entry>> y,LineChart l){
        globalVariable.writelog("analyzeactivity initchar");
        try {
            x.clear();
            name.clear();
            y.clear();
            if (!yearflag) {
                for (int i = 1; i < size + 1; i++) {
                    x.add("" + i);
                }
            } else {
                for (int i = 2013; i < size + 2013; i++) {
                    x.add("" + i);
                }
            }
//        Log.d("kevin",""+x);
            XAxis xAxis = l.getXAxis();

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
            xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
            xAxis.setTextSize(10);//X軸標籤大小

            xAxis.setLabelCount(size);//X軸標籤個數
            xAxis.setSpaceMin(0f);//折線起點距離左側Y軸距離
            xAxis.setSpaceMax(0.2f);//折線終點距離右側Y軸距離

//        xAxis.setDrawGridLines(false);//不顯示每個座標點對應X軸的線 (預設顯示)
            xAxis.setGranularity(1F);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(x));

            YAxis rightAxis = l.getAxisRight();//獲取右側的軸線
            rightAxis.setEnabled(false);//不顯示右側Y軸
            YAxis leftAxis = l.getAxisLeft();//獲取左側的軸線

//        leftAxis.setDrawGridLines(false);
            leftAxis.setLabelCount(10);//Y軸標籤個數
            leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
            leftAxis.setTextSize(12);//Y軸標籤大小

            leftAxis.setAxisMinimum(0);//Y軸標籤最小值
            leftAxis.setAxisMaximum(100);//Y軸標籤最大值

            leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void setChar(boolean yearflag,Spinner sp,int size,ArrayList<String> name,ArrayList<String> x,List<ArrayList<Entry>> y,LineChart l,List<HashMap<String, String>> data,List<ILineDataSet> setlist){
        globalVariable.writelog("analyzeactivity setChar");
        try {
            for (int i = 0; i < data.size(); i++) {
                int index = Integer.valueOf(data.get(i).get("date").split("-")[data.get(i).get("date").split("-").length - 1]);
//            Log.d("kevin","index:"+index);
//            Log.d("kevin","ipcName:"+data.get(i).get("ipcName"));
                if (name.indexOf(data.get(i).get("ipcName")) == -1) {
                    name.add(data.get(i).get("ipcName"));
                    ArrayList<Entry> yData = new ArrayList<>();
                    for (int j = 0; j < size; j++) {
                        yData.add(new Entry(j, 0));
                    }
                    if (!yearflag) {
                        yData.set(index - 1, new Entry(index - 1, Float.valueOf(data.get(i).get("activation")) * 100));
                    } else {
                        yData.set(index - 2013, new Entry(index - 2013, Float.valueOf(data.get(i).get("activation")) * 100));
                    }
                    y.add(yData);
                } else {
                    int id = name.indexOf(data.get(i).get("ipcName"));
                    if (!yearflag)
                        y.get(id).set(index - 1, new Entry(index - 1, Float.valueOf(data.get(i).get("activation")) * 100));
                    else
                        y.get(id).set(index - 2013, new Entry(index - 2013, Float.valueOf(data.get(i).get("activation")) * 100));
                }
//            Log.d("kevin",""+y);
            }

            setlist.clear();
            if (y.size() > 0) {

//            Log.d("kevin",""+y.size());
                for (int i = 0; i < y.size(); i++) {
                    LineDataSet set;
//                Log.d("kevin",name.get(i)+":"+y.get(i));
                    set = new LineDataSet(y.get(i), name.get(i));
                    set.setMode(LineDataSet.Mode.LINEAR);//類型為折線

                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    set.setColor(color);//線的顏色
                    set.setCircleColor(color);//圓點顏色
                    set.setCircleRadius(4);//圓點大小
                    set.setDrawCircleHole(false);//圓點為實心(預設空心)

                    set.setLineWidth(1.5f);//線寬
                    set.setDrawValues(false);//顯示座標點對應Y軸的數字(預設顯示)
                    set.setValueTextSize(8);//座標點數字大小
                    set.setHighlightEnabled(true);
                    Legend legend = l.getLegend();
                    legend.setTextColor(Color.parseColor("#000000"));
                    legend.setWordWrapEnabled(true);

                    Description description = l.getDescription();
                    description.setEnabled(false);//不顯示Description Label (預設顯示)
                    setlist.add(set);
                }
                LineData linedata = new LineData(setlist);

                MyMarkerView mv = new MyMarkerView(this, R.layout.markerview, x, l);//設置點擊標籤
                l.setMarker(mv);//設置點擊標籤


                l.setData(linedata);//一定要放在最後
                if (!island)
                    l.setVisibleXRange(0F, 9F);
                else
                    l.setVisibleXRange(0F, 20F);
            } else {

//                LineData linedata = new LineData();
//                l.setData(linedata);
                l.clear();
                l.setNoDataText("暫時沒有數據");
                l.setNoDataTextColor(Color.BLACK);//文字顏色
            }
            l.notifyDataSetChanged();
            l.invalidate();

            ArrayList<categorymodel> categoryModelArrayList = new ArrayList<>();
            name.add(0, "全選");
            for (String s : name) {
                categorymodel categoryModel = new categorymodel();
                categoryModel.setTitle(s);
                categoryModel.setSelected(true);
                categoryModelArrayList.add(categoryModel);
            }
            if(categoryModelArrayList.size()>0) {
                myadapter myAdapter = new myadapter(this, 0, categoryModelArrayList, l, setlist, name);
                sp.setAdapter(myAdapter);
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        try {
            Log.d("kevin", "Configuration " + newConfig.orientation);
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // 什麼都不用寫
                Log.d("kevin", "Configuration.ORIENTATION_LANDSCAPE");
                dailyChar.setVisibleXRange(0F, 20F);
                monthlyChar.setVisibleXRange(0F, 20F);
//                    monthlyChar,yearlylineChar
            } else {
                // 什麼都不用寫
                Log.d("kevin", "Configuration.ORIENTATION_LANDSCAPE 1");
                dailyChar.setVisibleXRange(0F, 9F);
                monthlyChar.setVisibleXRange(0F, 9F);
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }

    public class MyMarkerView extends MarkerView {

        private TextView tvValue, tvTitle;
        private ArrayList<String> customxLable;
        private LineChart chart;

        public MyMarkerView(Context context, int layoutResource, ArrayList<String> customxLable,LineChart chart) {
            super(context, layoutResource);
            this.customxLable = customxLable;
            this.chart = chart;
            tvTitle = findViewById(R.id.textView_Title);
            tvValue = findViewById(R.id.textView_Value);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            globalVariable.writelog("analyzeactivity refreshContent");
            try {
                tvTitle.setText(customxLable.get(Math.round(e.getX())));
                int line = chart.getLineData().getDataSets().size();

                StringBuffer value = new StringBuffer();
                for (int i = 0; i < line; i++) {
                    ILineDataSet set = chart.getLineData().getDataSets().get(i);

                    if ((set.getEntryForIndex(Math.round(e.getX())).getY() == e.getY())&&set.isVisible()) {
                        String s = set.getLabel() + ": " + set.getEntryForIndex(Math.round(e.getX())).getY();
                        if (i < line - 1) value.append(s + "\n");
                        else value.append(s);
                    }
                }
                tvValue.setText(value);
//            tvTitle.setText(chart.getLineData().getDataSets().get(highlight.getDataSetIndex()).getLabel());

            }catch (Exception ex){
                ex.printStackTrace();
                globalVariable.writeexception(ex);
            }
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
//            Log.d("kevin","getOffsetForDrawingAtPoint:"+posX+";"+posY);
//            Log.d("kevin","getOffsetForDrawingAtPoint:"+getWidth()+";"+getHeight());
            Float x,y;
            if (posX < 200)
                x=-getWidth() / 2f+170f;
            else
                x=-getWidth() / 2f-100f;
            if(posX>1000)
                x-=80f;
            if(posY<200)
                y=-getHeight()+150f;
            else
                y=-getHeight()-10f;
            Log.d("kevin","getOffsetForDrawingAtPoint:"+x+";"+y);
            return new MPPointF(x, y);

        }
    }
    public class myadapter extends ArrayAdapter<categorymodel> {
        private Context mContext;
        private ArrayList<categorymodel> listState;
        private ArrayList<String> labellist;
        private myadapter myAdapter;
        private LineChart lineChart;
        private List<ILineDataSet> lineDataSets;
        private boolean isFromView = false;

        public myadapter(Context context, int resource, ArrayList<categorymodel> objects,LineChart line,List<ILineDataSet> set,ArrayList<String> name) {
            super(context, resource, objects);
            this.mContext = context;
            this.listState = (ArrayList<categorymodel>) objects;
            this.lineChart = line;
            this.lineDataSets = set;
            this.myAdapter = this;
            this.labellist = name;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            final myadapter.ViewHolder holder;

            try {
                globalVariable.writelog("analyzeactivity getCustomView");
                if (convertView == null) {
                    LayoutInflater layoutInflator = LayoutInflater.from(mContext);
                    convertView = layoutInflator.inflate(R.layout.spinner_checkbox, null);
                    holder = new myadapter.ViewHolder();
//                holder.mTextView = (TextView) convertView
//                        .findViewById(R.id.text);
                    holder.mCheckBox = (CheckBox) convertView
                            .findViewById(R.id.checkbox);
                    convertView.setTag(holder);
                } else {
                    holder = (myadapter.ViewHolder) convertView.getTag();
                }

                holder.mCheckBox.setText(listState.get(position).getTitle());
                // To check weather checked event fire from getview() or user input
                isFromView = true;
                holder.mCheckBox.setChecked(listState.get(position).isSelected());
                isFromView = false;
                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mCheckBox.setTag(position);
                holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        globalVariable.writelog("analyzeactivity onCheckedChanged "+position);
                        try {
                            if (!isFromView) {
                                listState.get(position).setSelected(isChecked);
                                Log.i("checkpos", "" + listState.get(position).getTitle());
                                Log.i("valueif", "check" + isFromView);
//                    Toast.makeText(getContext(), "" + listState.get(position).isSelected(), Toast.LENGTH_LONG).show();
                                if (listState.get(position).isSelected()) {
                                    if (position == 0) {
                                        for (int i = 1; i < listState.size(); i++) {
                                            listState.get(i).setSelected(true);
                                            lineDataSets.get(i - 1).setVisible(true);
//                                        lineDataSets.get(i - 1).setLabel(labellist.get(i));
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        lineDataSets.get(position - 1).setVisible(true);
//                                    lineDataSets.get(position - 1).setLabel(labellist.get(position));
                                    }

                                } else {
                                    if (position == 0) {
                                        for (int i = 1; i < listState.size(); i++) {
                                            listState.get(i).setSelected(false);
                                            lineDataSets.get(i - 1).setVisible(false);
//                                        lineDataSets.get(i - 1).setLabel("");
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        lineDataSets.get(position - 1).setVisible(false);
//                                    lineDataSets.get(position - 1).setLabel("");

                                    }

                                }
                                boolean flag = true;
                                for (int i = 1; i < listState.size(); i++) {
                                    flag = flag && listState.get(i).isSelected();
                                }
                                listState.get(0).setSelected(flag);
                                myAdapter.notifyDataSetChanged();
                                lineChart.notifyDataSetChanged();

                                lineChart.invalidate();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            globalVariable.writeexception(e);
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                globalVariable.writeexception(e);
            }
            return convertView;
        }

        private class ViewHolder {
//            private TextView mTextView;
            public CheckBox mCheckBox;
        }
    }

}
