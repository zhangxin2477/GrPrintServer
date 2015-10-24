package com.printserver.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.printserver.base.BaseContext;
import com.printserver.base.BaseFragmentActivity;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseParameters;
import com.printserver.dao.GPSClient;
import com.printserver.service.Service_Common;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class InitActivity extends BaseFragmentActivity implements View.OnClickListener, BaseParameters {

    private ParameterApplication parameterApplication;
    private TextView tip;
    private ButtonFlat setup;
    private View view_main;
    public boolean initLoadState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_main = getLayoutInflater().from(this).inflate(R.layout.init_layout, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //view_main.setOnClickListener(this);
        setContentView(R.layout.init_layout);
        BaseHelp.enterLightsOutMode(getWindow());

        initSetup();
        tip = (TextView) findViewById(R.id.init_txt);
        setup = (ButtonFlat) findViewById(R.id.init_setup);
        setup.textButton.setTextColor(Color.parseColor("#FFFFFF"));
        parameterApplication = (ParameterApplication) getApplication();

        setup.setOnClickListener(this);
        if (!BaseHelp.CheckNetworkConnect(this)) {
            tip.setText("您未连接网络，请检查设备！");
            Toast.makeText(this, "您未连接网络，请检查设备！", Toast.LENGTH_LONG).show();
            return;
        }

        initLoading();
//        new Thread(runnable).start();
//        handler.postDelayed(runnable, 15000);
    }

    private void initSetup() {
        BaseContext baseContext = new BaseContext(this);
        if (baseContext.ReadDataValue("webip").equals("")) {
            baseContext.WriteData("webip", "192.168.1.107");
            baseContext.WriteData("webport", "80");
            baseContext.WriteData("webvrid", "print");
        }
    }

    private void initLoading() {
        GPSClient.get(this, METHOTEST, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (("ok").equals(response.getString("result"))) {
                        System.out.println("ok");
                        initLoadState = false;
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            //切换
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(InitActivity.this, LoginActivity.class);
                                startActivity(intent);
                                InitActivity.this.finish();
                            }
                        };
                        timer.schedule(task, 3000);
                    } else {
                        System.out.println("connected->no");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //back = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(statusCode + responseString);
                tip.setText("服务器无法连接，请检查配置！");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                System.out.println("retry");
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            boolean result = Service_Common.TestConnect(parameterApplication);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("result", result);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            boolean result = bundle.getBoolean("result");
            if (initLoadState) {
                if (result) {
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        //切换
                        @Override
                        public void run() {
                            handler.removeCallbacks(runnable);
                            initLoadState = false;
                            Intent intent = new Intent();
                            intent.setClass(InitActivity.this, LoginActivity.class);
                            startActivity(intent);
                            InitActivity.this.finish();
                            //InitActivity.this.onDestroy();
                        }
                    };
                    timer.schedule(task, 3000);
                } else {
                    tip.setText("服务器无法连接，请检查配置！");
                    Toast.makeText(InitActivity.this, "服务器无法连接，请检查配置！", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setup:
                Intent intent = new Intent();
                intent.setClass(InitActivity.this, SetupActivity.class);
                intent.putExtra(LOADINGTYPE, INIT);
                startActivityForResult(intent, 0);
                initLoadState = false;
                break;
            case R.id.menu_retry:
                initLoading();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        switch (v.getId()) {
            case R.id.init_setup:
                openOptionsMenu();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                tip.setText("尝试重新连接...");
                parameterApplication = (ParameterApplication) getApplication();
                initLoadState = true;
                initLoading();
                break;
        }
    }
}
