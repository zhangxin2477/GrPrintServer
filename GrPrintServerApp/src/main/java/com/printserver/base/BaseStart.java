package com.printserver.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.printserver.views.InitActivity;

/**
 * Created by zhangxin on 2015/9/16.
 */
public class BaseStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            ComponentName comp = new ComponentName(context.getPackageName(), InitActivity.class.getName());
            context.startActivity(new Intent().setComponent(comp).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Log.e("customTag", "Received unexpected intent " + intent.toString());
        }
    }

}
