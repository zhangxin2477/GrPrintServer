package com.printserver.base.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.printserver.base.widgets.BaseListView;
import com.printserver.base.BaseFragment;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;

/**
 * Created by zhangxin on 2015/9/7.
 */
public class DoTaskFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private BaseListView listView;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.dotask_layout, null);
        listView = (BaseListView) view.findViewById(R.id.listView_DoTask);
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setListView_dt(listView);
        homeActivity.InitDoTask();
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
