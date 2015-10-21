package com.printserver.dao;

import com.printserver.base.BaseHelp;
import com.printserver.model.UserModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/10.
 */
public class User_Dao {
    private String ip = "";
    private String port = "";
    private String vdir = "";
    private String account = "";
    private String password = "";

    public String GetUserString(ParameterApplication parameterApplication, String type) {
        account = parameterApplication.getUserInfo().getUserAccount();
        password = parameterApplication.getUserInfo().getUserPassword();
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("account", account);
        parameters.put("password", password);
        parameters.put("type", type);

        String user = null;
        try {
            user = BaseHelp.GetAllData(ip, port, vdir, "AndroidGetUserInfo", parameters);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    public List<UserModel> UserToModelList(String user) {
        List<UserModel> list = new ArrayList<UserModel>();
        try {
            JSONArray jsonArray = new JSONObject(user).getJSONArray("UserInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                UserModel userModel = new UserModel();
                userModel.setUserID(jsonObject.getString("id"));
                userModel.setUserName(jsonObject.getString("name"));
                userModel.setDeptID(jsonObject.getString("dept_id"));
                userModel.setDeptName(jsonObject.getString("dept_name"));
                userModel.setUserAccount(jsonObject.getString("account"));
                userModel.setUserPassword(jsonObject.getString("pwd"));
                userModel.setUserIsActive(jsonObject.getString("isactive"));
                userModel.setPosiID(jsonObject.getString("position_id"));
                userModel.setPosiName(jsonObject.getString("position_name"));
                userModel.setUserSec(jsonObject.getString("secgrade"));
                userModel.setMachineSec(jsonObject.getString("machinesec"));
                userModel.setUserIsLock(jsonObject.getString("islock"));
                list.add(userModel);
            }
        } catch (JSONException e) {
            list = null;
            e.printStackTrace();
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }
}
