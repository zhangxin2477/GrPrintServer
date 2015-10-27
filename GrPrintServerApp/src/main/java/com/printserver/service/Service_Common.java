package com.printserver.service;

import com.printserver.base.BaseHelp;
import com.printserver.dao.Connect_Dao;
import com.printserver.views.ParameterApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangxin on 2015/9/10.
 */
public class Service_Common {
    private static Connect_Dao connect_dao;

    public static String GetVersion(ParameterApplication parameterApplication){
        String result= BaseHelp.GetHtml(parameterApplication.getConnectIP(),parameterApplication.getConnectPort(),parameterApplication.getConnectVrid());
        result=result.substring(result.indexOf("{"),result.lastIndexOf("}")+1);
        try {
            result=new JSONObject(result).getString("version");
        } catch (JSONException e) {
            e.printStackTrace();
            result=null;
        }
        return result;
    }

    public  static String GetDownPath(ParameterApplication parameterApplication){
        return BaseHelp.GetDownUrl(parameterApplication.getConnectIP(),parameterApplication.getConnectPort(),parameterApplication.getConnectVrid());
    }
}
