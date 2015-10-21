package com.printserver.base.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.printserver.base.BaseFragment;
import com.printserver.views.HomeActivity;
import com.printserver.views.R;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class AboutFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.about_layout, null);
        HomeActivity homeActivity=(HomeActivity)getActivity();
        TextView textView=(TextView)view.findViewById(R.id.version);
        textView.setText("V "+homeActivity.getVersion());
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
