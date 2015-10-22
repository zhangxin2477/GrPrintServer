package com.printserver.base;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/9.
 */
public class BaseContext extends ContextWrapper {

    private String dataFile="com.zx.grprintserver";
    public BaseContext(Context base) {
        super(base);
    }

    public boolean WriteDataList(List<String[]> dataList){
        boolean result=false;
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(dataFile, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String[] object:dataList) {
                editor.putString(object[0].toString(),object[1].toString());
            }
            editor.commit();
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean WriteData(String key,String value){
        boolean result=false;
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(dataFile, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public String ReadDataValue(String key){
        String value="";
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(dataFile, Context.MODE_PRIVATE);
            value = sharedPreferences.getString(key,"");
        }catch (Exception ex){
            ex.printStackTrace();
            value = "";
        }
        return value;
    }
}
