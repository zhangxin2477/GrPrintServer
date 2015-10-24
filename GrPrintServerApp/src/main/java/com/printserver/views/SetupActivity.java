package com.printserver.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //view_main.setOnClickListener(this);
        setContentView(R.layout.initsetup_layout);
        BaseHelp.enterLightsOutMode(getWindow());
        back = (ButtonFlat) findViewById(R.id.init_setup_back);
        back.textButton.setTextColor(Color.parseColor("#FFFFFF"));
        back.setOnClickListener(this);

        localip = (EditText) findViewById(R.id.ip_txt);
        localip.setKeyListener(null);
        mask = (EditText) findViewById(R.id.mask_txt);
        mask.setKeyListener(null);
        gateway = (EditText) findViewById(R.id.gateway_txt);
        gateway.setKeyListener(null);
        dns = (EditText) findViewById(R.id.dns_txt);
        dns.setKeyListener(null);
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
            JSONObject jsonObject = BaseHelp.getEthernet();
            if (jsonObject.length() <= 0) {
                jsonObject = BaseHelp.getWirelessnet();
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
