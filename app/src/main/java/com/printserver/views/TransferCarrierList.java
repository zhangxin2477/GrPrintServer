package com.printserver.views;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
    private ImageView back;
    private ListView listView;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techList;
    private Intent intents;
    private boolean isnews = true;

    private ParameterApplication parameterApplication;
    private String carrierIDs;
    private View view_main;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        view_main=getLayoutInflater().from(this).inflate(R.layout.transfercarrier_list_layout,null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //view_main.setOnClickListener(this);
        setContentView(view_main);

        cardID =(EditText)findViewById(R.id.tracar_receiver);
        back=(ImageView)findViewById(R.id.tc_back);
        back.setOnClickListener(this);
        Button bt=(Button)findViewById(R.id.tc_ok);
        bt.setOnClickListener(this);

        Intent intent=this.getIntent();
        carrierID=intent.getStringExtra("carrierid").toString();

        listView=(ListView)findViewById(R.id.listView_TransferCarrier);
        parameterApplication=(ParameterApplication)getApplication();

        initNfc();
        //new Thread(runnable).start();
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
        view_main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
                    //String tc_result=Service_Carrier.GetTransferCarrierResult(parameterApplication, cardID.getText().toString(), carrierIDs);
                    //bundle.putString("tc_result", Service_Carrier.GetTransferCarrierResult(parameterApplication,cardID.getText().toString(),carrierIDs));
                    //Toast.makeText(TransferCarrierList.this, tc_result, Toast.LENGTH_LONG).show();
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

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Message msg = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putString("carrier", Service_Carrier.GetCarrierString(parameterApplication,carrierID,"1"));
//            msg.setData(bundle);
//            handler.sendMessage(msg);
//        }
//    };
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            Bundle bundle = msg.getData();
//            String result = bundle.getString("carrier");
//            if (!result.equals("")) {
//                carrierIDs = Service_Carrier.GetCarrierIDs(result);
//                carrierIDs = carrierIDs.substring(0, carrierIDs.length() - 1);
//                Toast.makeText(TransferCarrierList.this, carrierIDs, Toast.LENGTH_LONG).show();
//                listView.setAdapter(new TransferCarrierAdapter(TransferCarrierList.this, Service_Carrier.getCarrierMaps(result)));
//            }
//        }
//    };
//
//    private Runnable runnable2=new Runnable() {
//        @Override
//        public void run() {
//            //Message msg=new Message();
//            //Bundle bundle=new Bundle();
//            String tc_result=Service_Carrier.GetTransferCarrierResult(parameterApplication, cardID.getText().toString(), carrierIDs);
//            //bundle.putString("tc_result", Service_Carrier.GetTransferCarrierResult(parameterApplication,cardID.getText().toString(),carrierIDs));
//            Toast.makeText(TransferCarrierList.this, tc_result, Toast.LENGTH_LONG).show();
//            //BaseHelp.ShowDialog(TransferCarrierList.this, tc_result, 1);
//            //msg.setData(bundle);
//            //handler2.handleMessage(msg);
//        }
//    };
//
//    private Handler handler2=new Handler(){
//        public void handleMessage(Message msg){
//            Bundle bundle = msg.getData();
//            String tc_result = bundle.getString("tc_result");
//            BaseHelp.ShowDialog(TransferCarrierList.this,tc_result,1);
//        }
//    };
}
