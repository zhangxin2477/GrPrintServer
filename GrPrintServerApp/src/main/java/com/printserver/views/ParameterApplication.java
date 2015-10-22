package com.printserver.views;

import android.app.Application;

import com.printserver.base.BaseContext;
import com.printserver.model.UserModel;

/**
 * Created by zhangxin on 2015/9/9.
 */
public class ParameterApplication extends Application {

    private UserModel UserInfo;
    private String ConnectIP;
    private String ConnectPort;
    private String ConnectVrid;
    private String ListenIP;
    private String ListenPort;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseContext baseContext=new BaseContext(this);
        setConnectIP(baseContext.ReadDataValue("webip"));//初始化全局变量
        setConnectPort(baseContext.ReadDataValue("webport"));
        setConnectVrid(baseContext.ReadDataValue("webvrid"));
        setListenIP(baseContext.ReadDataValue("listenip"));
        setListenPort(baseContext.ReadDataValue("listenport"));
        setUserInfo(UserInfo);
    }

    public void setConnectIP(String value){
        this.ConnectIP=value;
    }

    public String getConnectIP(){
        return this.ConnectIP;
    }

    public void setConnectPort(String value){
        this.ConnectPort=value;
    }

    public String getConnectPort(){
        return this.ConnectPort;
    }

    public void setConnectVrid(String value){
        this.ConnectVrid =value;
    }

    public String getConnectVrid(){
        return this.ConnectVrid;
    }

    public UserModel getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        UserInfo = userInfo;
    }

    public String getListenIP() {
        return ListenIP;
    }

    public void setListenIP(String listenIP) {
        ListenIP = listenIP;
    }

    public String getListenPort() {
        return ListenPort;
    }

    public void setListenPort(String listenPort) {
        ListenPort = listenPort;
    }
}
