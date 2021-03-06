package com.printserver.service;

import com.printserver.dao.User_Dao;
import com.printserver.model.UserModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangxin on 2015/9/11.
 */
public class Service_User {

    public static User_Dao user_dao;

    public static List<UserModel> UserToModelList(JSONObject jsonUser) {
        user_dao=new User_Dao();
        return user_dao.UserToModelList(jsonUser);
    }

    public static UserModel userLogin(List<UserModel> userModelList){
        UserModel result=null;
        if (userModelList==null||userModelList.size()==0){
            result=null;
        }else {
            result=userModelList.get(0);
            if (result.getUserIsActive().equals("0")){
                result=null;
            }
            else if (result.getUserIsLock().equals("0")){
                result=null;
            }else {
                result=userModelList.get(0);
            }
        }
        return result;
    }
}
