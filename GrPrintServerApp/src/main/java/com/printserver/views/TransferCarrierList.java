package com.printserver.views;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.printserver.base.BaseHelp;
import com.printserver.base.BaseNfc;
import com.printserver.base.BaseParameters;
import com.printserver.base.adapter.TransferCarrierAdapter;
import com.printserver.service.Service_Carrier;

/**
 * Created by zhangxin on 2015/9/14.
 */
public class TransferCarrierList extends Activity implements View.OnClickListener,BaseParameters{

    private String carrierID;
    private EditText cardID;
    private ButtonFlat back;
    private ListView listView;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techList;
    private Intent intents;
    private boolean isnews = true;

    private ParameterApplication parameterApplication;
    private String carrierIDs;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.transfercarrier_list_layout);

        cardID =(EditText)findViewById(R.id.tracar_receiver);
        back=(ButtonFlat)findViewById(R.id.tc_back);
        back.textButton.setTextColor(Color.parseColor("#FFFFFF"));
        back.setOnClickListener(this);
        ButtonRectangle bt=(ButtonRectangle)findViewById(R.id.tc_ok);
        bt.setOnClickListener(this);

        Intent intent=this.getIntent();
        carrierID=intent.getStringExtra("carrierid").toString();

        listView=(ListView)findViewById(R.id.listView_TransferCarrier);
        parameterApplication=(ParameterApplication)getApplication();

        initNfc();
        Intent intentLoading=new Intent();
        intentLoading.setClass(this,LoadingActivity.class);
        intentLoading.putExtra(LOADINGTYPE, TRANSFERCARRIER);
        intentLoading.putExtra("carrierID",carrierID);
        startActivityForResult(intentLoading, 0);
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
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techList);
        if (isnews) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
                if (cardID != null) {
                    String tmp=BaseNfc.ScanNfc(this, getIntent());
                    if (tmp!=null){
                        cardID.setText(tmp);
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
            if (cardID !=null) {
                String tmp=BaseNfc.ScanNfc(this,intent);
                if (tmp!=null){
                    cardID.setText(tmp);
                }
            }
            intents = intent;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.tc_back:
                finish();
                break;
            case R.id.tc_ok:
                if (cardID.getText().toString().equals("")){
                    BaseHelp.ShowDialog(this,"接收人不能为空！",1);
                }else {
                    Intent intent=new Intent();
                    intent.setClass(TransferCarrierList.this,LoadingActivity.class);
                    intent.putExtra("","");
                    startActivityForResult(intent,0);
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String Result = bundle.getString("tcResult");
                    if (Result != null) {
                        listView.setAdapter(new TransferCarrierAdapter(this, Service_Carrier.getCarrierMaps(Result)));
                    }
            }
        }
    }
}
