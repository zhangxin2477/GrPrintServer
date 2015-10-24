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

    private static String ip="";
    private static String port="";
    private static String vdir="";

    public static boolean TestConnect(ParameterApplication parameterApplication) {
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        try {
            String result = BaseHelp.GetAllData(ip, port, vdir, "AndroidTestConnect", null);
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("result").equals("ok")){
                return true;
            }else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean TestConnect(Context context) {
        GPSClient.get(context, "AndroidTestConnect", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (("ok").equals(response.getString("result"))) {
                        System.out.println("ok");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //back = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
        return true;
    }
}
