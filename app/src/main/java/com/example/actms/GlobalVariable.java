package com.example.actms;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import treeview.Node;

public class GlobalVariable extends Application {
    public String logpath= Environment.getExternalStorageDirectory()+"/ACTMS/";
    String ATSsel="";
    String TPsel="";
    String empname="";
    String empid="";
    String password="";
    String version="0.10";
    String httptoken="";
    int totalpage;
    int nowpage;
    List<String> pagelist=new ArrayList<String>();
    String selusername="";
    String seladaccount="";
    String selemail="";
    String selrolename="";
    String seldescription="";
    int totalcount=0;
    int pccount;
    int tpcount;
    int ticount;
    int ipcOnlineQty,ipcOfflineQty,tpPassQty,tpFailQty,tpFinishedQty,tpRunningQty;
    boolean httpreturn=false;
    boolean httpnotireturn=false;
    List<HashMap<String, String>> tpinfoList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> tiinfoList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> dailyList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> monthlyList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> yearlyList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> levelname=new ArrayList<HashMap<String, String>>();
    List<String> selrole=new ArrayList<String>();
    List<String> selscope=new ArrayList<String>();
    List<String> selscopelabel=new ArrayList<String>();
    List<String> selfinction=new ArrayList<String>();
    List<String> selfinctionlabel=new ArrayList<String>();
    List<HashMap<String, String>> userList=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> roleList=new ArrayList<HashMap<String, String>>();
    List<Node>  scopelist =new ArrayList<Node>();
    List<Node>  functionslist =new ArrayList<Node>();
    String[] scope={"Country","City","Site","Zone","Line","Station","IPC"};
    String[] scopename={"countryName","cityName","siteName","zoneName","lineName","stationName"};
    String[] scopeid={"countryId","cityId","siteId","zoneId","lineId","stationId"};
    String[] functionlist={"functions","children"};
    List<HashMap<String, String>> countrylist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> citylist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> sitelist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> zonelist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> linelist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> stationlist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> ipclist=new ArrayList<HashMap<String, String>>();

    List<HashMap<String, String>> pcschedulelist=new ArrayList<HashMap<String, String>>();
    List<HashMap<String, String>> notificationlist=new ArrayList<HashMap<String, String>>();
    HashMap<String, String> selmanage=new HashMap<String, String>();
    public List<HashMap<String, String>> getTpinfoList() {
        return tpinfoList;
    }

    public List<HashMap<String, String>> getDailyList() {
        return dailyList;
    }

    public List<HashMap<String, String>> getMonthlyList() {
        return monthlyList;
    }

    public List<HashMap<String, String>> getYearlyList() {
        return yearlyList;
    }

    public List<HashMap<String, String>> getTiinfoList() {
        return tiinfoList;
    }

    public void setATSsel(String ATSsel) {
        this.ATSsel = ATSsel;
    }

    public String getATSsel() {
        return ATSsel;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

    public void setTPsel(String TPsel) {
        this.TPsel = TPsel;
    }

    public String getTPsel() {
        return TPsel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getVersion() {
        return version;
    }

    public void setHttpreturn(boolean httpreturn) {
        this.httpreturn = httpreturn;
    }

    public boolean isHttpreturn() {
        return httpreturn;
    }

    public int getPccount() {
        return pccount;
    }

    public void setPccount(int pccount) {
        this.pccount = pccount;
    }

    public int getTicount() {
        return ticount;
    }

    public int getTpcount() {
        return tpcount;
    }

    public void setTicount(int ticount) {
        this.ticount = ticount;
    }

    public void setTpcount(int tpcount) {
        this.tpcount = tpcount;
    }

    public String getHttptoken() {
        return httptoken;
    }

    public void setHttptoken(String httptoken) {
        this.httptoken = httptoken;
    }

    public List<HashMap<String, String>> getLevelname() {
        return levelname;
    }

    public List<HashMap<String, String>> getRoleList() {
        return roleList;
    }

    public List<HashMap<String, String>> getUserList() {
        return userList;
    }

    public String getSeladaccount() {
        return seladaccount;
    }

    public String getSelusername() {
        return selusername;
    }

    public String getSelemail() {
        return selemail;
    }



    public void setSeladaccount(String seladaccount) {
        this.seladaccount = seladaccount;
    }

    public void setSelemail(String selemail) {
        this.selemail = selemail;
    }

    public void setSelusername(String selusername) {
        this.selusername = selusername;
    }

    public List<String> getSelrole() {
        return selrole;
    }

    public void setSelrole(List<String> selrole) {
        this.selrole = selrole;
    }

    public List<Node> getScopelist() {
        return scopelist;
    }

    public List<Node> getFunctionslist() {
        return functionslist;
    }

    public String[] getScope() {
        return scope;
    }

    public String[] getFunctionlist() {
        return functionlist;
    }

    public List<String> getSelfinction() {
        return selfinction;
    }

    public List<String> getSelscope() {
        return selscope;
    }

    public String getSeldescription() {
        return seldescription;
    }

    public String getSelrolename() {
        return selrolename;
    }

    public void setSeldescription(String seldescription) {
        this.seldescription = seldescription;
    }

    public void setSelrolename(String selrolename) {
        this.selrolename = selrolename;
    }

    public List<String> getSelfinctionlabel() {
        return selfinctionlabel;
    }

    public List<String> getSelscopelabel() {
        return selscopelabel;
    }

    public List<HashMap<String, String>> getCountrylist() {
        return countrylist;
    }

    public List<HashMap<String, String>> getCitylist() {
        return citylist;
    }

    public String[] getScopename() {
        return scopename;
    }

    public List<HashMap<String, String>> getSitelist() {
        return sitelist;
    }

    public List<HashMap<String, String>> getZonelist() {
        return zonelist;
    }

    public List<HashMap<String, String>> getLinelist() {
        return linelist;
    }

    public List<HashMap<String, String>> getStationlist() {
        return stationlist;
    }

    public List<HashMap<String, String>> getIpclist() {
        return ipclist;
    }

    public List<HashMap<String, String>> getPcschedulelist() {
        return pcschedulelist;
    }

    public HashMap<String, String> getSelmanage() {
        return selmanage;
    }

    public void setSelmanage(HashMap<String, String> selmanage) {
        this.selmanage = selmanage;
    }

    public String[] getScopeid() {
        return scopeid;
    }

    public List<HashMap<String, String>> getNotificationlist() {
        return notificationlist;
    }

    public boolean isHttpnotireturn() {
        return httpnotireturn;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public void setHttpnotireturn(boolean httpnotireturn) {
        this.httpnotireturn = httpnotireturn;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getIpcOfflineQty() {
        return ipcOfflineQty;
    }

    public int getIpcOnlineQty() {
        return ipcOnlineQty;
    }

    public int getTpFailQty() {
        return tpFailQty;
    }

    public int getTpFinishedQty() {
        return tpFinishedQty;
    }

    public int getTpPassQty() {
        return tpPassQty;
    }

    public int getTpRunningQty() {
        return tpRunningQty;
    }

    public void setIpcOfflineQty(int ipcOfflineQty) {
        this.ipcOfflineQty = ipcOfflineQty;
    }

    public void setIpcOnlineQty(int ipcOnlineQty) {
        this.ipcOnlineQty = ipcOnlineQty;
    }

    public void setTpFailQty(int tpFailQty) {
        this.tpFailQty = tpFailQty;
    }

    public void setTpFinishedQty(int tpFinishedQty) {
        this.tpFinishedQty = tpFinishedQty;
    }

    public void setTpPassQty(int tpPassQty) {
        this.tpPassQty = tpPassQty;
    }

    public void setTpRunningQty(int tpRunningQty) {
        this.tpRunningQty = tpRunningQty;
    }

    public void writelog(String log){
        Date date=new Date();
        SimpleDateFormat sdFormat1 =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS=> ");
        File file1 = new File( logpath+ "devicelog/", sdFormat1.format(date)+".txt");
        Log.d("kevin",logpath);
        try {
            if(!file1.exists()) {
                file1.getParentFile().mkdirs();
                file1.createNewFile();
            }
            FileOutputStream fos1 = new FileOutputStream(file1,true);
            fos1.write((sdFormat.format(date)+log+"\n").getBytes());
            fos1.flush();
            fos1.getFD().sync();
            fos1.close();
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void writeexception(Exception log){
        Date date=new Date();
        SimpleDateFormat sdFormat1 =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS=> ");
        File file1 = new File( logpath+"devicelog/", sdFormat1.format(date)+".txt");
        try {
            if(!file1.exists()) {
                file1.getParentFile().mkdirs();
                file1.createNewFile();
            }
            FileOutputStream fos1 = new FileOutputStream(file1,true);
            StringWriter errors = new StringWriter();
            log.printStackTrace(new PrintWriter(errors));
            fos1.write((sdFormat.format(date)+errors.toString()+"\n").getBytes());
            fos1.flush();
            fos1.getFD().sync();
            fos1.close();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public List<String> getPagelist() {
        pagelist.clear();
        for(int i=0;i<totalpage;i++){
            pagelist.add(String.valueOf(i+1));
        }
        return pagelist;
    }
    public void cklogfile(){
        try {
            writelog("cklogfile");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            File[] file = new File(logpath + "devicelog/").listFiles();
            for (int i = 0; i < file.length; i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdFormat.parse(file[i].getName().split("\\.")[0]));
                if (cal.before(calendar)){
                    writelog("del logfile "+file[i].getName());
                    file[i].delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeexception(e);
        }
    }
}
