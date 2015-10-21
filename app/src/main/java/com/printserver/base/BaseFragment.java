package com.printserver.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.printserver.views.HomeActivity;
import com.printserver.views.R;

/**
 * Created by zhangxin on 2015/9/7.
 */
public abstract class BaseFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ImageView leftBt;
    private ImageView rightBt;
    public TextView centerTitle;
    public String title="安全打印监控与审计系统";
    public View rootView;

    protected abstract View initView(LayoutInflater inflater);

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = initView(inflater);
        leftBt = (ImageView) rootView.findViewById(R.id.showLeft);
        rightBt = (ImageView) rootView.findViewById(R.id.showRight);
        centerTitle=(TextView)rootView.findViewById(R.id.title_name_main);
        centerTitle.setText(title);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).showLeft();
            }
        });
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).showRight();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (myPageChangeListener != null)
            myPageChangeListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private MyPageChangeListener myPageChangeListener;

    public void setMyPageChangeListener(MyPageChangeListener l) {
        myPageChangeListener = l;
    }

    public interface MyPageChangeListener {
        public void onPageSelected(int position);
    }

    public void initUpdateUI(final String title) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bundle bundle = message.getData();
                String title = bundle.getString("title");
                centerTitle.setText(title);

            }
        };
        Thread updateUI = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                message.setData(bundle);
                handler.handleMessage(message);
            }
        });
        updateUI.start();
    }
}
