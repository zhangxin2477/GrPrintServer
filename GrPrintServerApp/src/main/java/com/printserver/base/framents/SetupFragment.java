package com.printserver.base.framents;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.printserver.base.BaseContext;
import com.printserver.base.BaseFragment;
import com.printserver.base.BaseHelp;
import com.printserver.views.HomeActivity;
import com.printserver.views.ParameterApplication;
import com.printserver.views.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2015/9/2.
 */
public class SetupFragment extends BaseFragment implements OnClickListener {

    private HomeActivity homeActivity;
    private EditText localip, mask, gateway, dns, webip, webport, webvrid, listenip, listenport;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setup_ok:
                if (webip.getText().toString().equals("")
                        || webport.getText().toString().equals("")
                        || webvrid.getText().toString().equals("")
                        || listenip.getText().toString().equals("")
                        || listenport.getText().toString().equals("")) {
                    BaseHelp.ShowDialog(homeActivity, "您的配置信息不能为空！", 1);
                    return;
                }
                if (updateSetup()) {
                    ((ParameterApplication) homeActivity.getApplication()).updata();
                    Toast.makeText(homeActivity, "保存成功！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(homeActivity, "保存失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.setup_test:
                updateSetup();
                homeActivity.LoadTest();
                break;
            case R.id.ip_txt:
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
            default:
                break;
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.setup_layout, null);
        localip = (EditText) view.findViewById(R.id.ip_txt);
        localip.setKeyListener(null);
        localip.setOnClickListener(this);
        mask = (EditText) view.findViewById(R.id.mask_txt);
        mask.setKeyListener(null);
        mask.setOnClickListener(this);
        gateway = (EditText) view.findViewById(R.id.gateway_txt);
        gateway.setKeyListener(null);
        gateway.setOnClickListener(this);
        dns = (EditText) view.findViewById(R.id.dns_txt);
        dns.setKeyListener(null);
        dns.setOnClickListener(this);
        webip = (EditText) view.findViewById(R.id.webip_txt);
        webport = (EditText) view.findViewById(R.id.webport_txt);
        webvrid = (EditText) view.findViewById(R.id.vd_txt);
        listenip = (EditText) view.findViewById(R.id.listenip_txt);
        listenport = (EditText) view.findViewById(R.id.listenport_txt);

        view.findViewById(R.id.setup_ok).setOnClickListener(this);
        view.findViewById(R.id.setup_test).setOnClickListener(this);
        homeActivity = (HomeActivity) getActivity();
        initSetup();
        return view;
    }

    private void initSetup() {
        BaseContext baseContext = new BaseContext(homeActivity);
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
        BaseContext baseContext = new BaseContext(homeActivity);
        List<String[]> setupList = new ArrayList<String[]>();
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
}
