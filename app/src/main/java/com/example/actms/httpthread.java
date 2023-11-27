package com.example.actms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import treeview.Node;

public class httpthread {

    Context context;
    GlobalVariable globalVariable;
    DBHelper DH=null;
//    String hostname="http://10.136.217.184:50063";
    String hostname="http://10.136.217.185:50063";

    public httpthread(Context c){
        context=c;
        DH=new DBHelper(c);
        globalVariable=(GlobalVariable)context.getApplicationContext();
    }
    private void addpc(JSONObject jsonObject){
        try {
            SQLiteDatabase sQLiteDatabase = DH.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("nVId", jsonObject.getInt("nVId"));
            contentValues.put("sPcName", jsonObject.get("sPcName").toString());
            contentValues.put("sConnect", jsonObject.get("sConnect").toString());
            contentValues.put("sUpdateTime", jsonObject.get("sUpdateTime").toString().replace("T"," "));
            contentValues.put("sLastTpName", jsonObject.get("sLastTpName").toString());
            contentValues.put("sConfig", jsonObject.get("sConfig").toString());
            contentValues.put("sRunId", jsonObject.get("sRunId").toString());
            contentValues.put("sRunStatus", jsonObject.get("sRunStatus").toString());
            contentValues.put("sProgress", jsonObject.get("sProgress").toString());
            contentValues.put("sTpResult", jsonObject.get("sTpResult").toString());
            sQLiteDatabase.insert("pcinfo", null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
            globalVariable.writeexception(exception);
        }
    }
    private void updatapc(JSONObject jsonObject){
        try {
            globalVariable.writelog("http updatapc");
            SQLiteDatabase sQLiteDatabase = DH.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if(jsonObject.has("name"))
                contentValues.put("name", jsonObject.get("name").toString());
            if(jsonObject.has("id"))
                contentValues.put("id", jsonObject.getInt("id"));
            if(jsonObject.has("computerName"))
                contentValues.put("computerName", jsonObject.get("computerName").toString());
            if(jsonObject.has("updateTime"))
                contentValues.put("updateTime", jsonObject.get("updateTime").toString().replace("T"," "));
            if(!jsonObject.get("lastTestProgramData").equals(null)) {
                Log.d("kevin", "lastTestProgramData "+new JSONObject(jsonObject.get("lastTestProgramData").toString()).has("name"));
                if (!new JSONObject(jsonObject.get("lastTestProgramData").toString()).has("name"))
                    contentValues.put("LastTpName", "");
                else
                    contentValues.put("LastTpName", new JSONObject(jsonObject.get("lastTestProgramData").toString()).get("name").toString());
            }else
                contentValues.put("LastTpName", "");
            if(jsonObject.has("status"))
                contentValues.put("status", jsonObject.get("status").toString());
            if(!jsonObject.get("lastTestProgramData").equals(null))
                contentValues.put("LastTpresult", new JSONObject(jsonObject.get("lastTestProgramData").toString()).get("result").toString());
            else
                contentValues.put("LastTpresult", "");
            if(!jsonObject.get("lastTestProgramData").equals(null))
                contentValues.put("LastTpProgress", new JSONObject(jsonObject.get("lastTestProgramData").toString()).get("prograss").toString());
            else
                contentValues.put("LastTpProgress", "0");

            Cursor c = sQLiteDatabase.rawQuery(" SELECT * FROM " + "pcinfo where id="+jsonObject.getInt("id"), null);
            String vid="-1";
            while (c.moveToNext()){
                Log.d("kevin", c.getString(0));
                vid=c.getString(0);
            }
            Log.d("kevin",vid);
            if(!vid.equals("-1"))
                sQLiteDatabase.update("pcinfo",  contentValues,"_id="+vid,null);
            else
                sQLiteDatabase.insert("pcinfo", null, contentValues);
//            if(vid.equals("-1"))
//                sQLiteDatabase.insert("pcinfo", null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
            globalVariable.writeexception(exception);
        }
    }
    public void deleteAllpc() {
        globalVariable.writelog("http deleteAllpc");
        SQLiteDatabase sQLiteDatabase = this.DH.getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM pcinfo");
        sQLiteDatabase.close();
    }
    private void addtp(JSONObject jsonObject){
        try {
            globalVariable.writelog("http addtp");
            SQLiteDatabase sQLiteDatabase = DH.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
//            contentValues.put("systemTime", jsonObject.get("systemTime").toString().replace("T"," "));
            if(jsonObject.has("id"))
                contentValues.put("id", jsonObject.getInt("id"));
            if(jsonObject.has("name"))
                contentValues.put("name", jsonObject.get("name").toString());
//            contentValues.put("connect", jsonObject.get("connect").toString());

            if(jsonObject.has("totalSeq"))
                contentValues.put("totalSeq", jsonObject.getInt("totalSeq"));

            if(jsonObject.has("startTime"))
                contentValues.put("startTime", jsonObject.get("startTime").toString().replace("T"," "));
            if(jsonObject.has("endTime"))
                contentValues.put("endTime", jsonObject.get("endTime").toString().replace("T"," "));

            if(jsonObject.has("result"))
                contentValues.put("result", jsonObject.get("result").toString());
            if(jsonObject.has("prograss"))
                contentValues.put("prograss", jsonObject.get("prograss").toString());
            else
                contentValues.put("prograss", "0");

            sQLiteDatabase.insert("tpinfo", null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
            globalVariable.writeexception(exception);
        }
    }
    public void deleteAlltp() {
        globalVariable.writelog("http deleteAlltp");
        SQLiteDatabase sQLiteDatabase = this.DH.getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM tpinfo");
        sQLiteDatabase.close();
    }
    private void addti(JSONObject jsonObject){
        try {
            globalVariable.writelog("http addti");
            SQLiteDatabase sQLiteDatabase = DH.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if(jsonObject.has("id"))
                contentValues.put("id", jsonObject.getInt("id"));

            if(jsonObject.has("seq"))
                contentValues.put("seq", jsonObject.get("seq").toString());
            if(jsonObject.has("name"))
                contentValues.put("name", jsonObject.get("name").toString());
            if(jsonObject.has("extName"))
                contentValues.put("extName", jsonObject.get("extName").toString());
            if(jsonObject.has("startTime"))
                contentValues.put("startTime", jsonObject.get("startTime").toString().replace("T"," "));
            if(jsonObject.has("endTime"))
                contentValues.put("endTime", jsonObject.get("endTime").toString().replace("T"," "));
            if(jsonObject.has("result"))
                contentValues.put("result", jsonObject.get("result").toString());
            sQLiteDatabase.insert("tiinfo", null, contentValues);
        } catch (Exception exception) {
            exception.printStackTrace();
            globalVariable.writeexception(exception);
        }
    }
    public void deleteAllti() {
        globalVariable.writelog("http deleteAllti");
        SQLiteDatabase sQLiteDatabase = this.DH.getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM tiinfo");
        sQLiteDatabase.close();
    }
    public void pcinfopage(){
        pcinfopagethread pcinfopagethread=new pcinfopagethread();
        pcinfopagethread.start();
    }
    private class pcinfopagethread extends Thread{
        @Override
        public void run() {
            super.run();
            deleteAllpc();
            globalVariable.setPccount(0);
            getpcinfopage();
            globalVariable.setHttpreturn(true);
        }
    }
    private void getpcinfopage(){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getpcinfopage");
//            Date current = new Date();
            URL url=new URL(hostname+"/api/Monitor/IPC");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            Log.d("kevin","getHttptoken "+globalVariable.getHttptoken());
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            Log.d("kevin",connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
//                    Log.d("kevin", line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));

                for(int i=0;i<jsonArraya.length();i++){
                    Log.d("data ", jsonArraya.get(i).toString());
                    JSONObject jsonObject2 = new JSONObject(jsonArraya.get(i).toString());
                    updatapc(jsonObject2);
                }
                jsonObject1=new JSONObject(jsonObject.getString("info"));
                Log.d("info ", jsonObject1.toString());
                globalVariable.setPccount(jsonObject1.getInt("ipcOnlineQty")+jsonObject1.getInt("ipcOfflineQty"));
                globalVariable.setIpcOnlineQty(jsonObject1.getInt("ipcOnlineQty"));
                globalVariable.setIpcOfflineQty(jsonObject1.getInt("ipcOfflineQty"));
                globalVariable.setTpPassQty(jsonObject1.getInt("tpPassQty"));
                globalVariable.setTpFailQty(jsonObject1.getInt("tpFailQty"));
                globalVariable.setTpFinishedQty(jsonObject1.getInt("tpFinishedQty"));
                globalVariable.setTpRunningQty(jsonObject1.getInt("tpRunningQty"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tpinfopage(String name){
        tpinfopagethread tpinfopagethread=new tpinfopagethread(name);
        tpinfopagethread.start();
    }
    private class tpinfopagethread extends Thread{
        String thname;

        public tpinfopagethread(String name){
            thname=name;

        }
        @Override
        public void run() {
            super.run();
            deleteAlltp();
            globalVariable.setTpcount(0);
            if(!thname.equals(""))
                gettpinfopage(thname);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettpinfopage(String name){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettpinfopage");
            URL url=new URL(hostname+"/api/Monitor/TestProgramData/Search?ipcComputerName="+name);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            Log.d("kevin",connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin", line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                globalVariable.setTpcount(Integer.valueOf(jsonObject.getString("total")));
                jsonArraya=new JSONArray(jsonObject.getString("data"));

                for(int i=0;i<jsonArraya.length();i++){
                    jsonObject = new JSONObject(jsonArraya.get(i).toString());
                    Log.d("kevin", ""+jsonObject);
                    addtp(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tiinfopage(String id){
        tiinfopagethread tiinfopagethread=new tiinfopagethread(id);
        tiinfopagethread.start();
    }
    private class tiinfopagethread extends Thread{
        String thid;
        public tiinfopagethread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deleteAllti();
            globalVariable.setTicount(0);
            if(!thid.equals(""))
                gettiinfopage(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettiinfopage(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettiinfopage");

            URL url=new URL(hostname+"/api/Monitor/TestItemData/Search?TestProgramId="+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            Log.d("kevin",connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin", line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                globalVariable.setTicount(Integer.valueOf(jsonObject.getString("total")));
                jsonArraya=new JSONArray(jsonObject.getString("data"));

                for(int i=0;i<jsonArraya.length();i++){
                    jsonObject = new JSONObject(jsonArraya.get(i).toString());
                    Log.d("kevin", ""+jsonObject);
                    addti(jsonObject);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void adeq(String tool,String config,String pcname,String ip,String systype,String mac){
        adeqthread adeqthread=new adeqthread(tool,config,pcname,ip,systype,mac);
        adeqthread.start();
    }
    private class adeqthread extends Thread{
        String thtool="";
        String thconfig="";
        String thpcname="";
        String thip="";
        String thsystype="";
        String thmac="";
        public adeqthread(String tool,String config,String pcname,String ip,String systype,String mac){
            thtool=tool;
            thconfig=config;
            thpcname=pcname;
            thip=ip;
            thsystype=systype;
            thmac=mac;
        }
        @Override
        public void run() {
            super.run();
//            deleteAllpc();
//            globalVariable.setPccount(0);
            postadeq(thtool,thconfig,thpcname,thip,thsystype,thmac);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postadeq(String tool,String config,String pcname,String ip,String systype,String mac){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postadeq");
            URL url=new URL(hostname+"/monitor/pcinfopage/pccard/adeq");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("sTool",tool);
            jsonParam.put("sConfig",config);
            jsonParam.put("sPcName",pcname);
            jsonParam.put("sIp",ip);
            jsonParam.put("sSysType",systype);
            jsonParam.put("sMac",mac);
            Log.d("kevin", ""+jsonParam);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin",connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin", line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tpinfos(String startTimeFrom,String startTimeTo,String result,String equipmentName,String page,String perPage){
        tpinfosthread tpinfosthread=new tpinfosthread(startTimeFrom,startTimeTo,result,equipmentName,page,perPage);
        tpinfosthread.start();
    }
    private class tpinfosthread extends Thread{
        String thFrom;
        String thTo;
        String thresult;
        String thequipmentName;
        String thpage;
        String thperPage;
        public tpinfosthread(String startTimeFrom,String startTimeTo,String result,String equipmentName,String page,String perPage){
            thFrom=startTimeFrom;
            thTo=startTimeTo;
            thresult=result;
            thequipmentName=equipmentName;
            thpage=page;
            thperPage=perPage;
        }
        @Override
        public void run() {
            super.run();
            gettpinfos(thFrom,thTo,thresult,thequipmentName,thpage,thperPage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettpinfos(String startTimeFrom,String startTimeTo,String result,String equipmentName,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettpinfos");
            String u=hostname+"/api/History/TestProgramData/Search?startTimeFrom="+ startTimeFrom+" 00:00:00"+"&startTimeTo="+startTimeTo+" 23:59:59"+"&result="+result+"&ipcName="+equipmentName;
            if(!page.equals(""))
                u+="&page="+page;
            if(!perPage.equals(""))
                u+="&perPage="+perPage;
            u=u.replaceAll(" ","%20");
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);

            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","gettpinfos:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
//            InputStream inputStream = connection.getInputStream();
//            if(inputStream != null) {
//
//            }
            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin","gettpinfos:"+ line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                    jsonObject = new JSONObject(s);
                    jsonArraya=new JSONArray(jsonObject.getString("data"));
                    globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                    globalVariable.setTotalcount(jsonObject.getInt("total"));
                    globalVariable.getTpinfoList().clear();
                    for(int i=0;i<jsonArraya.length();i++){
                        JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                        HashMap<String, String> tp = new HashMap<String, String>();
                        tp.put("ipcName", jsonObject1.getString("ipcName"));
                        tp.put("id", jsonObject1.getString("id"));
                        tp.put("startTime",jsonObject1.getString("startTime").replace("T"," "));
                        if(jsonObject1.getString("endTime").equals("null"))
                            tp.put("endTime","");
                        else
                            tp.put("endTime",jsonObject1.getString("endTime").replace("T"," "));
                        tp.put("name", jsonObject1.getString("name"));
                        tp.put("result",jsonObject1.getString("result"));
                        tp.put("totalSeq",jsonObject1.getString("totalSeq"));
                        tp.put("doneSeq",jsonObject1.getString("doneSeq"));
                        tp.put("updateTime",jsonObject1.getString("updateTime").replace("T"," "));
                        globalVariable.getTpinfoList().add(tp);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "gettpinfos:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tiinfos(String tprunid,String page,String perPage){
        tiinfosthread tiinfosthread=new tiinfosthread(tprunid,page,perPage);
        tiinfosthread.start();
    }
    private class tiinfosthread extends Thread{
        String thtprunid;
        String thpage;
        String thperPage;
        public tiinfosthread(String tprunid,String page,String perPage){
            thtprunid=tprunid;
            thpage=page;
            thperPage=perPage;
        }
        @Override
        public void run() {
            super.run();
            gettiinfos(thtprunid,thpage,thperPage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettiinfos(String tprunid,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettiinfos");

            String u=hostname+"/api/History/TestItemData/Search?TestProgramId="+tprunid;
            if(!page.equals(""))
                u+="&page="+page;
            if(!perPage.equals(""))
                u+="&perPage="+perPage;

            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","gettiinfos:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","gettiinfos:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));

                globalVariable.getTiinfoList().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> ti = new HashMap<String, String>();
                    ti.put("startTime",jsonObject1.getString("startTime").replace("T"," "));
                    ti.put("endTime",jsonObject1.getString("endTime").replace("T"," "));
                    ti.put("name", jsonObject1.getString("name"));
                    ti.put("result",jsonObject1.getString("result"));
                    ti.put("seq",jsonObject1.getString("seq"));
                    ti.put("updateTime",jsonObject1.getString("updateTime").replace("T"," "));
                    globalVariable.getTiinfoList().add(ti);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tpdailyactivation(String datefrom,String dateto){
        tpdailyactivationthread tpdailyactivationthread=new tpdailyactivationthread(datefrom,dateto);
        tpdailyactivationthread.start();
    }
    private class tpdailyactivationthread extends Thread{
        String thdatefrom;
        String thdateto;
        public tpdailyactivationthread(String datefrom,String dateto){
            thdatefrom=datefrom;
            thdateto=dateto;
        }
        @Override
        public void run() {
            super.run();
            gettpdailyactivation(thdatefrom,thdateto);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettpdailyactivation(String datefrom,String dateto){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettpdailyactivation");
            URL url=new URL(hostname+"/api/DataAnalysis/Activation/Daily?dateFrom="+datefrom+"&dateTo="+dateto);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","gettpdailyactivation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","gettpdailyactivation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonArraya = new JSONArray(s);

                globalVariable.getDailyList().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    jsonObject = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> activation = new HashMap<String, String>();
                    activation.put("ipcName",jsonObject.getString("ipcName"));
                    activation.put("date",jsonObject.getString("date"));
                    activation.put("activation",jsonObject.getString("activation"));
                    if( datefrom.split("-")[datefrom.split("-").length - 2].equals(jsonObject.getString("date").split("-")[jsonObject.getString("date").split("-").length - 2]))
                        globalVariable.getDailyList().add(activation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tpmonthlyactivation(String yearfrom,String yearto,String monthfrom,String monthto){
        tpmonthlyactivationthread tpmonthlyactivationthread=new tpmonthlyactivationthread(yearfrom,yearto,monthfrom,monthto);
        tpmonthlyactivationthread.start();
    }
    private class tpmonthlyactivationthread extends Thread{
        String thyearfrom;
        String thyearto;
        String thmonthfrom;
        String thmonthto;
        public tpmonthlyactivationthread(String yearfrom,String yearto,String monthfrom,String monthto){
            thyearfrom=yearfrom;
            thyearto=yearto;
            thmonthfrom=monthfrom;
            thmonthto=monthto;
        }
        @Override
        public void run() {
            super.run();
            gettpmonthlyactivation(thyearfrom,thyearto,thmonthfrom,thmonthto);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettpmonthlyactivation(String yearfrom,String yearto,String monthfrom,String monthto){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettpmonthlyactivation");
            URL url=new URL(hostname+"/api/DataAnalysis/Activation/Monthly?yearFrom="+yearfrom+"&yearTo="+yearto+"&monthFrom="+monthfrom+"&monthTo="+monthto);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","gettpmonthlyactivation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","gettpmonthlyactivation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonArraya = new JSONArray(s);


                globalVariable.getMonthlyList().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    jsonObject = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> activation = new HashMap<String, String>();
                    activation.put("ipcName",jsonObject.getString("ipcName"));
                    activation.put("date",jsonObject.getString("date"));
                    activation.put("activation",jsonObject.getString("activation"));

                    globalVariable.getMonthlyList().add(activation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void tpyearlyactivation(String yearfrom,String yearto){
        tpyearlyactivationthread tpyearlyactivationthread=new tpyearlyactivationthread(yearfrom,yearto);
        tpyearlyactivationthread.start();
    }
    private class tpyearlyactivationthread extends Thread{
        String thyearfrom;
        String thyearto;
        public tpyearlyactivationthread(String yearfrom,String yearto){
            thyearfrom=yearfrom;
            thyearto=yearto;
        }
        @Override
        public void run() {
            super.run();
            gettpyearlyactivation(thyearfrom,thyearto);
            globalVariable.setHttpreturn(true);
        }
    }
    private void gettpyearlyactivation(String yearfrom,String yearto){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http gettpyearlyactivation");
            URL url=new URL(hostname+"/api/DataAnalysis/Activation/Yearly?yearFrom="+yearfrom+"&yearTo="+yearto);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","gettpyearlyactivation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","gettpyearlyactivation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonArraya = new JSONArray(s);


                globalVariable.getYearlyList().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    jsonObject = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> activation = new HashMap<String, String>();
                    activation.put("ipcName",jsonObject.getString("ipcName"));
                    activation.put("date",jsonObject.getString("date"));
                    activation.put("activation",jsonObject.getString("activation"));

                    globalVariable.getYearlyList().add(activation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void login(String type,String account,String password){
        loginthread loginthread=new loginthread(type,account,password);
        loginthread.start();
    }
    private class loginthread extends Thread{
        String thtype;
        String thaccount;
        String thpassword;
        public loginthread(String type,String account,String password){
            thtype=type;
            thaccount=account;
            thpassword=password;
        }
        @Override
        public void run() {
            super.run();
            postlogin(thtype,thaccount,thpassword);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postlogin(String type,String account,String password){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postlogin");
            URL url=new URL(hostname+"/api/auth/Login");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("type",type);
            jsonParam.put("account",account);
            jsonParam.put("password",password);
            Log.d("kevin", ""+jsonParam);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postlogin:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postlogin:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                    jsonObject = new JSONObject(s);
                    globalVariable.setHttptoken(jsonObject.getString("token"));
//                jsonArraya = new JSONArray(s);
                }
            }else{
                globalVariable.setHttptoken("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
            globalVariable.setHttptoken("error");
        }
    }
    public void myinfo(){
        myinfothread myinfothread=new myinfothread();
        myinfothread.start();
    }
    private class myinfothread extends Thread{

        @Override
        public void run() {
            super.run();
            getmyinfo();
            globalVariable.setHttpreturn(true);
        }
    }
    private void getmyinfo(){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getmyinfo");
            URL url=new URL(hostname+"/api/user");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getmyinfo:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "getmyinfo:" + line);
                        s = line;
//
                    }
                    globalVariable.getSelfinction().clear();
                    jsonObject = new JSONObject(s);
//                    jsonArraya = new JSONArray(jsonObject.getString("data"));
                    jsonArraya= new JSONArray(new JSONObject(jsonObject.getString("data")).getString("functionNames"));
                    for(int i=0;i<jsonArraya.length();i++){
                        globalVariable.getSelfinction().add(jsonArraya.get(i).toString());
//                        Log.d("kevin", "getmyinfo:" + jsonArraya.get(i).toString());

                    }
//                    globalVariable.getSelfinction().add("remote");
//                    globalVariable.getSelfinction().add("schedule");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getusers(String page,String perPage){
        getusersthread getusersthread=new getusersthread(page,perPage);
        getusersthread.start();
    }
    private class getusersthread extends Thread{
        private String thpage;
        private String thperPage;
        public getusersthread(String page,String perPage){
            thpage=page;
            thperPage=perPage;
        }

        @Override
        public void run() {
            super.run();
            getgetusers(thpage,thperPage);
            globalVariable.setHttpreturn(true);
            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void getgetusers(String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetusers");
            String u=hostname+"/api/System/Identity/User";
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetusers:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(connection.getResponseCode()==200){
                if(inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
    //                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "getgetusers:" + line);
                        s       = line;
    //                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                    jsonObject = new JSONObject(s);

                    jsonArraya = new JSONArray(jsonObject.getString("data"));
                    globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                    globalVariable.setTotalcount(jsonObject.getInt("total"));
                    globalVariable.getUserList().clear();
                    for (int i = 0; i < jsonArraya.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                        HashMap<String, String> user = new HashMap<String, String>();
                        user.put("userName", jsonObject1.getString("userName"));
                        if(jsonObject1.getString("adAccount").equals("null"))
                            user.put("adAccount", "");
                        else
                            user.put("adAccount", jsonObject1.getString("adAccount"));
                        if(jsonObject1.getString("email").equals("null"))
                            user.put("email", "");
                        else
                            user.put("email", jsonObject1.getString("email"));

                        user.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                        user.put("id", jsonObject1.getString("id"));

                        globalVariable.getUserList().add(user);
                    }
                }
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getuser(String userid){
        getuserthread getuserthread=new getuserthread(userid);
        getuserthread.start();
    }
    private class getuserthread extends Thread{
        String thuserid;
        public getuserthread(String userid){
            thuserid=userid;
        }
        @Override
        public void run() {
            super.run();
            getgetuser(thuserid);
            globalVariable.setHttpreturn(true);
            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void getgetuser(String userid){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetuser");
            URL url=new URL(hostname+"/api/System/Identity/User/"+userid);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","getgetuser:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetuser:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                globalVariable.getSelrole().clear();
                jsonObject = new JSONObject(s);
                jsonObject1 = new JSONObject(jsonObject.getString("data"));
                globalVariable.setSelusername(jsonObject1.getString("userName"));
                if(jsonObject1.getString("adAccount").equals("null"))
                    globalVariable.setSeladaccount("");
                else
                    globalVariable.setSeladaccount(jsonObject1.getString("adAccount"));
                if(jsonObject1.getString("email").equals("null"))
                    globalVariable.setSelemail("");
                else
                    globalVariable.setSelemail(jsonObject1.getString("email"));

//
                String[] temp=jsonObject1.getString("roleNames").replaceAll("\\[|\\]|\"","").split(",");
//                globalVariable.setTotaluserpage(jsonObject.getString("lastPage"));


                for(int i=0;i<temp.length;i++){

                    globalVariable.getSelrole().add(temp[i]);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createuser(String username,String email,String adaccount,String password,List rolenames){
        createuserthread createuserthread=new createuserthread(username,email,adaccount,password,rolenames);
        createuserthread.start();
    }
    private class createuserthread extends Thread{
        String thusername;
        String themail;
        String thadaccount;
        String thpassword;
        List throlenames;
        public createuserthread(String username,String email,String adaccount,String password,List rolenames){
            thusername=username;
            themail=email;
            thadaccount=adaccount;
            thpassword=password;
            throlenames=rolenames;
        }
        @Override
        public void run() {
            super.run();
            postcreateuser(thusername,themail,thadaccount,thpassword,throlenames);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreateuser(String username,String email,String adaccount,String password,List rolenames){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreateuser");
            URL url=new URL(hostname+"/api/System/Identity/User");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="UserName="+username+"&Email="+email+"&AdAccount="+adaccount+"&Password="+password;
            for(int a=0;a<rolenames.size();a++){
                par+="&RoleNames="+rolenames.get(a);
            }
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreateuser:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateuser:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateuser:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatauser(String userid,String username,String email,String adaccount,String password,List rolenames){
        updatauserthread updatauserthread=new updatauserthread(userid,username,email,adaccount,password,rolenames);
        updatauserthread.start();
    }
    private class updatauserthread extends Thread{
        String thuserid;
        String thusername;
        String themail;
        String thadaccount;
        String thpassword;
        List throlenames;
        public updatauserthread(String userid,String username,String email,String adaccount,String password,List rolenames){
            thuserid=userid;
            thusername=username;
            themail=email;
            thadaccount=adaccount;
            thpassword=password;
            throlenames=rolenames;
        }
        @Override
        public void run() {
            super.run();
            putupdatauser(thuserid,thusername,themail,thadaccount,thpassword,throlenames);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatauser(String userid,String username,String email,String adaccount,String password,List rolenames){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatauser");
            URL url=new URL(hostname+"/api/System/Identity/User/"+userid);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if(password.equals("")) {
                String par = "UserName=" + username + "&Email=" + email + "&AdAccount=" + adaccount;
                for (int a = 0; a < rolenames.size(); a++) {
                    par += "&RoleNames=" + rolenames.get(a);
                }
                Log.d("kevin", ""+par);
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = par.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }else{
                String par = "UserName=" + username + "&Email=" + email + "&AdAccount=" + adaccount + "&Password=" + password;
                for (int a = 0; a < rolenames.size(); a++) {
                    par += "&RoleNames=" + rolenames.get(a);
                }
                Log.d("kevin", ""+par);
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = par.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            Log.d("kevin","putupdatauser:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatauser:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatauser:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deleteuser(String userid){
        deleteuserthread deleteuserthread=new deleteuserthread(userid);
        deleteuserthread.start();
    }
    private class deleteuserthread extends Thread{
        String thuserid;
        public deleteuserthread(String userid){
            thuserid=userid;
        }
        @Override
        public void run() {
            super.run();
            deldeleteuser(thuserid);
            globalVariable.setHttpreturn(true);
            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeleteuser(String userid){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeleteuser");
            URL url=new URL(hostname+"/api/System/Identity/User/"+userid);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","getgetuser:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetuser:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getroles(String page,String perPage){
        getrolesthread getrolesthread=new getrolesthread(page,perPage);
        getrolesthread.start();
    }
    private class getrolesthread extends Thread{
        String thpage;
        String thperpage;
        public getrolesthread(String page,String perPage){
            thpage=page;
            thperpage=perPage;
        }

        @Override
        public void run() {
            super.run();
            getgetroles(thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetroles(String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetroles");
            String u=hostname+"/api/System/Identity/Role";
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }

            URL url=new URL(u);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetroles:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetroles:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);

                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getRoleList().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> role = new HashMap<String, String>();
                    role.put("name", jsonObject1.getString("name"));
                    if(jsonObject1.getString("description").equals("null"))
                        role.put("description", "");
                    else
                        role.put("description", jsonObject1.getString("description"));

                    role.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    role.put("id", jsonObject1.getString("id"));

                    globalVariable.getRoleList().add(role);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getrole(String roleid){
        getrolethread getrolethread=new getrolethread(roleid);
        getrolethread.start();
    }
    private class getrolethread extends Thread{
        String throleid;
        public getrolethread(String roleid){
            throleid=roleid;
        }
        @Override
        public void run() {
            super.run();
            getgetrole(throleid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void getgetrole(String roleid){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetrole");
            URL url=new URL(hostname+"/api/System/Identity/Role/"+roleid);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","getgetrole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetuser:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                globalVariable.getSelfinction().clear();
                globalVariable.getSelscopelabel().clear();
                globalVariable.getSelfinctionlabel().clear();
                globalVariable.getSelscope().clear();
                jsonObject = new JSONObject(s);
                jsonObject1 = new JSONObject(jsonObject.getString("data"));
                globalVariable.setSelrolename(jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    globalVariable.setSeldescription("");
                else
                    globalVariable.setSeldescription(jsonObject1.getString("description"));
                setscope(jsonObject1,globalVariable.getSelscope(),globalVariable.getSelscopelabel(),"scopes");
                setscope(jsonObject1,globalVariable.getSelfinction(),globalVariable.getSelfinctionlabel(),"functions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createrole(String rolename,String description,List scope,List function){
        createrolethread createrolethread=new createrolethread(rolename,description,scope,function);
        createrolethread.start();
    }
    private class createrolethread extends Thread{
        String throlename;
        String thdescription;
        List thscope;
        List thfunction;
        public createrolethread(String rolename,String description,List scope,List function){
            throlename=rolename;
            thdescription=description;
            thscope=scope;
            thfunction=function;

        }
        @Override
        public void run() {
            super.run();
            postcreaterole(throlename,thdescription,thscope,thfunction);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreaterole(String rolename,String description,List scope,List function){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreaterole");
            URL url=new URL(hostname+"/api/System/Identity/Role");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="Name="+rolename+"&Description="+description;
            for(int a=0;a<scope.size();a++){
                par+="&Scopes="+scope.get(a);
            }
            for(int a=0;a<function.size();a++){
                par+="&Functions="+function.get(a);
            }
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreaterole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreaterole:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreaterole:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatarole(String roleid,String name,String description,List scope,List function){
        updatarolethread updatarolethread=new updatarolethread(roleid,name,description,scope,function);
        updatarolethread.start();
    }
    private class updatarolethread extends Thread{
        String throleid;
        String thname;
        String thedescription;
        List thscope;
        List thfunction;
        public updatarolethread(String roleid,String name,String description,List scope,List function){
            throleid=roleid;
            thname=name;
            thedescription=description;
            thscope=scope;
            thfunction=function;
        }
        @Override
        public void run() {
            super.run();
            putupdatarole(throleid,thname,thedescription,thscope,thfunction);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatarole(String roleid,String name,String description,List scope,List function){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatarole");
            URL url=new URL(hostname+"/api/System/Identity/Role/"+roleid);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="Name="+name+"&Description="+description;
            for(int a=0;a<scope.size();a++){
                par+="&Scopes="+scope.get(a);
            }
            for(int a=0;a<function.size();a++){
                par+="&Functions="+function.get(a);
            }
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","putupdatarole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatarole:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatarole:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deleterole(String roleid){
        deleterolethread deleterolethread=new deleterolethread(roleid);
        deleterolethread.start();
    }
    private class deleterolethread extends Thread{
        String throleid;
        public deleterolethread(String roleid){
            throleid=roleid;
        }
        @Override
        public void run() {
            super.run();
            deldeleterole(throleid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeleterole(String roleid){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeleterole");
            URL url=new URL(hostname+"/api/System/Identity/Role/"+roleid);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeleterole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeleterole:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getcountries(String page,String perPage){
        getcountiesthread getcountiesthread=new getcountiesthread(page,perPage);
        getcountiesthread.start();
    }
    private class getcountiesthread extends Thread{
        private String thpage;
        private String thperpage;
        public getcountiesthread(String page,String perPage){
            thpage=page;
            thperpage=perPage;
        }
        @Override
        public void run() {
            super.run();
            getgetcountries(thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetcountries(String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetcountries");
            String u=hostname+"/api/Manage/Country";
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetcountry:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetcountry:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getCountrylist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> country = new HashMap<String, String>();
                    country.put("id", jsonObject1.getString("id"));
                    country.put("name", jsonObject1.getString("name"));
                    if(jsonObject1.getString("description").equals("null"))
                        country.put("description", "");
                    else
                        country.put("description", jsonObject1.getString("description"));

                    country.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    country.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getCountrylist().add(country);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getcountry(String id){
        getcountrythread getcountrythread=new getcountrythread(id);
        getcountrythread.start();
    }
    private class getcountrythread extends Thread{
        String thid;
        public getcountrythread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetcountry(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetcountry(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetcountry");
            URL url=new URL(hostname+"/api/Manage/Country/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetcountry:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetcountry:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> country = new HashMap<String, String>();

                country.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    country.put("description", "");
                else
                    country.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(country);



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createcountry(String name,String description){
        createcountrythread createcountrythread=new createcountrythread(name,description);
        createcountrythread.start();
    }
    private class createcountrythread extends Thread{
        String thname;
        String thdescription;

        public createcountrythread(String name,String description){
            thname=name;
            thdescription=description;

        }
        @Override
        public void run() {
            super.run();
            postcreatecountry(thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreatecountry(String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreatecountry");
            URL url=new URL(hostname+"/api/Manage/Country");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreatecountry:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatecountry:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatecountry:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatacountry(String id,String name,String description){
        updatacountrythread updatacountrythread=new updatacountrythread(id,name,description);
        updatacountrythread.start();
    }
    private class updatacountrythread extends Thread{
        String thid;
        String thname;
        String thedescription;

        public updatacountrythread(String id,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;

        }
        @Override
        public void run() {
            super.run();
            putupdatacountry(thid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatacountry(String id,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatacountry");
            URL url=new URL(hostname+"/api/Manage/Country/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","putupdatacountry:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatacountry:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdatacountry:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deletecountry(String id){
        deletecountrythread deletecountrythread=new deletecountrythread(id);
        deletecountrythread.start();
    }
    private class deletecountrythread extends Thread{
        String thid;
        public deletecountrythread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeletecountry(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeletecountry(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeletecountry");
            URL url=new URL(hostname+"/api/Manage/Country/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeletecountry:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletecountry:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getcites(String id,String name,String page,String perPage){
        getcitesthread getcitesthread=new getcitesthread(id,name,page,perPage);
        getcitesthread.start();
    }
    private class getcitesthread extends Thread{
        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;
        public getcitesthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }
        @Override
        public void run() {
            super.run();
            getgetcites(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetcites(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetcites");
            String u=hostname+"/api/Manage/City/Search?countryId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetcites:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetcites:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getCitylist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> city = new HashMap<String, String>();
                    city.put("id", jsonObject1.getString("id"));
                    city.put("name", jsonObject1.getString("name"));
                    city.put("countryId", jsonObject1.getString("countryId"));
                    city.put("countryName", jsonObject1.getString("countryName"));
                    if(jsonObject1.getString("description").equals("null"))
                        city.put("description", "");
                    else
                        city.put("description", jsonObject1.getString("description"));

                    city.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    city.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getCitylist().add(city);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getcity(String id){
        getcitythread getcitythread=new getcitythread(id);
        getcitythread.start();
    }
    private class getcitythread extends Thread{
        String thid;
        public getcitythread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetcity(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetcity(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetcity");
            URL url=new URL(hostname+"/api/Manage/City/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetcity:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetcity:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> city = new HashMap<String, String>();

                city.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    city.put("description", "");
                else
                    city.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(city);



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createcity(String countryid,String name,String description){
        createcitythread createcitythread=new createcitythread(countryid,name,description);
        createcitythread.start();
    }
    private class createcitythread extends Thread{
        String thname;
        String thdescription;
        String thcountryid;

        public createcitythread(String countryid,String name,String description){
            thname=name;
            thdescription=description;
            thcountryid=countryid;

        }
        @Override
        public void run() {
            super.run();
            postcreatecity(thcountryid,thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreatecity(String countryid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreatecity");
            URL url=new URL(hostname+"/api/Manage/City");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="CountryId="+countryid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreatecity:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatecity:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatecity:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatacity(String id,String countryid,String name,String description){
        updatacitythread updatacitythread=new updatacitythread(id,countryid,name,description);
        updatacitythread.start();
    }
    private class updatacitythread extends Thread{
        String thid;
        String thname;
        String thedescription;
        String thcountryid;
        public updatacitythread(String id,String countryid,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;
            thcountryid=countryid;

        }
        @Override
        public void run() {
            super.run();
            putupdatacity(thid,thcountryid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatacity(String id,String countryid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatacity");
            URL url=new URL(hostname+"/api/Manage/City/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="CountryId="+countryid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","updatacity:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatacity:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatacity:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deletecity(String id){
        deletecitythread deletecitythread=new deletecitythread(id);
        deletecitythread.start();
    }
    private class deletecitythread extends Thread{
        String thid;
        public deletecitythread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeletecity(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeletecity(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeletecity");
            URL url=new URL(hostname+"/api/Manage/City/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeletecity:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletecity:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getsites(String id,String name,String page,String perPage){
        getsitesthread getsitesthread=new getsitesthread(id,name,page,perPage);
        getsitesthread.start();
    }
    private class getsitesthread extends Thread{

        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;
        public getsitesthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }
        @Override
        public void run() {
            super.run();
            getgetsites(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetsites(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetsites");
            String u=hostname+"/api/Manage/Site/Search?CityId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetsites:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetsites:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getSitelist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> site = new HashMap<String, String>();
                    site.put("id", jsonObject1.getString("id"));
                    site.put("name", jsonObject1.getString("name"));
                    site.put("countryId", jsonObject1.getString("countryId"));
                    site.put("countryName", jsonObject1.getString("countryName"));
                    site.put("cityId", jsonObject1.getString("cityId"));
                    site.put("cityName", jsonObject1.getString("cityName"));
                    if(jsonObject1.getString("description").equals("null"))
                        site.put("description", "");
                    else
                        site.put("description", jsonObject1.getString("description"));

                    site.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    site.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
//                    Log.d("kevin",site.toString());
                    globalVariable.getSitelist().add(site);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getsite(String id){
        getsitethread getsitethread=new getsitethread(id);
        getsitethread.start();
    }
    private class getsitethread extends Thread{
        String thid;
        public getsitethread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetsite(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetsite(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetsite");
            URL url=new URL(hostname+"/api/Manage/Site/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetsite:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetsite:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> site = new HashMap<String, String>();

                site.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    site.put("description", "");
                else
                    site.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(site);



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createsite(String cityid,String name,String description){
        createsitethread createsitethread=new createsitethread(cityid,name,description);
        createsitethread.start();
    }
    private class createsitethread extends Thread{
        String thname;
        String thdescription;
        String thcityid;

        public createsitethread(String cityid,String name,String description){
            thname=name;
            thdescription=description;
            thcityid=cityid;

        }
        @Override
        public void run() {
            super.run();
            postcreatesite(thcityid,thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreatesite(String cityid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreatesite");
            URL url=new URL(hostname+"/api/Manage/Site");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="CityId="+cityid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreatesite:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatesite:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatesite:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatasite(String id,String cityid,String name,String description){
        updatasitethread updatasitethread=new updatasitethread(id,cityid,name,description);
        updatasitethread.start();
    }
    private class updatasitethread extends Thread{
        String thid;
        String thname;
        String thedescription;
        String thcityid;
        public updatasitethread(String id,String cityid,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;
            thcityid=cityid;

        }
        @Override
        public void run() {
            super.run();
            putupdatasite(thid,thcityid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatasite(String id,String cityid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatasite");
            URL url=new URL(hostname+"/api/Manage/Site/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="CityId="+cityid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","updatasite:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatasite:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatasite:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deletesite(String id){
        deletesitethread deletesitethread=new deletesitethread(id);
        deletesitethread.start();
    }
    private class deletesitethread extends Thread{
        String thid;
        public deletesitethread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeletesite(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeletesite(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeletesite");
            URL url=new URL(hostname+"/api/Manage/Site/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeletesite:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletesite:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getzones(String id,String name,String page,String perPage){
        getzonesthread getzonesthread=new getzonesthread(id,name,page,perPage);
        getzonesthread.start();
    }
    private class getzonesthread extends Thread{
        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;
        public getzonesthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }

        @Override
        public void run() {
            super.run();
            getgetzones(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetzones(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetzones");
            String u=hostname+"/api/Manage/Zone/Search?SiteId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetzones:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetzones:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getZonelist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> zone = new HashMap<String, String>();
                    zone.put("id", jsonObject1.getString("id"));
                    zone.put("name", jsonObject1.getString("name"));
                    zone.put("countryId", jsonObject1.getString("countryId"));
                    zone.put("countryName", jsonObject1.getString("countryName"));
                    zone.put("cityId", jsonObject1.getString("cityId"));
                    zone.put("cityName", jsonObject1.getString("cityName"));
                    zone.put("siteId", jsonObject1.getString("siteId"));
                    zone.put("siteName", jsonObject1.getString("siteName"));
                    if(jsonObject1.getString("description").equals("null"))
                        zone.put("description", "");
                    else
                        zone.put("description", jsonObject1.getString("description"));

                    zone.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    zone.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getZonelist().add(zone);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getzone(String id){
        getzonethread getzonethread=new getzonethread(id);
        getzonethread.start();
    }
    private class getzonethread extends Thread{
        String thid;
        public getzonethread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetzone(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetzone(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetzone");
            URL url=new URL(hostname+"/api/Manage/Zone/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetzone:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetzone:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> zone = new HashMap<String, String>();

                zone.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    zone.put("description", "");
                else
                    zone.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(zone);



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }

    public void createzone(String siteid,String name,String description){
        createzonethread createzonethread=new createzonethread(siteid,name,description);
        createzonethread.start();
    }
    private class createzonethread extends Thread{
        String thname;
        String thdescription;
        String thsiteid;

        public createzonethread(String siteid,String name,String description){
            thname=name;
            thdescription=description;
            thsiteid=siteid;

        }
        @Override
        public void run() {
            super.run();
            postcreatezone(thsiteid,thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreatezone(String siteid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreatezone");
            URL url=new URL(hostname+"/api/Manage/Zone");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="SiteId="+siteid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreatezone:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatezone:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatezone:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatazone(String id,String siteid,String name,String description){
        updatazonethread updatazonethread=new updatazonethread(id,siteid,name,description);
        updatazonethread.start();
    }
    private class updatazonethread extends Thread{
        String thid;
        String thname;
        String thedescription;
        String thsiteid;
        public updatazonethread(String id,String siteid,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;
            thsiteid=siteid;

        }
        @Override
        public void run() {
            super.run();
            putupdatazone(thid,thsiteid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatazone(String id,String siteid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatazone");
            URL url=new URL(hostname+"/api/Manage/Zone/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="SiteId="+siteid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","updatazone:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatazone:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatazone:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deletezone(String id){
        deletezonethread deletezonethread=new deletezonethread(id);
        deletezonethread.start();
    }
    private class deletezonethread extends Thread{
        String thid;
        public deletezonethread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeletezone(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeletezone(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeletezone");
            URL url=new URL(hostname+"/api/Manage/Zone/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeletezone:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletezone:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getlines(String id,String name,String page,String perPage){
        getlinesthread getlinesthread=new getlinesthread(id,name,page,perPage);
        getlinesthread.start();
    }
    private class getlinesthread extends Thread{
        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;

        public getlinesthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }

        @Override
        public void run() {
            super.run();
            getgetlines(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetlines(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetlines");

            String u=hostname+"/api/Manage/Line/Search?ZoneId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetlines:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String readline = "";
                String s = "";

                while ((readline = in.readLine()) != null) {
                    Log.d("kevin","getgetlines:"+ readline);
                    s = readline;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getLinelist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> line = new HashMap<String, String>();
                    line.put("id", jsonObject1.getString("id"));
                    line.put("name", jsonObject1.getString("name"));
                    line.put("countryId", jsonObject1.getString("countryId"));
                    line.put("countryName", jsonObject1.getString("countryName"));
                    line.put("cityId", jsonObject1.getString("cityId"));
                    line.put("cityName", jsonObject1.getString("cityName"));
                    line.put("siteId", jsonObject1.getString("siteId"));
                    line.put("siteName", jsonObject1.getString("siteName"));
                    line.put("zoneId", jsonObject1.getString("zoneId"));
                    line.put("zoneName", jsonObject1.getString("zoneName"));
                    if(jsonObject1.getString("description").equals("null"))
                        line.put("description", "");
                    else
                        line.put("description", jsonObject1.getString("description"));

                    line.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    line.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getLinelist().add(line);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getline(String id){
        getlinethread getlinethread=new getlinethread(id);
        getlinethread.start();
    }
    private class getlinethread extends Thread{
        String thid;
        public getlinethread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetline(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetline(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetline");
            URL url=new URL(hostname+"/api/Manage/Line/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetline:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetline:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> linemap = new HashMap<String, String>();

                linemap.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    linemap.put("description", "");
                else
                    linemap.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(linemap);



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createline(String zoneid,String name,String description){
        createlinethread createlinethread=new createlinethread(zoneid,name,description);
        createlinethread.start();
    }
    private class createlinethread extends Thread{
        String thname;
        String thdescription;
        String thzoneid;

        public createlinethread(String zoneid,String name,String description){
            thname=name;
            thdescription=description;
            thzoneid=zoneid;

        }
        @Override
        public void run() {
            super.run();
            postcreateline(thzoneid,thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreateline(String zoneid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreateline");
            URL url=new URL(hostname+"/api/Manage/Line");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="ZoneId="+zoneid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreateline:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateline:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateline:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updataline(String id,String zoneid,String name,String description){
        updatalinethread updatalinethread=new updatalinethread(id,zoneid,name,description);
        updatalinethread.start();
    }
    private class updatalinethread extends Thread{
        String thid;
        String thname;
        String thedescription;
        String thzoneid;
        public updatalinethread(String id,String zoneid,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;
            thzoneid=zoneid;

        }
        @Override
        public void run() {
            super.run();
            putupdataline(thid,thzoneid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdataline(String id,String zoneid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdataline");
            URL url=new URL(hostname+"/api/Manage/Line/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="ZoneId="+zoneid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","updataline:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updataline:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updataline:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deleteline(String id){
        deletelinethread deletelinethread=new deletelinethread(id);
        deletelinethread.start();
    }
    private class deletelinethread extends Thread{
        String thid;
        public deletelinethread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeleteline(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeleteline(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeleteline");
            URL url=new URL(hostname+"/api/Manage/Line/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeleteline:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeleteline:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getstations(String id,String name,String page,String perPage){
        getstationsthread getstationsthread=new getstationsthread(id,name,page,perPage);
        getstationsthread.start();
    }
    private class getstationsthread extends Thread{

        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;
        public getstationsthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }

        @Override
        public void run() {
            super.run();
            getgetstations(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetstations(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetstations");
            String u=hostname+"/api/Manage/Station/Search?LineId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="page="+page;
                else if(!perPage.equals(""))
                    u+="perPage="+perPage;
            }
            URL url=new URL(u);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetstations:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetstations:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getStationlist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> station = new HashMap<String, String>();
                    station.put("id", jsonObject1.getString("id"));
                    station.put("name", jsonObject1.getString("name"));
                    station.put("countryId", jsonObject1.getString("countryId"));
                    station.put("countryName", jsonObject1.getString("countryName"));
                    station.put("cityId", jsonObject1.getString("cityId"));
                    station.put("cityName", jsonObject1.getString("cityName"));
                    station.put("siteId", jsonObject1.getString("siteId"));
                    station.put("siteName", jsonObject1.getString("siteName"));
                    station.put("zoneId", jsonObject1.getString("zoneId"));
                    station.put("zoneName", jsonObject1.getString("zoneName"));
                    station.put("lineId", jsonObject1.getString("lineId"));
                    station.put("lineName", jsonObject1.getString("lineName"));
                    if(jsonObject1.getString("description").equals("null"))
                        station.put("description", "");
                    else
                        station.put("description", jsonObject1.getString("description"));

                    station.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    station.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getStationlist().add(station);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getstation(String id){
        getstationthread getstationthread=new getstationthread(id);
        getstationthread.start();
    }
    private class getstationthread extends Thread{
        String thid;
        public getstationthread(String id){
            thid=id;
        }

        @Override
        public void run() {
            super.run();
            getgetstation(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetstation(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetstation");
            URL url=new URL(hostname+"/api/Manage/station/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetstation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetstation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }

                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> station = new HashMap<String, String>();

                station.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("description").equals("null"))
                    station.put("description", "");
                else
                    station.put("description", jsonObject1.getString("description"));
                globalVariable.setSelmanage(station);
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createstation(String lineid,String name,String description){
        createstationthread createstationthread=new createstationthread(lineid,name,description);
        createstationthread.start();
    }
    private class createstationthread extends Thread{
        String thname;
        String thdescription;
        String thlineid;

        public createstationthread(String lineid,String name,String description){
            thname=name;
            thdescription=description;
            thlineid=lineid;

        }
        @Override
        public void run() {
            super.run();
            postcreatestation(thlineid,thname,thdescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreatestation(String lineid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreatestation");
            URL url=new URL(hostname+"/api/Manage/Station");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="LineId="+lineid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreatestation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatestation:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreatestation:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updatastation(String id,String lineid,String name,String description){
        updatastationthread updatastationthread=new updatastationthread(id,lineid,name,description);
        updatastationthread.start();
    }
    private class updatastationthread extends Thread{
        String thid;
        String thname;
        String thedescription;
        String thlineid;
        public updatastationthread(String id,String lineid,String name,String description){
            thname=name;
            thedescription=description;
            thid=id;
            thlineid=lineid;

        }
        @Override
        public void run() {
            super.run();
            putupdatastation(thid,thlineid,thname,thedescription);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdatastation(String id,String lineid,String name,String description){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdatastation");
            URL url=new URL(hostname+"/api/Manage/Station/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="LineId="+lineid+"&Name="+name+"&Description="+description;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","updatastation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatastation:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "updatastation:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deletestation(String id){
        deletestationthread deletestationthread=new deletestationthread(id);
        deletestationthread.start();
    }
    private class deletestationthread extends Thread{
        String thid;
        public deletestationthread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeletestation(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeletestation(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeletestation");
            URL url=new URL(hostname+"/api/Manage/Station/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeletestation:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletestation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
//    public void getrole(String roleid){
//        getrolethread getrolethread=new getrolethread(roleid);
//        getrolethread.start();
//    }
//    private class getrolethread extends Thread{
//        String throleid;
//        public getrolethread(String roleid){
//            throleid=roleid;
//        }
//        @Override
//        public void run() {
//            super.run();
//            getgetrole(throleid);
//            globalVariable.setHttpreturn(true);
////            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
//        }
//    }
//    private void getgetrole(String userid){
//        HttpURLConnection connection = null;
//        try {
//
//            URL url=new URL(hostname+"/api/System/Identity/Role/"+userid);
//
//            Log.d("kevin","URL "+url);
//            connection = (HttpURLConnection) url.openConnection();
////            connection.setConnectTimeout(5000);
//            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
////            connection.setRequestProperty("userId",userid);
//            connection.setReadTimeout(5000);
//            connection.setRequestMethod("GET");
//            connection.setDoInput(true);
//            connection.connect();
//
//
//            Log.d("kevin","getgetrole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
//            InputStream inputStream = connection.getInputStream();
//            if(inputStream != null) {
//                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader in = new BufferedReader(reader);
//                JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                JSONObject jsonObject;
//                JSONObject jsonObject1;
//                String line = "";
//                String s = "";
//
//                while ((line = in.readLine()) != null) {
//                    Log.d("kevin","getgetuser:"+ line);
//                    s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                }
//
//                globalVariable.getSelfinction().clear();
//                globalVariable.getSelscope().clear();
//                jsonObject = new JSONObject(s);
//                jsonObject1 = new JSONObject(jsonObject.getString("data"));
//                globalVariable.setSelrolename(jsonObject1.getString("name"));
//                if(jsonObject1.getString("description").equals("null"))
//                    globalVariable.setSeldescription("");
//                else
//                    globalVariable.setSeldescription(jsonObject1.getString("description"));
//                setscope(jsonObject1,globalVariable.getSelscope(),globalVariable.getSelscopelabel(),"scopes");
//                setscope(jsonObject1,globalVariable.getSelfinction(),globalVariable.getSelfinctionlabel(),"functions");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();

//        }
//    }
//    public void createrole(String rolename,String description,List scope,List function){
//        createrolethread createrolethread=new createrolethread(rolename,description,scope,function);
//        createrolethread.start();
//    }
//    private class createrolethread extends Thread{
//        String throlename;
//        String thdescription;
//        List thscope;
//        List thfunction;
//        public createrolethread(String rolename,String description,List scope,List function){
//            throlename=rolename;
//            thdescription=description;
//            thscope=scope;
//            thfunction=function;
//
//        }
//        @Override
//        public void run() {
//            super.run();
//            postcreaterole(throlename,thdescription,thscope,thfunction);
//            globalVariable.setHttpreturn(true);
//        }
//    }
//    private void postcreaterole(String rolename,String description,List scope,List function){
//        HttpURLConnection connection = null;
//        try {
//            URL url=new URL(hostname+"/api/System/Identity/Role");
//            Log.d("kevin","URL "+url);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Connection", "keep-alive");
//            connection.setRequestProperty("Accept", "*/*");
//            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            String par="Name="+rolename+"&Description="+description;
//            for(int a=0;a<scope.size();a++){
//                par+="&Scopes="+scope.get(a);
//            }
//            for(int a=0;a<function.size();a++){
//                par+="&Functions="+function.get(a);
//            }
//            Log.d("kevin", ""+par);
//            try(OutputStream os = connection.getOutputStream()) {
//                byte[] input = par.toString().getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//            Log.d("kevin","postcreaterole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
//
//            if(connection.getResponseCode()==200) {
//                InputStream inputStream = connection.getInputStream();
//                if (inputStream != null) {
//                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                    BufferedReader in = new BufferedReader(reader);
//                    JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                    JSONObject jsonObject;
//                    String line = "";
//                    String s = "";
//
//                    while ((line = in.readLine()) != null) {
//                        Log.d("kevin", "postcreaterole:" + line);
//                        s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                    }
//                    jsonObject = new JSONObject(s);
//
//                }
//            }else{
//                InputStream inputStream = connection.getErrorStream();
//                if (inputStream != null) {
//                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                    BufferedReader in = new BufferedReader(reader);
//                    JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                    JSONObject jsonObject;
//                    String line = "";
//                    String s = "";
//
//                    while ((line = in.readLine()) != null) {
//                        Log.d("kevin", "postcreaterole:" + line);
//                        s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//    public void updatarole(String roleid,String name,String description,List scope,List function){
//        updatarolethread updatarolethread=new updatarolethread(roleid,name,description,scope,function);
//        updatarolethread.start();
//    }
//    private class updatarolethread extends Thread{
//        String throleid;
//        String thname;
//        String thedescription;
//        List thscope;
//        List thfunction;
//        public updatarolethread(String roleid,String name,String description,List scope,List function){
//            throleid=roleid;
//            thname=name;
//            thedescription=description;
//            thscope=scope;
//            thfunction=function;
//        }
//        @Override
//        public void run() {
//            super.run();
//            putupdatarole(throleid,thname,thedescription,thscope,thfunction);
//            globalVariable.setHttpreturn(true);
//        }
//    }
//    private void putupdatarole(String roleid,String name,String description,List scope,List function){
//        HttpURLConnection connection = null;
//        try {
//            URL url=new URL(hostname+"/api/System/Identity/Role/"+roleid);
//            Log.d("kevin","URL "+url);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setRequestMethod("PUT");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            String par="Name="+name+"&Description="+description;
//            for(int a=0;a<scope.size();a++){
//                par+="&Scopes="+scope.get(a);
//            }
//            for(int a=0;a<function.size();a++){
//                par+="&Functions="+function.get(a);
//            }
//            Log.d("kevin", ""+par);
//            try(OutputStream os = connection.getOutputStream()) {
//                byte[] input = par.toString().getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            Log.d("kevin","putupdatarole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
//
//            if(connection.getResponseCode()==200) {
//                InputStream inputStream = connection.getInputStream();
//                if (inputStream != null) {
//                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                    BufferedReader in = new BufferedReader(reader);
//                    JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                    JSONObject jsonObject;
//                    String line = "";
//                    String s = "";
//
//                    while ((line = in.readLine()) != null) {
//                        Log.d("kevin", "putupdatarole:" + line);
//                        s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                    }
//
//                }
//            }else{
//                InputStream inputStream = connection.getErrorStream();
//                if (inputStream != null) {
//                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                    BufferedReader in = new BufferedReader(reader);
//                    JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                    JSONObject jsonObject;
//                    String line = "";
//                    String s = "";
//
//                    while ((line = in.readLine()) != null) {
//                        Log.d("kevin", "putupdatarole:" + line);
//                        s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//    public void deleterole(String roleid){
//        deleterolethread deleterolethread=new deleterolethread(roleid);
//        deleterolethread.start();
//    }
//    private class deleterolethread extends Thread{
//        String thuroleid;
//        public deleterolethread(String roleid){
//            thuroleid=roleid;
//        }
//        @Override
//        public void run() {
//            super.run();
//            deldeleterole(thuroleid);
//            globalVariable.setHttpreturn(true);
////            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
//        }
//    }
//    private void deldeleterole(String roleid){
//        HttpURLConnection connection = null;
//        try {
//
//            URL url=new URL(hostname+"/api/System/Identity/Role/"+roleid);
//
//            Log.d("kevin","URL "+url);
//            connection = (HttpURLConnection) url.openConnection();
////            connection.setConnectTimeout(5000);
//            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
////            connection.setRequestProperty("userId",userid);
//            connection.setReadTimeout(5000);
//            connection.setRequestMethod("DELETE");
//            connection.setDoInput(true);
//            connection.connect();
//
//
//            Log.d("kevin","deldeleterole:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
//            InputStream inputStream = connection.getInputStream();
//            if(inputStream != null) {
//                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader in = new BufferedReader(reader);
//                JSONArray jsonArraya;
////                JSONArray jsonArraya1;
//                JSONObject jsonObject;
//                JSONObject jsonObject1;
//                String line = "";
//                String s = "";
//
//                while ((line = in.readLine()) != null) {
//                    Log.d("kevin","deldeleterole:"+ line);
//                    s = line;
////                    globalVariable.writelog( " ChargingDeviceStatus "+s);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void getipcs(String id,String name,String page,String perPage){
        getipcsthread getipcsthread=new getipcsthread(id,name,page,perPage);
        getipcsthread.start();
    }
    private class getipcsthread extends Thread{

        private String thid;
        private String thname;
        private String thpage;
        private String thperpage;
        public getipcsthread(String id,String name,String page,String perPage){
            thid=id;
            thname=name;
            thpage=page;
            thperpage=perPage;
        }
        @Override
        public void run() {
            super.run();
            getgetipcs(thid,thname,thpage,thperpage);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetipcs(String id,String name,String page,String perPage){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetipcs");
            String u=hostname+"/api/Manage/IPC/Search?StationId="+id+"&name="+name;
            if(!page.equals("")||!perPage.equals("")){
//                u+="?";
                if(!page.equals("")&&!perPage.equals(""))
                    u+="&page="+page+"&perPage="+perPage;
                else if(!page.equals(""))
                    u+="&page="+page;
                else if(!perPage.equals(""))
                    u+="&perPage="+perPage;
            }
            URL url=new URL(u);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetipcs:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetipcs:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.setTotalpage(jsonObject.getInt("lastPage"));
                globalVariable.setTotalcount(jsonObject.getInt("total"));
                globalVariable.getIpclist().clear();

                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> ipc = new HashMap<String, String>();
                    ipc.put("id", jsonObject1.getString("id"));
                    ipc.put("name", jsonObject1.getString("name"));
                    ipc.put("countryId", jsonObject1.getString("countryId"));
                    ipc.put("countryName", jsonObject1.getString("countryName"));
                    ipc.put("cityId", jsonObject1.getString("cityId"));
                    ipc.put("cityName", jsonObject1.getString("cityName"));
                    ipc.put("siteId", jsonObject1.getString("siteId"));
                    ipc.put("siteName", jsonObject1.getString("siteName"));
                    ipc.put("zoneId", jsonObject1.getString("zoneId"));
                    ipc.put("zoneName", jsonObject1.getString("zoneName"));
                    ipc.put("lineId", jsonObject1.getString("lineId"));
                    ipc.put("lineName", jsonObject1.getString("lineName"));
                    ipc.put("stationId", jsonObject1.getString("lineName"));
                    ipc.put("stationName", jsonObject1.getString("lineName"));
                    ipc.put("ip", jsonObject1.getString("ip"));
                    ipc.put("macAddress", jsonObject1.getString("macAddress"));
                    if(jsonObject1.getString("description").equals("null"))
                        ipc.put("description", "");
                    else
                        ipc.put("description", jsonObject1.getString("description"));

                    ipc.put("createTime", jsonObject1.getString("createTime").toString().replace("T", " "));
                    ipc.put("updateTime", jsonObject1.getString("updateTime").toString().replace("T", " "));
                    globalVariable.getIpclist().add(ipc);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getipc(String id){
        getipcthread getipcthread=new getipcthread(id);
        getipcthread.start();
    }
    private class getipcthread extends Thread{

        private String thid;
        public getipcthread(String id){thid=id;}
        @Override
        public void run() {
            super.run();
            getgetipc(thid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetipc(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetipc");
            URL url=new URL(hostname+"/api/Manage/IPC/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetipc:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetipc:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));

                HashMap<String, String> ipc = new HashMap<String, String>();

                ipc.put("name", jsonObject1.getString("name"));
                if(jsonObject1.getString("computerName").equals("null"))
                    ipc.put("computerName", "");
                else
                    ipc.put("computerName", jsonObject1.getString("computerName"));
                if(jsonObject1.getString("systemType").equals("null"))
                    ipc.put("systemType", "---");
                else
                    ipc.put("systemType", jsonObject1.getString("systemType"));
                if(jsonObject1.getString("description").equals("null"))
                    ipc.put("description", "");
                else
                    ipc.put("description", jsonObject1.getString("description"));
                if(jsonObject1.getString("deviceKey").equals("null"))
                    ipc.put("deviceKey", "");
                else
                    ipc.put("deviceKey", jsonObject1.getString("deviceKey"));
                if(jsonObject1.getString("token").equals("null"))
                    ipc.put("token", "");
                else
                    ipc.put("token", jsonObject1.getString("token"));
                if(jsonObject1.getString("ip").equals("null"))
                    ipc.put("ip", "");
                else
                    ipc.put("ip", jsonObject1.getString("ip"));
                if(jsonObject1.getString("macAddress").equals("null"))
                    ipc.put("macAddress", "");
                else
                    ipc.put("macAddress", jsonObject1.getString("macAddress"));
                globalVariable.setSelmanage(ipc);

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createipc(String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){
        createipcthread createipcthread=new createipcthread(stationid,name,description,ComputerName,SystemType,IP,MAC,ToolIds);
        createipcthread.start();
    }
    private class createipcthread extends Thread{
        String thname;
        String thdescription;
        String thstationid;
        String thpcname;
        String thsystype;
        String thip;
        String thmac;
        String thtool;

        public createipcthread(String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){
            thname=name;
            thdescription=description;
            thstationid=stationid;
            thpcname=ComputerName;
            thsystype=SystemType;
            thip=IP;
            thmac=MAC;
            thtool=ToolIds;
        }
        @Override
        public void run() {
            super.run();
            postcreateipc(thstationid,thname,thdescription,thpcname,thsystype,thip,thmac,thtool);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreateipc(String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreateipc");
            URL url=new URL(hostname+"/api/Manage/IPC");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="StationId="+stationid+"&Name="+name+"&Description="+description+"&ComputerName="+ComputerName+"&SystemType="+SystemType+"&Ip="+IP+"&MacAddress="+MAC+"&ToolIds="+ToolIds;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreateipc:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateipc:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateipc:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void updataipc(String id,String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){
        updataipcthread updataipcthread=new updataipcthread(id,stationid,name,description,ComputerName,SystemType,IP,MAC,ToolIds);
        updataipcthread.start();
    }
    private class updataipcthread extends Thread{
        String thid;
        String thname;
        String thdescription;
        String thstationid;
        String thpcname;
        String thsystype;
        String thip;
        String thmac;
        String thtool;
        public updataipcthread(String id,String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){

            thid=id;
            thname=name;
            thdescription=description;
            thstationid=stationid;
            thpcname=ComputerName;
            thsystype=SystemType;
            thip=IP;
            thmac=MAC;
            thtool=ToolIds;

        }
        @Override
        public void run() {
            super.run();
            putupdataipc(thid,thstationid,thname,thdescription,thpcname,thsystype,thip,thmac,thtool);
            globalVariable.setHttpreturn(true);
        }
    }
    private void putupdataipc(String id,String stationid,String name,String description,String ComputerName,String SystemType,String IP,String MAC,String ToolIds){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http putupdataipc");
            URL url=new URL(hostname+"/api/Manage/IPC/"+id);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="StationId="+stationid+"&Name="+name+"&Description="+description+"&ComputerName="+ComputerName+"&SystemType="+SystemType+"&Ip="+IP+"&MacAddress="+MAC+"&ToolIds="+ToolIds;
            Log.d("kevin", ""+par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Log.d("kevin","putupdataipc:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdataipc:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }

                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "putupdataipc:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deleteipc(String id){
        deleteipcthread deleteipcthread=new deleteipcthread(id);
        deleteipcthread.start();
    }
    private class deleteipcthread extends Thread{
        String thid;
        public deleteipcthread(String id){
            thid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeleteipc(thid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeleteipc(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeleteipc");
            URL url=new URL(hostname+"/api/Manage/IPC/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeleteipc:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeletestation:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getscheldule(String ipcid){
        getscheldulethread getscheldulethread=new getscheldulethread(ipcid);
        getscheldulethread.start();
    }
    private class getscheldulethread extends Thread{

        String thipcid;
        public getscheldulethread(String ipcid){
            thipcid=ipcid;
        }
        @Override
        public void run() {
            super.run();
            getgetscheldule(thipcid);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetscheldule(String ipcid){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetscheldule");
            String urlstring=hostname+"/api/Remote/AtsTpTask/Search";
            if(!ipcid.equals(""))
                urlstring+="?IPCId="+ipcid;
            URL url=new URL(urlstring);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetscheldule:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetscheldule:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.getPcschedulelist().clear();

                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> schedule = new HashMap<String, String>();
                    schedule.put("id",jsonObject1.getString("id"));
                    schedule.put("testProgramName",jsonObject1.getString("testProgramName"));
                    schedule.put("planStartTime",jsonObject1.getString("planStartTime").replace("T", " "));
                    schedule.put("planEndTime",jsonObject1.getString("planEndTime").replace("T", " "));
                    if(jsonObject1.getString("actualStartTime").equals("null"))
                        schedule.put("actualStartTime","");
                    else
                        schedule.put("actualStartTime",jsonObject1.getString("actualStartTime").replace("T", " "));
                    if(jsonObject1.getString("actualEndTime").equals("null"))
                        schedule.put("actualEndTime","");
                    else
                        schedule.put("actualEndTime",jsonObject1.getString("actualEndTime").replace("T", " "));
                    if(jsonObject1.getString("testProgramVersion").equals("null"))
                        schedule.put("testProgramVersion","");
                    else
                        schedule.put("testProgramVersion",jsonObject1.getString("testProgramVersion"));
                    if(jsonObject1.getString("userName").equals("null"))
                        schedule.put("userName","");
                    else
                        schedule.put("userName",jsonObject1.getString("userName"));
                    if(jsonObject1.getString("departmentName").equals("null"))
                        schedule.put("departmentName","");
                    else
                        schedule.put("departmentName",jsonObject1.getString("departmentName"));
                    if(jsonObject1.getString("remark").equals("null"))
                        schedule.put("remark","");
                    else
                        schedule.put("remark",jsonObject1.getString("remark"));
                    globalVariable.getPcschedulelist().add(schedule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void createschedule(String ipcid,String tpname,String start,String end,String user,String dpname,String version,String remark){
        createschedulethread createschedulethread=new createschedulethread(ipcid,tpname,start,end,user,dpname,version,remark);
        createschedulethread.start();
    }
    private class createschedulethread extends Thread{
        String thipcid;
        String thtpname;
        String thstart;
        String thend;
        String thuser;
        String thdpname;
        String thversion;
        String thremark;
        public createschedulethread(String ipcid,String tpname,String start,String end,String user,String dpname,String version,String remark){
            thipcid=ipcid;
            thtpname=tpname;
            thstart=start;
            thend=end;
            thuser=user;
            thdpname=dpname;
            thversion=version;
            thremark=remark;
        }
        @Override
        public void run() {
            super.run();
            postcreateschedule(thipcid,thtpname,thstart,thend,thuser,thdpname,thversion,thremark);
            globalVariable.setHttpreturn(true);
        }
    }
    private void postcreateschedule(String ipcid,String tpname,String start,String end,String user,String dpname,String version,String remark){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http postcreateschedule");
            URL url=new URL(hostname+"/api/Remote/AtsTpTask");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String par="IPCId="+ipcid+"&TestProgramName="+tpname+"&PlanStartTime="+start+"&PlanEndTime="+end+"&UserName="+user+"&DepartmentName="+dpname+"&Remark="+remark+"&TestProgramVersion="+version;
            Log.d("kevin",par);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = par.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            Log.d("kevin","postcreateschedule:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());

            if(connection.getResponseCode()==200) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateschedule:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }


                }
            }else{
                InputStream inputStream = connection.getErrorStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader in = new BufferedReader(reader);
                    JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                    JSONObject jsonObject;
                    String line = "";
                    String s = "";

                    while ((line = in.readLine()) != null) {
                        Log.d("kevin", "postcreateschedule:" + line);
                        s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void deleteschedule(String id){
        deleteschedulethread deleteschedulethread=new deleteschedulethread(id);
        deleteschedulethread.start();
    }
    private class deleteschedulethread extends Thread{
        String thuid;
        public deleteschedulethread(String id){
            thuid=id;
        }
        @Override
        public void run() {
            super.run();
            deldeleteschedule(thuid);
            globalVariable.setHttpreturn(true);
//            Log.d("kevin","setHttpreturn:"+ globalVariable.isHttpreturn());
        }
    }
    private void deldeleteschedule(String id){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http deldeleteschedule");
            URL url=new URL(hostname+"/api/Remote/AtsTpTask/"+id);

            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
//            connection.setRequestProperty("userId",userid);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();


            Log.d("kevin","deldeleteschedule:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                JSONObject jsonObject1;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","deldeleteschedule:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getentity(){
        getentitythread getentitythread=new getentitythread();
        getentitythread.start();
    }
    private class getentitythread extends Thread{


        @Override
        public void run() {
            super.run();
            getgetentity();
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetentity(){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetentity");
            URL url=new URL(hostname+"/api/System/Parameter/Entity/");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetentity:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
//                    Log.d("kevin","getgetentity:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
//                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.getScopelist().clear();
                int entitypos=0;
                String[] entitylist={"data","cities","sites","zones","lines","stations","ipCs"};
                setentitydatanode(jsonObject,entitypos,entitylist,globalVariable.getScope(),globalVariable.getScopelist(),"-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getfunction(){
        getfunctionthread getfunctionthread=new getfunctionthread();
        getfunctionthread.start();
    }
    private class getfunctionthread extends Thread{


        @Override
        public void run() {
            super.run();
            getgetfunction();
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetfunction(){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetfunction");
            URL url=new URL(hostname+"/api/System/Parameter/Function/");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetfunction:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
//                    Log.d("kevin","getgetentity:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
//                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.getFunctionslist().clear();
                int functionpos=0;
                setfundatanode(jsonObject,functionpos,globalVariable.getFunctionlist(),globalVariable.getFunctionslist(),"-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getname(String level){
        getnamethread getnamethread=new getnamethread(level);
        getnamethread.start();
    }
    private class getnamethread extends Thread{
        String thlevel;
        public getnamethread(String level){
            thlevel=level;
        }

        @Override
        public void run() {
            super.run();
            getgetname(thlevel);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetname(String level){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetname");
            URL url=new URL(hostname+"/api/System/Parameter/Entity/"+level);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetname:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetname:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.getLevelname().clear();
                HashMap<String, String> initname = new HashMap<String, String>();
                initname.put("fullName","---");
                initname.put("name","---");
                initname.put("id","");
                globalVariable.getLevelname().add(initname);
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> Levelname = new HashMap<String, String>();
                    Levelname.put("name",jsonObject1.getString("name"));
                    Levelname.put("fullName",jsonObject1.getString("fullName"));
                    Levelname.put("id",jsonObject1.getString("id"));
                    globalVariable.getLevelname().add(Levelname);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void setentitydatanode(JSONObject jsonObject,int pos,String[] taglist,String[] idlist,List<Node> nodelist,String parentid){
        try {
            if(pos<taglist.length){
                if(!jsonObject.getString(taglist[pos]).equals("null")) {
                    JSONArray jsonArraya = new JSONArray(jsonObject.getString(taglist[pos]));
                    for (int i = 0; i < jsonArraya.length(); i++) {
//                        Log.d("kevin", pos + "   " + jsonArraya.get(i).toString());
                        JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
//                        if (pos == 0) {
                            nodelist.add(new Node(parentid, idlist[pos] +":"+ jsonObject1.getString("id"), jsonObject1.getString("name")));
                            setentitydatanode(jsonObject1, pos + 1, taglist,idlist, nodelist,idlist[pos] +":"+ jsonObject1.getString("id"));

//                        }else {
//                            nodelist.add(new Node(parentid, taglist[pos] + ":" + jsonObject1.getString("id"), jsonObject1.getString("name")));
//                            setentitydatanode(jsonObject1, pos + 1, taglist, nodelist,taglist[pos] + ":" + jsonObject1.getString("id"));
//
//                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void setfundatanode(JSONObject jsonObject,int pos,String[] taglist,List<Node> nodelist,String parentid){
        try {
            if(pos<taglist.length){
                if(!jsonObject.getString(taglist[pos]).equals("null")) {
                    JSONArray jsonArraya = new JSONArray(jsonObject.getString(taglist[pos]));
                    for (int i = 0; i < jsonArraya.length(); i++) {
//                        Log.d("kevin", pos + "   " + jsonArraya.get(i).toString());
                        JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                        nodelist.add(new Node(parentid, jsonObject1.getString("value"), jsonObject1.getString("label")));
                        setfundatanode(jsonObject1, pos + 1, taglist, nodelist, jsonObject1.getString("value"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void setscope(JSONObject jsonObject,List<String> value,List<String> label,String tag){
        try {
            JSONArray jsonArraya = new JSONArray(jsonObject.getString(tag));
            for(int i=0;i<jsonArraya.length();i++){
                JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                if(jsonObject1.getBoolean("checked")){
                    value.add(jsonObject1.getString("value"));
                    label.add(jsonObject1.getString("label"));
                    Log.d("kevin",jsonObject1.getString("value"));
                }
                setscope(jsonObject1,value,label,"children");
            }
        }catch (Exception e){
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getnotification(){
        getnotificationthread getnotificationthread=new getnotificationthread();
        getnotificationthread.start();
    }
    private class getnotificationthread extends Thread{


        @Override
        public void run() {
            super.run();
            getgetnotification();
            globalVariable.setHttpnotireturn(true);
        }
    }
    private void getgetnotification(){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetnotification");
            URL url=new URL(hostname+"/api/Notification");
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetnotification:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetnotification:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }
                jsonObject = new JSONObject(s);
                jsonArraya=new JSONArray(jsonObject.getString("data"));
                globalVariable.getNotificationlist().clear();
                for(int i=0;i<jsonArraya.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArraya.get(i).toString());
                    HashMap<String, String> noti = new HashMap<String, String>();
                    noti.put("id",jsonObject1.getString("id"));
                    noti.put("title",jsonObject1.getString("title"));
                    noti.put("content",jsonObject1.getString("content").replaceAll("<br />", "\n").replaceAll("</p>|<p>",""));
//                    noti.put("content",jsonObject1.getString("content"));
                    noti.put("createTime",jsonObject1.getString("createTime").replace("T", " "));
                    globalVariable.getNotificationlist().add(noti);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
    public void getremo(String pcname,String status,String tpname){
        getremothread getremothread=new getremothread(pcname,status,tpname);
        getremothread.start();
    }
    private class getremothread extends Thread{
        String thpcname;
        String thstatus;
        String thtpname;
        public getremothread(String pcname,String status,String tpname){

            thpcname=pcname;
            thstatus=status;
            thtpname=tpname;
        }
        @Override
        public void run() {
            super.run();
            getgetremo(thpcname,thstatus,thtpname);
            globalVariable.setHttpreturn(true);
        }
    }
    private void getgetremo(String pcname,String status,String tpname){
        HttpURLConnection connection = null;
        try {
            globalVariable.writelog("http getgetremo");
            URL url=new URL("http://10.136.217.185:50064"+"/RunJob/"+pcname+"/TPE/"+status+tpname);
            Log.d("kevin","URL "+url);
            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Authorization","Bearer "+globalVariable.getHttptoken());
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            Log.d("kevin","getgetremo:"+connection.getResponseCode()+";"+connection.getResponseMessage()+";"+connection.getErrorStream());
            InputStream inputStream = connection.getInputStream();
            if(inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                JSONArray jsonArraya;
//                JSONArray jsonArraya1;
                JSONObject jsonObject;
                String line = "";
                String s = "";

                while ((line = in.readLine()) != null) {
                    Log.d("kevin","getgetremo:"+ line);
                    s = line;
//                    globalVariable.writelog( " ChargingDeviceStatus "+s);
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
            globalVariable.writeexception(e);
        }
    }
}
