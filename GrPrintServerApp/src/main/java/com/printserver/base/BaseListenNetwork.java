package com.printserver.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by zhangxin on 15-10-23.
 */
public class BaseListenNetwork extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo ethInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        Toast.makeText(context, "mobile:" + mobileInfo.isConnected()
                + "\n" + "wifi:" + wifiInfo.isConnected()
                + "\n" + "eth:" + ethInfo.isConnected()
                + "\n" + "active:" + activeInfo.getTypeName(), Toast.LENGTH_LONG).show();
    }
}
