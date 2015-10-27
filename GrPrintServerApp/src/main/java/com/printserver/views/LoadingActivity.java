package com.printserver.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseParameters;
import com.printserver.dao.GPSClient;
import com.printserver.model.UserModel;
import com.printserver.service.Service_Carrier;
import com.printserver.service.Service_Common;
import com.printserver.service.Service_DoTask;
import com.printserver.service.Service_EntrustPrint;
import com.printserver.service.Service_UnTask;
import com.printserver.service.Service_User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhangxin on 2015/9/10.
 */
public class LoadingActivity extends Activity implements BaseParameters, View.OnClickListener {

    private ParameterApplication parameterApplication;
    private Bundle loadingData;
    private String loadingType;

    private TextView loadingText;
    private View view_main;
    private Thread thread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_main = getLayoutInflater().from(this).inflate(R.layout.loading_layout, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(view_main);
        BaseHelp.enterLightsOutMode(getWindow());
        loadingText = (TextView) findViewById(R.id.loading_txt);

        parameterApplication = (ParameterApplication) getApplication();
        loadingData = getIntent().getExtras();
        loadingType = loadingData.getString(LOADINGTYPE);

        intLoading();

        //thread = new Thread(runnable);
        //thread.start();
    }

    private void intLoading() {
        RequestParams params = null;
        switch (loadingType) {
            case INITSETUP:
                loadingText.setText("正在测试...");
                GPSClient.get(this, METHOTEST, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if ((RESULT_SUCCESS).equals(response.getString("result"))) {
                                RunCommon(new String[][]{{RESULT_SETUP, RESULT_SUCCESS}});
                            } else {
                                RunCommon(new String[][]{{RESULT_SETUP, RESULT_FAIL}});
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        RunCommon(new String[][]{{RESULT_SETUP, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        RunCommon(new String[][]{{RESULT_SETUP, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        RunCommon(new String[][]{{RESULT_SETUP, RESULT_FAIL}});
                    }

                });
                break;
            case LOGIN:
                loadingText.setText("正在登录...");
                params = new RequestParams();
                params.put("account", loadingData.getString("userAccount"));
                params.put("password", loadingData.getString("userPassword"));
                params.put("type", loadingData.getString("loginType"));
                GPSClient.get(this, METHOUSERINFO, params, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        List<UserModel> list = Service_User.UserToModelList(response);
                        if (list != null && list.size() > 0) {
                            UserModel userModel = Service_User.userLogin(list);
                            if (userModel == null) {
                                RunCommon(new String[][]{{RESULT_LOGIN, RESULT_FAIL}});
                            } else {
                                parameterApplication.setUserInfo(userModel);
                                RunCommon(new String[][]{{RESULT_LOGIN, RESULT_SUCCESS}});
                            }
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        RunCommon(new String[][]{{RESULT_LOGIN, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        RunCommon(new String[][]{{RESULT_LOGIN, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        RunCommon(new String[][]{{RESULT_LOGIN, RESULT_FAIL}});
                    }

                });
                break;
            case UNTASK:
                loadingText.setText("正在加载数据...");
                params = new RequestParams();
                params.put("userId", parameterApplication.getUserInfo().getUserID());
                params.put("pageIndex", "1");
                GPSClient.get(this, METHOUNPRINTINFO, params, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_UNTASK, null}});
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_UNTASK, s}});
                    }
                });
                break;
            case POSTUT:
                loadingText.setText("正在发送打印数据...");
                ArrayList<String> unTaskIds = loadingData.getStringArrayList("unTaskIds");
                String unTaskStr = "";
                if (unTaskIds.size() > 0) {
                    for (String tmp : unTaskIds) {
                        unTaskStr = tmp + ";" + unTaskStr;
                    }
                }
                unTaskStr = "ANDROID:" + parameterApplication.getUserInfo().getUserName() + ":" + parameterApplication.getUserInfo().getCardInfo() + ":" + unTaskStr.substring(0, unTaskStr.length() - 1);
                final String finalUnTaskStr = unTaskStr;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        acceptServer(finalUnTaskStr);
                    }
                }).start();
                break;
            case DOTASK:
                loadingText.setText("正在加载数据...");
                params = new RequestParams();
                params.put("userId", parameterApplication.getUserInfo().getUserID());
                params.put("pageIndex", "1");
                GPSClient.get(this, METHODOPRINTINFO, params, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_DOTASK, null}});
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_DOTASK, s}});
                    }
                });
                break;
            case TRANSFERCARRIER:
                loadingText.setText("正在加载数据...");
                params = new RequestParams();
                params.put("barcode", loadingData.getString("carrierID"));
                params.put("comefrom", "1");
                GPSClient.get(this, METHOCARRIERINFO, params, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_TC, null}});
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_TC, s}});
                    }
                });
                break;
            case ENTRUSTPRINT:
                loadingText.setText("正在加载数据...");
                GPSClient.get(this, METHOENTRUSTPRINTINFO, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_EP, null}});
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, String s) {
                        System.out.println(s);
                        RunCommon(new String[][]{{RESULT_EP, s}});
                    }
                });
                break;
            case POSTEP:
                loadingText.setText("正在让数据飞往服务器...");
                ArrayList<String> entrustPrintIds = loadingData.getStringArrayList("entrustPrintIds");
                String epIds = "0";
                if (entrustPrintIds != null) {
                    if (entrustPrintIds.size() > 0) {
                        for (String tmp : entrustPrintIds) {
                            epIds = epIds + ";" + tmp;
                        }
                    }
                }
                params = new RequestParams();
                params.put("epIds", epIds);
                GPSClient.get(this, METHOPOSTEPI, params, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if ((RESULT_SUCCESS).equals(response.getString("result"))) {
                                loadingText.setText("重新部署数据中...");
                                RequestParams params = new RequestParams();
                                params.put("userID", parameterApplication.getUserInfo().getUserID());
                                GPSClient.get(LoadingActivity.this, METHOENTRUSTPRINTINFO, params, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                                        System.out.println(s);
                                        RunCommon(new String[][]{{RESULT_POSTEP, RESULT_FAIL}});
                                    }

                                    @Override
                                    public void onSuccess(int i, Header[] headers, String s) {
                                        System.out.println(s);
                                        RunCommon(new String[][]{{RESULT_EP, s}, {RESULT_POSTEP, RESULT_SUCCESS}});
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            RunCommon(new String[][]{{RESULT_POSTEP, RESULT_FAIL}});
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        RunCommon(new String[][]{{RESULT_POSTEP, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        RunCommon(new String[][]{{RESULT_POSTEP, RESULT_FAIL}});
                    }

                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        RunCommon(new String[][]{{RESULT_POSTEP, RESULT_FAIL}});
                    }
                });
                break;
            case UPDATE:
                loadingText.setText("正在检查更新...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = "", version = "";
                        version = Service_Common.GetVersion(parameterApplication);
                        result = version;
                        if (result != null && !result.equals(getVersion())) {
                            RunCommon(new String[][]{{RESULT_UPDATE, RESULT_SUCCESS}, {"version", version}});
                        } else {
                            RunCommon(new String[][]{{RESULT_UPDATE, RESULT_FAIL}, {"version", version}});
                        }
                    }
                }).start();
                break;
        }
    }

    private void RunCommon(String[][] params) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        for (String[] tv : params) {
            bundle.putString(tv[0], tv[1]);
        }
        resultIntent.putExtras(bundle);
        LoadingActivity.this.setResult(RESULT_OK, resultIntent);
        LoadingActivity.this.finish();
        LoadingActivity.this.onDestroy();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (loadingType != null) {
                if (loadingType.equals(LOGIN)) {
                    loadingText.setText("正在登录...");
                    String result = "";
                    UserModel userInfo = new UserModel();
                    userInfo.setUserAccount(loadingData.getString("userAccount"));
                    userInfo.setUserPassword(loadingData.getString("userPassword"));
                    parameterApplication.setUserInfo(userInfo);
                    String loginType = loadingData.getString("loginType");
                    String userinfo = Service_User.getUserJson(parameterApplication, loginType);
                    UserModel userModel = Service_User.userLogin(Service_User.UserToModelList(userinfo));
                    if (userModel == null) {
                        result = RESULT_FAIL;
                    } else {
                        parameterApplication.setUserInfo(userModel);
                        result = RESULT_SUCCESS;
                    }

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_LOGIN, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(INITSETUP)) {
                    loadingText.setText("正在测试...");
                    String result = "";
                    if (Service_Common.TestConnect(parameterApplication)) {
                        result = RESULT_SUCCESS;
                    } else {
                        result = RESULT_FAIL;
                    }

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_SETUP, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(DOTASK)) {
                    loadingText.setText("正在加载数据...");
                    String result = "";
                    result = Service_DoTask.getDoTaskJson(parameterApplication, "1");

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_DOTASK, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(UNTASK)) {
                    loadingText.setText("正在加载数据...");
                    String result = "";
                    result = Service_UnTask.getUnTaskJson(parameterApplication, "1");

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_UNTASK, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(TRANSFERCARRIER)) {
                    loadingText.setText("正在加载数据...");
                    String result = "";
                    result = Service_Carrier.GetCarrierString(parameterApplication, loadingData.getString("carrierID"), "1");

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_TC, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(ENTRUSTPRINT)) {
                    loadingText.setText("正在加载数据...");
                    String result = "";
                    result = Service_EntrustPrint.getEntrustPrintString(parameterApplication);

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_EP, result);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(POSTEP)) {
                    loadingText.setText("正在让数据飞往服务器...");
                    ArrayList<String> entrustPrintIds = loadingData.getStringArrayList("entrustPrintIds");
                    String result = Service_EntrustPrint.getPostEPResult(parameterApplication, entrustPrintIds);
                    if (result.equals("ok")) {
                        loadingText.setText("重新部署数据中...");
                        result = Service_EntrustPrint.getEntrustPrintString(parameterApplication);

                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(RESULT_EP, result);
                        bundle.putString(RESULT_POSTEP, "ok");
                        resultIntent.putExtras(bundle);
                        LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                        LoadingActivity.this.finish();
                        LoadingActivity.this.onDestroy();
                    } else {
                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(RESULT_POSTEP, result);
                        resultIntent.putExtras(bundle);
                        LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                        LoadingActivity.this.finish();
                        LoadingActivity.this.onDestroy();
                    }
                }
                if (loadingType.equals(UPDATE)) {
                    loadingText.setText("正在检查更新...");
                    String result = "", version = "";
                    version = Service_Common.GetVersion(parameterApplication);
                    result = version;
                    if (result != null && !result.equals(getVersion())) {
                        result = RESULT_SUCCESS;
                    } else {
                        result = RESULT_FAIL;
                    }

                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_UPDATE, result);
                    bundle.putString("version", version);
                    resultIntent.putExtras(bundle);
                    LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                    LoadingActivity.this.finish();
                    LoadingActivity.this.onDestroy();
                }
                if (loadingType.equals(POSTUT)) {
                    loadingText.setText("正在发送打印数据...");
                    ArrayList<String> unTaskIds = loadingData.getStringArrayList("unTaskIds");
                    String unTaskStr = "";
                    if (unTaskIds.size() > 0) {
                        for (String tmp : unTaskIds) {
                            unTaskStr = tmp + ";" + unTaskStr;
                        }
                    }
                    unTaskStr = parameterApplication.getUserInfo().getUserName() + ":" + unTaskStr.substring(0, unTaskStr.length() - 1);
                    acceptServer(unTaskStr);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    // 获取版本号
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String str = "";
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x000) {
                Toast.makeText(LoadingActivity.this, str, Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(RESULT_POSTUT, RESULT_SUCCESS);
                resultIntent.putExtras(bundle);
                LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                LoadingActivity.this.finish();
                LoadingActivity.this.onDestroy();
            }
            if (msg.what == 0x123) {
                Toast.makeText(LoadingActivity.this, str, Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(RESULT_POSTUT, RESULT_FAIL);
                resultIntent.putExtras(bundle);
                LoadingActivity.this.setResult(RESULT_OK, resultIntent);
                LoadingActivity.this.finish();
                LoadingActivity.this.onDestroy();
            }
            if (msg.what == 0x124) {
                loadingText.setText("等待服务端响应中...");
            }
        }

        ;
    };

    public void acceptServer(String ids) {
        Socket socket = null;
        try {
            String listenip = parameterApplication.getListenIP();
            int listenport = Integer.parseInt(parameterApplication.getListenPort());
            socket = new Socket(listenip, listenport);
            if (socket == null)
                Log.v("serversocket_error", "error");
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println(ids);
            Log.v("向服务端发送：", ids);
            handler.sendEmptyMessage(0x124);
            String receive = bufferedReader.readLine();
            Log.v("服务端返回：", receive);
            str = receive;
            handler.sendEmptyMessage(0x000);
            bufferedReader.close();
            printStream.close();
            inputStream.close();
            outputStream.close();
        } catch (ConnectException e) {
            Log.v("connect_error", "服务端未开启！");
            str = "服务端未开启！";
            handler.sendEmptyMessage(0x123);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
