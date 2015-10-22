package com.printserver.service;

import com.printserver.dao.User_Dao;
import com.printserver.model.UserModel;
import com.printserver.views.ParameterApplication;

import java.util.List;

/**
 * Created by zhangxin on 2015/9/11.
 */
public class Service_User {

    public static User_Dao user_dao;
    public static String getUserJson(ParameterApplication parameterApplication,String type){
        user_dao=new User_Dao();
        return user_dao.GetUserString(parameterApplication,type);
    }

    public static List<UserModel> UserToModelList(String jsonUser) {
        user_dao=new User_Dao();
        return user_dao.UserToModelList(jsonUser);
    }

    public static UserModel userLogin(String jsonUser){
        UserModel result=null;
        List<UserModel> userModelList=Service_User.UserToModelList(jsonUser);
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
