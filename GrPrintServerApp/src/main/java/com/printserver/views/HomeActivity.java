package com.printserver.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ListView;

import com.printserver.base.BaseFragment;
import com.printserver.base.widgets.BaseListView;
import com.printserver.base.framents.DoTaskFragment;
import com.printserver.base.framents.EntrustPrintFragment;
import com.printserver.base.framents.LogoFragment;
import com.printserver.base.framents.SetupFragment;
import com.printserver.base.framents.TransferCarrierFragment;
import com.printserver.base.framents.UnTaskFragmemt;
import com.printserver.base.adapter.DoTaskListAdapter;
import com.printserver.base.adapter.EntrustPrintAdapter;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseFragmentActivity;
import com.printserver.base.BaseParameters;
import com.printserver.base.BaseSlidingMenu;
import com.printserver.base.adapter.UnTaskListAdapter;
import com.printserver.base.menus.LeftFragmemt;
import com.printserver.base.menus.RightFrament;
import com.printserver.service.Service_Common;
import com.printserver.service.Service_DoTask;
import com.printserver.service.Service_EntrustPrint;
import com.printserver.service.Service_UnTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class HomeActivity extends BaseFragmentActivity implements BaseParameters,View.OnClickListener{

    BaseSlidingMenu baseSlidingMenu;
    TransferCarrierFragment transferCarrierFrament;
    EntrustPrintFragment entrustPrintFrament;
    SetupFragment setupFrament;
    UnTaskFragmemt unTaskFragmemt;
    DoTaskFragment doTaskFrament;
    LogoFragment logoFrament;
    LeftFragmemt leftFragmemt;
    RightFrament rightFrament;

    private ListView listView_ep;
    public EntrustPrintAdapter entrustPrintAdapter;

    private EditText carrierID;
    private View view_main;

    private BaseListView listView_dt,listView_ut;
    private DoTaskListAdapter doTaskListAdapter;
    public UnTaskListAdapter unTaskListAdapter;
    private List<Map<String, Object>> doTaskListData,unTaskListData;
    public int firstDTD,lastDTD,firstUTD,lastUTD;//记录刷新记录

    // 文件当前路径
    private String currentFilePath = "";
    // 安装包文件临时路径
    private String currentTempFilePath = "";
    // 获得文件扩展名字符串
    private String fileEx = "";
    // 获得文件名字符串
    private String fileNa = "";
    // 服务器地址
    private String strURL = "";
    private ProgressDialog dialog;

    private static final String HOST = "192.168.1.124";
    private static final int PORT = 12345;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    private StringBuilder sb = null;

    protected void onCreate(Bundle home){
        super.onCreate(home);
        view_main=getLayoutInflater().from(this).inflate(R.layout.home_layout,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //view_main.setOnClickListener(this);
        setContentView(view_main);
        BaseHelp.enterLightsOutMode(getWindow());
        init();
        initListener();
    }

    private void init() {
        baseSlidingMenu=(BaseSlidingMenu)findViewById(R.id.homeSlidingMenu);
        baseSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.leftframe_layout, null));
        baseSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.rightframe_layout, null));
        baseSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.centerframe_layout, null));

        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        leftFragmemt = new LeftFragmemt();
        t.replace(R.id.left_frame, leftFragmemt);

        rightFrament = new RightFrament();
        t.replace(R.id.right_frame, rightFrament);

        logoFrament =new LogoFragment();
        t.replace(R.id.center_frame, logoFrament);
        t.commit();
    }

    private void initListener() {
        if (unTaskFragmemt !=null) {
            unTaskFragmemt.setMyPageChangeListener(new UnTaskFragmemt.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
        if (doTaskFrament!=null){
            doTaskFrament.setMyPageChangeListener(new DoTaskFragment.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
        if (logoFrament!=null) {
            logoFrament.setMyPageChangeListener(new LogoFragment.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
        if (setupFrament!=null) {
            setupFrament.setMyPageChangeListener(new SetupFragment.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
        if (transferCarrierFrament!=null) {
            transferCarrierFrament.setMyPageChangeListener(new TransferCarrierFragment.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
        if (entrustPrintFrament!=null) {
            entrustPrintFrament.setMyPageChangeListener(new EntrustPrintFragment.MyPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (true) {
                        baseSlidingMenu.setCanSliding(true, false);
                    } else if (true) {
                        baseSlidingMenu.setCanSliding(false, true);
                    } else {
                        baseSlidingMenu.setCanSliding(false, false);
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showLeft() {
        baseSlidingMenu.showLeftView();
    }

    public void showRight() {
        baseSlidingMenu.showRightView();
    }

    public void switchFrameActivity(BaseFragment fragment){
        if(fragment!=null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            t.replace(R.id.center_frame, fragment);
            t.commit();
        }
    }

    public void setCarrierID(EditText editText){
        this.carrierID=editText;
    }

    public void setListView_dt(BaseListView listView){
        this.listView_dt=listView;
    }

    public void setListView_ut(BaseListView listView){
        this.listView_ut=listView;
    }

    public void setListView_ep(ListView listView){
        this.listView_ep=listView;
    }

    public ParameterApplication getParamaterApplication(){
        return (ParameterApplication)getApplication();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String Result = bundle.getString("carrierid");
                if (Result!=null) {
                    if (carrierID.getText().toString().contains(Result)){
                        return;
                    }
                    Result = Result + ";" + carrierID.getText().toString();
                    carrierID.setText(Result);
                }
                Result=bundle.getString("dotaskResult");
                if (Result!=null){
                    doTaskListData=Service_DoTask.getDoTaskMaps(Result);
                    //firstDTD=Service_DoTask.getFirstDoTaskId(Result);
                    lastDTD=1;//Service_DoTask.getLastDoTaskId(Result);
                    doTaskListAdapter=new DoTaskListAdapter(this, doTaskListData);
                    listView_dt.setAdapter(doTaskListAdapter);
                    setDoTaskListener();
                }
                Result=bundle.getString("untaskResult");
                if (Result!=null){
                    unTaskListData=Service_UnTask.getUnTaskMaps(Result);
                    //firstUTD=Service_UnTask.getFirstUnTaskId(Result);
                    lastUTD=1;//Service_UnTask.getLastUnTaskId(Result);
                    unTaskListAdapter=new UnTaskListAdapter(this, unTaskListData);
                    listView_ut.setAdapter(unTaskListAdapter);
                    setUnTaskListener();
                }
                Result=bundle.getString("setupResult");
                if (Result!=null){
                    if (Result.contains(RESULT_SUCCESS)) {
                        BaseHelp.ShowDialog(this, "测试成功!", 1);
                    }else {
                        BaseHelp.ShowDialog(this,"测试失败！",1);
                    }
                }
                Result=bundle.getString("epResult");
                if (Result!=null) {
                    entrustPrintAdapter = new EntrustPrintAdapter(this, Service_EntrustPrint.getEntrustPrintMaps(Result));
                    listView_ep.setAdapter(entrustPrintAdapter);
                }
                Result=bundle.getString("pepResult");
                if (Result!=null){
                    if (Result.contains(RESULT_FAIL)) {
                        BaseHelp.ShowDialog(this, "移交失败!", 1);
                    }
                    if (Result.contains(RESULT_SUCCESS)){
                        BaseHelp.ShowDialog(this,"移交成功！",1);
                    }
                }
                Result=bundle.getString("postUTResult");
                if (Result!=null){
                    if (Result.contains(RESULT_FAIL)) {
                        BaseHelp.ShowDialog(this, "提交打印失败，服务端未开启!", 1);
                    }
                    if (Result.contains(RESULT_SUCCESS)){
                        BaseHelp.ShowDialog(this,"提交打印成功，打印正在处理，请待会手动更新列表！",1);
                    }
                }
                Result=bundle.getString("updateResult");
                if (Result!=null){
                    if (Result.contains(RESULT_SUCCESS)) {
                        String version=bundle.getString("version");
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("新客户端更新")
                                .setMessage("检测到最新的版本号：V"+version)
                                .setPositiveButton("下载",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                strURL = Service_Common.GetDownPath(getParamaterApplication());
                                                // 通过地址下载文件
                                                downloadTheFile(strURL);
                                                // 显示更新状态，进度条
                                                showWaitDialog();
                                            }
                                        })
                                .setNegativeButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                    }else {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("新客户端更新")
                                .setMessage("当前为最新版本！")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                    }
                }
            }
        }
    }

    public void InitDoTask(){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, DOTASK);
        startActivityForResult(intent, 0);
    }

    public void InitUnTask(){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, UNTASK);
        startActivityForResult(intent, 0);
    }

    public void LoadEntrustPrint(){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, ENTRUSTPRINT);
        startActivityForResult(intent,0);
    }

    public void LoadTest(){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, INITSETUP);
        startActivityForResult(intent,0);
    }

    public void LoadUpdate(){
        Intent intent=new Intent();
        intent.setClass(this, LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, UPDATE);
        startActivityForResult(intent,0);
    }

    public void PostEntrustPrint(ArrayList<String> entrustPrintIds){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE, POSTEP);
        intent.putStringArrayListExtra("entrustPrintIds", entrustPrintIds);
        startActivityForResult(intent, 0);
    }

    public void PostUnTask(ArrayList<String> unTaskIds){
        Intent intent=new Intent();
        intent.setClass(this,LoadingActivity.class);
        intent.putExtra(LOADINGTYPE,POSTUT);
        intent.putStringArrayListExtra("unTaskIds",unTaskIds);
        startActivityForResult(intent,0);
    }

    public void UpdateUnTaskSelect(boolean isChecked){
        unTaskListAdapter.selectAll=isChecked;
        unTaskListAdapter.notifyDataSetChanged();
    }

    public void UpdateEnPrSelect(boolean isChecked){
        entrustPrintAdapter.selectAll=isChecked;
        entrustPrintAdapter.notifyDataSetChanged();
    }

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

    // 显示更新状态，进度条
    public void showWaitDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在更新，请稍候...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    // 截取文件名称并执行下载
    private void downloadTheFile(final String strPath) {
        // 获得文件文件扩展名字符串
        fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
                .toLowerCase();
        // 获得文件文件名字符串
        fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
                strURL.lastIndexOf("."));
        try {
            if (strPath.equals(currentFilePath)) {
                doDownloadTheFile(strPath);
            }
            currentFilePath = strPath;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        // 执行下载
                        doDownloadTheFile(strPath);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 执行新版本进行下载，并安装
    private void doDownloadTheFile(String strPath) throws Exception {
        Log.i(TAG, "getDataSource()");
        // 判断strPath是否为网络地址
        if (!URLUtil.isNetworkUrl(strPath)) {
            Log.i(TAG, "服务器地址错误！");
        } else {
            URL myURL = new URL(strPath);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is == null) {
                throw new RuntimeException("stream is null");
            }
            String savePAth = Environment.getExternalStorageDirectory()
                    + "/GrPrintUpdate";
            File file1 = new File(savePAth);
            if (!file1.exists()) {
                file1.mkdir();
            }
            String savePathString = Environment.getExternalStorageDirectory()
                    + "/GrPrintUpdate/" + fileNa + "." + fileEx;
            File file = new File(savePathString);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            currentTempFilePath = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
            }
            Log.i(TAG, "getDataSource() Download ok...");
            dialog.cancel();
            dialog.dismiss();
            // 打开文件
            openFile(file);
            try {
                fos.close();
                bis.close();
                is.close();
            } catch (Exception ex) {
                Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
            }
        }
    }

    // 打开文件进行安装
    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        // 获得下载好的文件类型
        String type = getMIMEType(f);
        // 打开各种类型文件
        intent.setDataAndType(Uri.fromFile(f), type);
        // 安装
        this.startActivity(intent);
    }

    // 删除临时路径里的安装包
    public void delFile() {
        Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");
        File myFile = new File(currentTempFilePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    // 获得下载文件的类型
    private String getMIMEType(File f) {
        String type = "";
        // 获得文件名称
        String fName = f.getName();
        // 获得文件扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    public void setDoTaskListener(){
        listView_dt.setOnRefreshListner(new BaseListView.OnRefreshListner() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, List<Map<String, Object>>>() {
                    @Override
                    protected List<Map<String, Object>> doInBackground(Void... params) {
                        List<Map<String, Object>> virtualData = new ArrayList<Map<String, Object>>();
                        try {
                            lastDTD=1;
                            virtualData=Service_DoTask.getDoTaskMaps(Service_DoTask.getDoTaskJson((ParameterApplication) getApplication(),"1"));
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return virtualData;
                    }
                    //更新UI的方法,系统自动实现
                    @Override
                    protected void onPostExecute (List<Map<String, Object>> result) {
                        doTaskListData.clear();
                        doTaskListData.addAll(0, result);//注意是往前添加数据
                        doTaskListAdapter.notifyDataSetChanged();
                        listView_dt.onRefreshComplete();//完成下拉刷新,这个方法要调用
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
        //创建FootView
        final View footer = View.inflate(HomeActivity.this, R.layout.footer, null);
        listView_dt.setOnAddFootListener(new BaseListView.OnAddFootListener() {
            @Override
            public void addFoot() {
                listView_dt.addFooterView(footer);
            }
        });

        listView_dt.setOnFootLoadingListener(new BaseListView.OnFootLoadingListener() {
            @Override
            public void onFootLoading() {
                new AsyncTask<Void, Void, List<Map<String, Object>>>() {
                    @Override
                    protected List<Map<String, Object>> doInBackground(Void... params) {
                        List<Map<String, Object>> virtualData = new ArrayList<Map<String, Object>>();
                        try {
                            lastDTD++;
                            virtualData=Service_DoTask.getDoTaskMaps(Service_DoTask.getDoTaskJson((ParameterApplication) getApplication(),String.valueOf(lastDTD)));
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return virtualData;
                    }

                    //在doInBackground后面执行
                    @Override
                    protected void onPostExecute(List<Map<String, Object>> result) {
                        doTaskListData.addAll(result);//这个是往后添加数据
                        doTaskListAdapter.notifyDataSetChanged();
                        listView_dt.onFootLoadingComplete();//完成上拉刷新,就是底部加载完毕,这个方法要调用
                        //移除footer,这个动作不能少
                        listView_dt.removeFooterView(footer);
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
    }

    public void setUnTaskListener(){
        listView_ut.setOnRefreshListner(new BaseListView.OnRefreshListner() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, List<Map<String, Object>>>() {
                    @Override
                    protected List<Map<String, Object>> doInBackground(Void... params) {
                        List<Map<String, Object>> virtualData = new ArrayList<Map<String, Object>>();
                        try {
                            lastUTD=1;
                            virtualData=Service_UnTask.getUnTaskMaps(Service_UnTask.getUnTaskJson((ParameterApplication) getApplication(),"1"));
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return virtualData;
                    }
                    //更新UI的方法,系统自动实现
                    @Override
                    protected void onPostExecute (List<Map<String, Object>> result) {
                        unTaskListData.clear();
                        unTaskListData.addAll(0, result);//注意是往前添加数据
                        unTaskListAdapter.notifyDataSetChanged();
                        listView_ut.onRefreshComplete();//完成下拉刷新,这个方法要调用
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
        //创建FootView
        final View footer = View.inflate(HomeActivity.this, R.layout.footer, null);
        listView_ut.setOnAddFootListener(new BaseListView.OnAddFootListener() {
            @Override
            public void addFoot() {
                listView_ut.addFooterView(footer);
            }
        });

        listView_ut.setOnFootLoadingListener(new BaseListView.OnFootLoadingListener() {
            @Override
            public void onFootLoading() {
                new AsyncTask<Void, Void, List<Map<String, Object>>>() {
                    @Override
                    protected List<Map<String, Object>> doInBackground(Void... params) {
                        List<Map<String, Object>> virtualData = new ArrayList<Map<String, Object>>();
                        try {
                            lastUTD++;
                            virtualData=Service_UnTask.getUnTaskMaps(Service_UnTask.getUnTaskJson((ParameterApplication) getApplication(),String.valueOf(lastUTD)));
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return virtualData;
                    }

                    //在doInBackground后面执行
                    @Override
                    protected void onPostExecute(List<Map<String, Object>> result) {
                        unTaskListData.addAll(result);//这个是往后添加数据
                        unTaskListAdapter.notifyDataSetChanged();
                        listView_ut.onFootLoadingComplete();//完成上拉刷新,就是底部加载完毕,这个方法要调用
                        //移除footer,这个动作不能少
                        listView_ut.removeFooterView(footer);
                        super.onPostExecute(result);
                    }
                }.execute();
            }
        });
    }

}
