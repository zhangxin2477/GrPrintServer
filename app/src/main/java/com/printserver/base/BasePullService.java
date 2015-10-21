package com.printserver.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by zhangxin on 2015/10/13.
 */
public class BasePullService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("oncreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
