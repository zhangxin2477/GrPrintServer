package com.printserver.base.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.printserver.base.BaseFragment;
import com.printserver.views.HomeActivity;
import com.printserver.views.ParameterApplication;
import com.printserver.views.R;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class LogoFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.logo_layout, null);
        HomeActivity homeActivity=(HomeActivity)getActivity();
        ParameterApplication parameterApplication=homeActivity.getParamaterApplication();
        ((TextView)view.findViewById(R.id.dep)).setText(parameterApplication.getUserInfo().getDeptName());
        ((TextView)view.findViewById(R.id.pos)).setText(parameterApplication.getUserInfo().getPosiName());
        ((TextView)view.findViewById(R.id.user)).setText(parameterApplication.getUserInfo().getUserName());
        ((TextView)view.findViewById(R.id.sec)).setText(parameterApplication.getUserInfo().getUserSec());
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
