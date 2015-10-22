package com.printserver.base.menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.printserver.base.BaseFragment;
import com.printserver.base.framents.AboutFragment;
import com.printserver.base.framents.SetupFragment;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class RightFrament extends Fragment implements View.OnClickListener {

    private HomeActivity homeActivity;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_layout, null);
        homeActivity=(HomeActivity)getActivity();
        view.findViewById(R.id.my_setup_textview).setOnClickListener(this);
        view.findViewById(R.id.my_update_textview).setOnClickListener(this);
        view.findViewById(R.id.my_about_textview).setOnClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        BaseFragment fragment = null;
        switch (v.getId()) {
            case R.id.my_setup_textview:
                fragment=new SetupFragment();
                fragment.title="设置";
                break;
            case R.id.my_about_textview:
                fragment=new AboutFragment();
                fragment.title="关于";
                break;
            case R.id.my_update_textview:
                homeActivity.LoadUpdate();
                break;
            default:
                break;
        }
        homeActivity.switchFrameActivity(fragment);
        homeActivity.showRight();
        fragment = null;
    }
}
