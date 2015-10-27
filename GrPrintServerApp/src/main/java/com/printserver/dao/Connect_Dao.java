package com.printserver.dao;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.printserver.base.BaseHelp;
import com.printserver.views.ParameterApplication;

import org.json.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class Connect_Dao {

    private static String ip = "";
    private static String port = "";
    private static String vdir = "";

    public static boolean TestConnect(ParameterApplication parameterApplication) {
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        try {
            String result = BaseHelp.GetAllData(ip, port, vdir, "AndroidTestConnect", null);
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("result").equals("ok")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
