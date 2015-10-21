package com.printserver.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.printserver.views.HomeActivity;
import com.printserver.views.LoginActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/2.
 */
public class BaseHelp {

    private static String namespace = "http://tempuri.org/";

    public static void ShowDialog(final Context v,String title,int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(v);
        if (type==0) {
            builder.setTitle("提示").setMessage(title)
                    .setCancelable(false)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
        }
        if (type==1){
            builder.setTitle("提示").setMessage(title)
                    .setCancelable(false)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String FormatTime(String time){
        if (time==null||time.equals("")){
            return "";
        }else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = null;
            try {
                date = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
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

    public static boolean CheckNetworkConnect(Activity MainActivity) {
        Context context = MainActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean PingIP(String host) {
        boolean result=false;
        Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
        Process process = null; // 声明处理类对象
        String line = null; // 返回行信息
        InputStream is = null; // 输入流
        InputStreamReader isr = null; // 字节流
        BufferedReader br = null;
        try {
            process = runtime.exec("ping " + host+" -n 1 -w 3"); // PING
            is = process.getInputStream(); // 实例化输入流
            isr = new InputStreamReader(is);// 把输入流转换成字节流
            br = new BufferedReader(isr);// 从字节中读取文本
            process.waitFor();
            while ((line = br.readLine()) != null) {
                if (line.contains("TTL")) {
                    result = true;
                    break;
                }
            }
            is.close();
            isr.close();
            br.close();
            if (result) {
                System.out.println("ping 通  ...");
            } else {
                System.out.println("ping 不通...");
            }
        } catch (IOException e) {
            System.out.println(e);
            runtime.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            runtime.exit(1);
        }
        return result;
    }

    public static String LoadMethods_GetString(String ip, String port, String vdir, String methodName, Map<String,String> parameters) {
        String result=null;
        String endPoint = "http://" + ip + ":" + port +"/"+ vdir + "/PrinterServices/PrintService.asmx";
        String soapAcion = "http://" + ip + ":" + port +"/"+ vdir + "/PrinterServices/PrintService.asmx/" + methodName;
        SoapObject soapObject = new SoapObject(namespace, methodName);
        if (parameters!=null) {
            for (Map.Entry<String, String> params : parameters.entrySet()) {
                soapObject.addProperty(params.getKey(), params.getValue());
            }
        }
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet=true;
        HttpTransportSE transportSE=new HttpTransportSE(endPoint);
        try {
            transportSE.call(soapAcion, envelope);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return result;
        }
        SoapObject object = (SoapObject) envelope.bodyIn;
        result = object.getProperty(0).toString();
        return result;
    }

    public static SoapObject LoadMethods_GetMap(String ip, String port, String vdir, String methodName, Map<String,String> parameters) {
        SoapObject result=null;
        String endPoint = "http://" + ip + ":" + port +"/"+ vdir +"/PrinterServices/PrintService.asmx";
        String soapAcion = "http://" + ip + ":" + port + "/" + methodName;
        SoapObject soapObject = new SoapObject(namespace, methodName);
        if (parameters!=null) {
            for (Map.Entry<String, String> params : parameters.entrySet()) {
                soapObject.addProperty(params.getKey(), params.getValue());
            }
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        HttpTransportSE transportSE = new HttpTransportSE(endPoint);
        try {
            transportSE.call(soapAcion, envelope);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return result;
        }
        SoapObject object = null;
        try {
            object = (SoapObject) envelope.getResponse();
        } catch (SoapFault e) {
            e.printStackTrace();
            return result;
        }
        object = (SoapObject) object.getProperty(1);
        if (object.toString().equals("anyType{}")) {
            Log.v("object1", object.toString());
        } else {
            object = (SoapObject) object.getProperty(0);
            result=object;
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

    public static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getLocalInetAddress(InetAddress ip){
        return ip.toString();
    }

    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public static String getMacAddress(){
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();

            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append('-');
                }

                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strMacAddr;
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

    public static void enterLightsOutMode(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        window.setAttributes(params);
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

    public static String GetLocalIP(){
        JSONObject jsonObject = BaseHelp.getEthernet();
        if (jsonObject.length() <= 0) {
            jsonObject = BaseHelp.getWirelessnet();
        }
        String ip = "";
        try {
            ip = jsonObject.get("IPAddress").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
