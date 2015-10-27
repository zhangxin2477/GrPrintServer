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

    public List<UserModel> UserToModelList(JSONObject user) {
        List<UserModel> list = new ArrayList<UserModel>();
        try {
            JSONArray jsonArray = user.getJSONArray("UserInfo");
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
                userModel.setCardInfo(jsonObject.getString("cardID"));
                userModel.setCardType(jsonObject.getString("type"));
                userModel.setCardState(jsonObject.getString("state"));
                userModel.setCardUseAuthor(jsonObject.getString("useScope"));
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
