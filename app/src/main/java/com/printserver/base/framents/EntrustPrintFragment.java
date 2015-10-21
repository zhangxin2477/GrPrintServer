package com.printserver.base.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.printserver.base.BaseHelp;
import com.printserver.base.BaseFragment;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;

import java.util.ArrayList;

/**
 * Created by zhangxin on 2015/9/3.
 */
public class EntrustPrintFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private HomeActivity homeActivity;
    private ListView listView;

    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.entrustprint_layout, null);
        listView=(ListView)view.findViewById(R.id.listView_EntrustPrint);
        Button button=(Button)view.findViewById(R.id.entrustPrint_bt_ok);
        button.setOnClickListener(this);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.headentpri_choice);
        checkBox.setOnCheckedChangeListener(this);
        homeActivity=(HomeActivity)getActivity();
        homeActivity.setListView_ep(listView);
        homeActivity.LoadEntrustPrint();
        listView.setOnItemClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.entrustPrint_bt_ok:
                ArrayList<String> entrustPrintIds = homeActivity.entrustPrintAdapter.entrustPrintIds;
                if (entrustPrintIds != null) {
                    if (entrustPrintIds.size() == 0) {
                        BaseHelp.ShowDialog(homeActivity, "请选择确认移交的打印信息！", 1);
                    }else {
                        homeActivity.PostEntrustPrint(entrustPrintIds);
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.entpri_choice);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }
    }

    public boolean onTouch(MotionEvent event) {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        homeActivity.UpdateEnPrSelect(isChecked);
    }
}
