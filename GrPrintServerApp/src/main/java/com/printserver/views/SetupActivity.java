package com.printserver.views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.printserver.base.BaseContext;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseParameters;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2015/9/11.
 */
public class SetupActivity extends Activity implements View.OnClickListener, BaseParameters {

    private EditText localip, mask, gateway, dns, webip, webport, webvrid, listenip, listenport;
    private ButtonRectangle test_setup, apply_setup;
    private ButtonFlat back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.initsetup_layout);

        back = (ButtonFlat) findViewById(R.id.init_setup_back);
        back.textButton.setTextColor(Color.parseColor("#FFFFFF"));
        back.setOnClickListener(this);

        localip = (EditText) findViewById(R.id.ip_txt);
        localip.setKeyListener(null);
        localip.setOnClickListener(this);
        mask = (EditText) findViewById(R.id.mask_txt);
        mask.setKeyListener(null);
        mask.setOnClickListener(this);
        gateway = (EditText) findViewById(R.id.gateway_txt);
        gateway.setKeyListener(null);
        gateway.setOnClickListener(this);
        dns = (EditText) findViewById(R.id.dns_txt);
        dns.setKeyListener(null);
        dns.setOnClickListener(this);
        webip = (EditText) findViewById(R.id.webip_txt);
        webport = (EditText) findViewById(R.id.webport_txt);
        webvrid = (EditText) findViewById(R.id.vd_txt);
        listenip = (EditText) findViewById(R.id.listenip_txt);
        listenport = (EditText) findViewById(R.id.listenport_txt);

        test_setup = (ButtonRectangle) findViewById(R.id.setup_test);
        apply_setup = (ButtonRectangle) findViewById(R.id.setup_ok);
        test_setup.setOnClickListener(this);
        apply_setup.setOnClickListener(this);

        initSetup();
    }

    private void initSetup() {
        BaseContext baseContext = new BaseContext(this);
        try {
            JSONObject jsonObject = BaseHelp.GetEthernet();
            if (jsonObject.length() <= 0) {
                jsonObject = BaseHelp.GetWirelessnet();
            }
            localip.setText(jsonObject.get("IPAddress").toString());
            mask.setText(jsonObject.get("Mask").toString());
            gateway.setText(jsonObject.get("Gateway").toString());
            dns.setText(jsonObject.get("Dns").toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        webip.setText(baseContext.ReadDataValue("webip"));
        webport.setText(baseContext.ReadDataValue("webport"));
        webvrid.setText(baseContext.ReadDataValue("webvrid"));
        listenip.setText(baseContext.ReadDataValue("listenip"));
        listenport.setText(baseContext.ReadDataValue("listenport"));
    }

    private boolean updateSetup() {
        BaseContext baseContext = new BaseContext(this);
        List<String[]> setupList = new ArrayList<>();
        setupList.add(new String[]{"localip", localip.getText().toString()});
        setupList.add(new String[]{"mask", mask.getText().toString()});
        setupList.add(new String[]{"gateway", gateway.getText().toString()});
        setupList.add(new String[]{"dns", dns.getText().toString()});
        setupList.add(new String[]{"webip", webip.getText().toString()});
        setupList.add(new String[]{"webport", webport.getText().toString()});
        setupList.add(new String[]{"webvrid", webvrid.getText().toString()});
        setupList.add(new String[]{"listenip", listenip.getText().toString()});
        setupList.add(new String[]{"listenport", listenport.getText().toString()});
        return baseContext.WriteDataList(setupList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init_setup_back:
                SetupActivity.this.setResult(RESULT_OK, null);
                SetupActivity.this.finish();
                SetupActivity.this.onDestroy();
                break;
            case R.id.setup_ok:
                if (webip.getText().toString().equals("")
                        || webport.getText().toString().equals("")
                        || webvrid.getText().toString().equals("")
                        || listenip.getText().toString().equals("")
                        || listenport.getText().toString().equals("")) {
                    BaseHelp.ShowDialog(this, "您的配置信息不能为空！", 1);
                    return;
                }
                if (updateSetup()) {
                    ((ParameterApplication)getApplication()).updata();
                    Toast.makeText(this, "保存成功！", Toast.LENGTH_LONG).show();
                    SetupActivity.this.setResult(RESULT_OK, null);
                    SetupActivity.this.finish();
                    SetupActivity.this.onDestroy();
                } else {
                    Toast.makeText(this, "保存失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.setup_test:
                updateSetup();
                Intent intentLoading = new Intent();
                intentLoading.setClass(SetupActivity.this, LoadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("LoadingType", INITSETUP);
                intentLoading.putExtras(bundle);
                startActivityForResult(intentLoading, 0);
                break;
            case  R.id.ip_txt:
                Intent intent1 = new Intent();
                ComponentName componentName1 = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent1.setComponent(componentName1);
                intent1.setAction("android.intent.action.VIEW");
                startActivity(intent1);
                break;
            case R.id.mask_txt:
                Intent intent2 = new Intent();
                ComponentName componentName2 = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent2.setComponent(componentName2);
                intent2.setAction("android.intent.action.VIEW");
                startActivity(intent2);
                break;
            case R.id.dns_txt:
                Intent intent3 = new Intent();
                ComponentName componentName3 = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent3.setComponent(componentName3);
                intent3.setAction("android.intent.action.VIEW");
                startActivity(intent3);
                break;
            case R.id.gateway_txt:
                Intent intent4 = new Intent();
                ComponentName componentName4 = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent4.setComponent(componentName4);
                intent4.setAction("android.intent.action.VIEW");
                startActivity(intent4);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String setupResult = bundle.getString(RESULT_SETUP);
                    if (setupResult != null) {
                        if (setupResult.contains(RESULT_FAIL)) {
                            BaseHelp.ShowDialog(this, "测试失败！", 1);
                        } else {
                            BaseHelp.ShowDialog(this, "测试成功!", 1);
                        }
                    }
                    break;
            }
        } else {
            BaseHelp.ShowDialog(this, "测试失败！", 1);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SetupActivity.this.setResult(RESULT_OK, null);
            SetupActivity.this.finish();
            SetupActivity.this.onDestroy();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
