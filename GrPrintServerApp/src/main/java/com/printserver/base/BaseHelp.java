package com.printserver.base;


import android.content.Context;
import android.net.*;
import android.util.Log;
import android.view.View;

import com.gc.materialdesign.widgets.Dialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/2.
 */
public class BaseHelp {

    public static void ShowDialog(final Context v,String title,int type){
        final Dialog dialog=new Dialog(v,"提示",title,type,"确定");
        if (type==0){
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });
        }
        dialog.show();
    }

    //字符序列转换为16进制字符串
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);

        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString2(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static boolean CheckNetworkConnect(Context context) {
        boolean result=false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo ethInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        Log.v("networkInfo", "mobile:" + mobileInfo.isConnected()
                + "\n" + "wifi:" + wifiInfo.isConnected()
                + "\n" + "eth:" + ethInfo.isConnected()
                + "\n" + "active:" + activeInfo.getTypeName());
        if (wifiInfo.isConnected()||ethInfo.isConnected()){
            result = true;
        }
        return result;
    }

    public static String GetAllData(String ip, String port, String vdir, String methodName, Map<String, String> parameters) {
        String result = "";
        try {
            String url = "http://" + ip + ":" + port + "/" + vdir + "/PrinterServices/PrintService.asmx/" + methodName;
            HttpPost request = new HttpPost(url);
            List<BasicNameValuePair> params = new ArrayList();
            if (parameters != null) {
                for (Map.Entry<String, String> tmp : parameters.entrySet()) {
                    params.add(new BasicNameValuePair(tmp.getKey(), tmp.getValue()));
                }
            }
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
            defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
            HttpResponse httpResponse = defaultHttpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //getprop->
    /*WIFI模块-zx
    *dhcp.wlan0.dns1
    *dhcp.wlan0.ipaddress
    * dhcp.wlan0.mask
    * dhcp.wlan0.gateway
    * dhcp.wlan0.result-》ok/failed
     */
    /*以太网模块-zx
    *ifconfig eth0
    * net.eth0.dns1
     */
    private static String runLinux(String key){
        String value="";
        try {

            Process localProcess = Runtime.getRuntime().exec(key);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
            String tmp=null;
            while((tmp=bufferedReader.readLine()) != null) {
                Log.v("value", tmp);
                value += tmp;
            }
            localProcess.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;

    }

    private static JSONObject getGateway(){
        JSONObject gateway=new JSONObject();
        try {
            String value = runLinux("ip route show");
            if (value.contains("wlan0")) {
                String wlan0 = value.substring(0, value.indexOf("wlan0") + 5);
                gateway.put("wlan0", wlan0.substring(wlan0.lastIndexOf("default via") + 12, wlan0.indexOf("dev wlan0")).replace(" ", ""));
            }
            if ((value.contains("eth0"))) {
                String eth0=value.substring(0,value.indexOf("eth0")+5);
                gateway.put("eth0",eth0.substring(eth0.lastIndexOf("default via") + 12, eth0.indexOf("dev eth0")).replace(" ", ""));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return gateway;
    }

    public static JSONObject getWirelessnet(){
        JSONObject jsonObject=new JSONObject();
        try {
            String tmp = runLinux("getprop ro.build.characteristics");
            String value = runLinux("ifconfig wlan0");
            if (tmp.contains("phone")) {
                jsonObject.put("IPAddress", runLinux("getprop dhcp.wlan0.ipaddress"));
                jsonObject.put("Mask", runLinux("getprop dhcp.wlan0.mask"));
                jsonObject.put("Gateway", runLinux("getprop dhcp.wlan0.gateway"));
            } else {//tablet
                if (value.contains("HWaddr")) {
                    jsonObject.put("HWAddress", value.substring(value.indexOf("HWaddr") + 7, value.indexOf("HWaddr") + 26).replace(" ", ""));
                }
                if (value.contains("inet addr")) {
                    jsonObject.put("IPAddress", value.substring(value.indexOf("inet addr") + 10, value.indexOf("Bcast") - 2).replace(" ", ""));
                    jsonObject.put("Bcast", value.substring(value.indexOf("Bcast") + 6, value.indexOf("Mask") - 2).replace(" ", ""));
                    jsonObject.put("Mask", value.substring(value.indexOf("Mask") + 5, value.indexOf("Mask") + 21).replace(" ", ""));
                }
                jsonObject.put("Gateway", getGateway().get("wlan0"));
            }
            jsonObject.put("Dns", runLinux("getprop dhcp.wlan0.dns1").replace(" ", ""));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getEthernet(){
        JSONObject jsonObject=new JSONObject();
        try {
            String value = runLinux("ifconfig eth0");
            if (value.contains("HWaddr")) {
                jsonObject.put("HWAddress", value.substring(value.indexOf("HWaddr") + 7, value.indexOf("HWaddr") + 26).replace(" ", ""));
            }
            if (value.contains("inet addr")) {
                jsonObject.put("IPAddress",value.substring(value.indexOf("inet addr") + 10, value.indexOf("Bcast") - 2).replace(" ", ""));
                jsonObject.put("Bcast", value.substring(value.indexOf("Bcast") + 6, value.indexOf("Mask") - 2).replace(" ", ""));
                jsonObject.put("Mask", value.substring(value.indexOf("Mask") + 5, value.indexOf("Mask") + 21).replace(" ", ""));
            }
            jsonObject.put("Gateway",getGateway().get("eth0"));
            jsonObject.put("Dns", runLinux("getprop net.eth0.dns1").replace(" ", ""));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static String GetHtml(String ip, String port, String vdir) {
        String urlString="http://" + ip + ":" + port +"/"+ vdir +"/PrintCSUpdate/Android/androidupdate.html";
        String html = null;
        try {
            java.net.URL url = new java.net.URL(urlString); //根据 String 表示形式创建 URL 对象。
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();// 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
            java.io.InputStreamReader isr = new java.io.InputStreamReader(conn.getInputStream());//返回从此打开的连接读取的输入流。
            java.io.BufferedReader br = new java.io.BufferedReader(isr);//创建一个使用默认大小输入缓冲区的缓冲字符输入流。

            String temp;
            while ((temp = br.readLine()) != null) { //按行读取输出流
                if(!temp.trim().equals("")){
                    html+=temp;
                }
            }
            br.close(); //关闭
            isr.close(); //关闭

        } catch (Exception e) {
            e.printStackTrace();
            html=null;
        }
        return html; //返回此序列中数据的字符串表示形式。
    }

    public static String GetDownUrl(String ip, String port, String vdir){
        return "http://" + ip + ":" + port +"/"+ vdir +"/PrintCSUpdate/Android/GrPrintServer.apk";
    }
}
