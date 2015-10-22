package com.printserver.base.framents;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.printserver.base.BaseHelp;
import com.printserver.base.BaseFragment;
import com.printserver.base.zxing.CaptureActivity;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;
import com.printserver.views.TransferCarrierList;

/**
 * Created by zhangxin on 2015/9/3.
 */
public class TransferCarrierFragment extends BaseFragment implements View.OnClickListener {

    private EditText carrierID;
    private Button scan;
    private Button search;
    private HomeActivity homeActivity;

    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.transfercarrier_layout, null);
        homeActivity=(HomeActivity)getActivity();
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carrierID=(EditText)view.findViewById(R.id.carrier_id);
        homeActivity.setCarrierID(carrierID);
        scan=(Button)view.findViewById(R.id.button_scan);
        search=(Button)view.findViewById(R.id.button_search);
        scan.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_scan:
                Intent intent = new Intent(homeActivity, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case  R.id.button_search:
                if (carrierID.getText().toString().equals("")){
                    BaseHelp.ShowDialog(homeActivity,"载体编号为空，请先扫描！",1);
                    break;
                }
                Intent intent1=new Intent(homeActivity, TransferCarrierList.class);
                Bundle bundle=new Bundle();
                bundle.putString("carrierid",carrierID.getText().toString());
                intent1.putExtras(bundle);
                startActivityForResult(intent1,0);
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(homeActivity,"点下",Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                Toast.makeText(homeActivity,"移动",Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_UP:
                Toast.makeText(homeActivity,"离开",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
