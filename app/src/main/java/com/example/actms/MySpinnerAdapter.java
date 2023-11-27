package com.example.actms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class MySpinnerAdapter extends ArrayAdapter {

    private List<String> myList;
    private List<HashMap<String, String>> myList1;
    private Context context;
    private int type;
    private String tagname;


    public MySpinnerAdapter(List<String> l , Context context, int t) {
        super(context,0, l);
        type=t;
        myList = l;
        this.context=context;
    }
    public MySpinnerAdapter(List<HashMap<String, String>> l , String tag, Context context, int t) {
        super(context,0, l);
        type=t;
        myList1 = l;
        tagname=tag;
        this.context=context;
    }
    @Override
    public int getCount() {
        if(type==0)
            return myList.size();
        else
            return myList1.size();
    }

    @Override
    public Object getItem(int position) {
        if(type==0)
            return myList.get(position);
        else
            return myList1.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return createView(position,convertView,parent);
    }
    private View createView(int position, View convertView
            , ViewGroup parent){

        if (convertView == null)
            convertView =  LayoutInflater.from(getContext()).inflate(
                    R.layout.myspinner, parent, false);

        TextView text1 = (TextView) convertView.findViewById(R.id.sptext);
        if(type==0) {
            String myObj = myList.get(position);

            text1.setText(myObj);
            text1.setSelected(true);
        }else{
            String myObj = myList1.get(position).get(tagname);

            text1.setText(myObj);
            text1.setSelected(true);
        }
        return convertView;
    }//複寫介面
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position,convertView,parent);
    }

}
