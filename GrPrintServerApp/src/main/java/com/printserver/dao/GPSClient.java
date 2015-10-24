package com.printserver.dao;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.printserver.base.BaseContext;

/**
 * Created by zhangxin on 15-10-23.
 */
public class GPSClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {client.setTimeout(10000);}

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url,context), params, responseHandler);
    }

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url, context), responseHandler);
    }

    public static void post(String url, Context context, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url,context), params, responseHandler);
    }

    public static AsyncHttpClient getClient(){
        return client;
    }

    private static String getAbsoluteUrl(String relativeUrl,Context context) {
        BaseContext baseContext=new BaseContext(context);
        String ip = baseContext.ReadDataValue("webip");
        String port = baseContext.ReadDataValue("webport");
        String vrid = baseContext.ReadDataValue("webvrid");
        String BASE_URL = "http://" + ip + ":" + port + "/" + vrid + "/PrinterServices/PrintService.asmx/";
        return BASE_URL + relativeUrl;
    }
}
