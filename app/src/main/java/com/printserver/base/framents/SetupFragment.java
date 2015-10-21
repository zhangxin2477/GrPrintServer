package com.printserver.base.framents;

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
    private ParameterApplication parameterApplication;
    private EditText localip,mask,gateway,dns,webip,webport, webvrid, listenip, listenport;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setup_ok:
                if(webip.getText().toString().equals("")
                        ||webport.getText().toString().equals("")
                        ||webvrid.getText().toString().equals("")
                        ||listenip.getText().toString().equals("")
                        ||listenport.getText().toString().equals("")){
                    BaseHelp.ShowDialog(homeActivity,"您的配置信息不能为空！",1);
                    return;
                }
                if(updateSetup()) {
                    Toast.makeText(homeActivity, "保存成功！", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(homeActivity, "保存失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.setup_test:
                updateSetup();
                homeActivity.LoadTest();
                break;
            default:
                break;
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view=inflater.inflate(R.layout.setup_layout,null);
        localip=(EditText)view.findViewById(R.id.ip_txt);localip.setKeyListener(null);
        mask=(EditText)view.findViewById(R.id.mask_txt);mask.setKeyListener(null);
        gateway=(EditText)view.findViewById(R.id.gateway_txt);gateway.setKeyListener(null);
        dns=(EditText)view.findViewById(R.id.dns_txt);dns.setKeyListener(null);
        webip=(EditText)view.findViewById(R.id.webip_txt);
        webport=(EditText)view.findViewById(R.id.webport_txt);
        webvrid =(EditText)view.findViewById(R.id.vd_txt);
        listenip =(EditText)view.findViewById(R.id.listenip_txt);
        listenport =(EditText)view.findViewById(R.id.listenport_txt);

        view.findViewById(R.id.setup_ok).setOnClickListener(this);
        view.findViewById(R.id.setup_test).setOnClickListener(this);
        homeActivity=(HomeActivity)getActivity();
        initSetup();
        return view;
    }

    private void initSetup(){
        BaseContext baseContext=new BaseContext(homeActivity);
        try {
            JSONObject jsonObject = BaseHelp.getEthernet();
            if (jsonObject.length() <= 0) {
                jsonObject = BaseHelp.getWirelessnet();
            }
            localip.setText(jsonObject.get("IPAddress").toString());
            mask.setText(jsonObject.get("Mask").toString());
            gateway.setText(jsonObject.get("Gateway").toString());
            dns.setText(jsonObject.get("Dns").toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        webip.setText(baseContext.ReadDataValue("webip"));
        webport.setText(baseContext.ReadDataValue("webport"));
        webvrid.setText(baseContext.ReadDataValue("webvrid"));
        listenip.setText(baseContext.ReadDataValue("listenip"));
        listenport.setText(baseContext.ReadDataValue("listenport"));
    }

    private boolean updateSetup(){
        BaseContext baseContext=new BaseContext(homeActivity);
        List<String[]> setupList=new ArrayList<String[]>();
        setupList.add(new String[]{"localip",localip.getText().toString()});
        setupList.add(new String[]{"mask",mask.getText().toString()});
        setupList.add(new String[]{"gateway",gateway.getText().toString()});
        setupList.add(new String[]{"dns", dns.getText().toString()});
        setupList.add(new String[]{"webip", webip.getText().toString()});
        setupList.add(new String[]{"webport", webport.getText().toString()});
        setupList.add(new String[]{"webvrid", webvrid.getText().toString()});
        setupList.add(new String[]{"listenip", listenip.getText().toString()});
        setupList.add(new String[]{"listenport", listenport.getText().toString()});
        return baseContext.WriteDataList(setupList);
    }
}
