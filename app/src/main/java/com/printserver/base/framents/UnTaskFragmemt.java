package com.printserver.base.framents;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.printserver.base.BaseFragment;
import com.printserver.base.BaseHelp;
import com.printserver.base.widgets.BaseListView;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by zhangxin on 2015/9/6.
 */
public class UnTaskFragmemt extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
;
    private HomeActivity homeActivity;
    private BaseListView listView;

    protected View initView(LayoutInflater inflater) {
        View view=inflater.inflate(R.layout.untask_layout, null);
        listView = (BaseListView)view.findViewById(R.id.listView_UnTask);
        Button btn=(Button)view.findViewById(R.id.button_print_all);
        btn.setOnClickListener(this);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.headuntask_checkbox);
        checkBox.setOnCheckedChangeListener(this);
        homeActivity=(HomeActivity)getActivity();
        homeActivity.setListView_ut(listView);
        homeActivity.InitUnTask();
        listView.setOnItemClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_print_all:
                ArrayList<String> unTaskIds=homeActivity.unTaskListAdapter.untaskIds;
                if (unTaskIds!=null){
                    if (unTaskIds.size()==0){
                        BaseHelp.ShowDialog(homeActivity,"请选择待打印文件！",1);
                    }else {
                        homeActivity.PostUnTask(unTaskIds);
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.task_checkbox);
        if (checkBox.isChecked()){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        homeActivity.UpdateUnTaskSelect(isChecked);
    }
}
