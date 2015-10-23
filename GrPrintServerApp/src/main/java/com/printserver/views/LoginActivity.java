package com.printserver.views;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.ButtonRectangle;
import com.printserver.base.BaseActivity;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseNfc;
import com.printserver.base.BaseParameters;


/**
 * Created by zhangxin on 2015/9/2.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,BaseParameters {

    private EditText userAccount;
    private EditText userPassword;
    private ButtonRectangle login_btn;
    private Switch login_type;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techList;
    private Intent intents;
    private boolean isnews = true;

    private ParameterApplication parameterApplication;
    private View view_main;
    private boolean login_card=true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_main=getLayoutInflater().from(this).inflate(R.layout.login_layout,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //view_main.setOnClickListener(this);
        setContentView(view_main);

        BaseHelp.enterLightsOutMode(getWindow());
        parameterApplication=(ParameterApplication)getApplication();

        userAccount=(EditText)findViewById(R.id.login_user);
        userPassword=(EditText)findViewById(R.id.login_pw);
        login_type=(Switch)findViewById(R.id.type_open);
        login_btn=(ButtonRectangle)findViewById(R.id.login_ok_bt);
        login_btn.setOnClickListener(this);
        login_type.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean check) {
                if(check){
                    login_card=true;
                }else {
                    login_card=false;
                }
            }
        });

        initNfc();
    }

    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            BaseHelp.ShowDialog(this, "您的设备不支持NFC，是否退出！", 0);
            //return;
        }
        if (!nfcAdapter.isEnabled()) {
            BaseHelp.ShowDialog(this, "您的设备没有打开NFC功能！", 1);
            //return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        intentFilter.addCategory("*//*");
        intentFilters = new IntentFilter[] { intentFilter };// 过滤器
        techList = new String[][] {
                new String[] { MifareClassic.class.getName() },
                new String[] { NfcA.class.getName() }};// 允许扫描的标签类型
    }

    @Override
    public void onClick(View v) {
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        switch (v.getId()){
            case R.id.login_ok_bt:
                if (login_card) {
                    BaseHelp.ShowDialog(this,"请刷卡登录！",1);
                    return;
                }
                Logining();
                //new Thread(runnable).start();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String loginResult = bundle.getString("loginResult");
                    if (loginResult != null) {
                        if (loginResult.contains(RESULT_FAIL)) {
                            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            LoginActivity.this.CloseActivity();
                        }
                    }
                    break;
            }
        }else {
            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techList);
        if (isnews) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
                if (userAccount != null) {
                    String tmp=BaseNfc.ScanNfc(this, getIntent());
                    if (tmp!=null){
                        userAccount.setText(tmp);
                        Logining();
                    }
                }
                intents = getIntent();
                isnews = false;
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            if (userAccount !=null) {
                String tmp=BaseNfc.ScanNfc(this,intent);
                if(tmp!=null){
                    userAccount.setText(tmp);
                    Logining();
                }
            }
            intents = intent;
        }
    }

    public void Logining(){
        if(!login_card) {
            Intent intentLoading = new Intent();
            intentLoading.setClass(LoginActivity.this, LoadingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LOADINGTYPE, LOGIN);
            bundle.putString("userAccount", userAccount.getText().toString());
            bundle.putString("userPassword", userPassword.getText().toString());
            bundle.putString("loginType","0");
            intentLoading.putExtras(bundle);
            startActivityForResult(intentLoading, 0);
        }else {
            Intent intentLoading = new Intent();
            intentLoading.setClass(LoginActivity.this, LoadingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LOADINGTYPE, LOGIN);
            bundle.putString("userAccount", userAccount.getText().toString());
            bundle.putString("userPassword", "");
            bundle.putString("loginType","1");
            intentLoading.putExtras(bundle);
            startActivityForResult(intentLoading, 0);
        }
    }
}
