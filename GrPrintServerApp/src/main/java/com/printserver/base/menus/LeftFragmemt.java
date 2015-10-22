package com.printserver.base.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.printserver.base.BaseFragment;
import com.printserver.base.framents.DoTaskFragment;
import com.printserver.base.framents.EntrustPrintFragment;
import com.printserver.base.framents.TransferCarrierFragment;
import com.printserver.views.HomeActivity;
import com.printserver.base.framents.LogoFragment;
import com.printserver.views.LoginActivity;
import com.printserver.views.ParameterApplication;
import com.printserver.views.R;
import com.printserver.base.framents.UnTaskFragmemt;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class LeftFragmemt extends Fragment implements View.OnClickListener {

    private HomeActivity homeActivity;
    private ParameterApplication parameterApplication;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_layout, null);
        homeActivity=(HomeActivity)getActivity();
        parameterApplication=homeActivity.getParamaterApplication();
        TextView user=(TextView)view.findViewById(R.id.username_textview);
        TextView sec=(TextView)view.findViewById(R.id.usersec_textview);
        user.setText(parameterApplication.getUserInfo().getUserName());
        sec.setText(parameterApplication.getUserInfo().getUserSec());
        view.findViewById(R.id.my_untask_textview).setOnClickListener(this);
        view.findViewById(R.id.my_dotask_textview).setOnClickListener(this);
        view.findViewById(R.id.my_tracar_textview).setOnClickListener(this);
        view.findViewById(R.id.my_entprt_textview).setOnClickListener(this);
        view.findViewById(R.id.my_cancel_textview).setOnClickListener(this);
        view.findViewById(R.id.user_re).setOnClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        BaseFragment fragment=null;
        switch (v.getId()){
            case R.id.my_untask_textview:
                fragment=new UnTaskFragmemt();
                fragment.title="待打印任务";
                break;
            case R.id.my_dotask_textview:
                fragment=new DoTaskFragment();
                fragment.title="已打印任务";
                break;
            case R.id.my_tracar_textview:
                fragment=new TransferCarrierFragment();
                fragment.title="载体自助移交";
                break;
            case R.id.my_entprt_textview:
                fragment=new EntrustPrintFragment();
                fragment.title="委托打印任务";
                break;
            case R.id.my_cancel_textview:
                Intent intent=new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.user_re:
                fragment=new LogoFragment();
                fragment.title="安全打印监控与审计系统";
                break;
            default:
                fragment=new LogoFragment();
                fragment.title="安全打印监控与审计系统";
                break;
        }
        if (fragment!=null) {
            homeActivity.switchFrameActivity(fragment);
            homeActivity.showLeft();
            fragment=null;
        }
    }
}
