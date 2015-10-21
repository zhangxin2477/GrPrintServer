package com.printserver.service;

import android.support.annotation.NonNull;

import com.printserver.dao.EntrustPrint_Dao;
import com.printserver.model.EntrustPrintModel;
import com.printserver.model.PrintInfoModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangxin on 2015/9/17.
 */
public class Service_EntrustPrint {
    private static EntrustPrint_Dao entrustPrint_dao;

    public static String getEntrustPrintString(ParameterApplication parameterApplication){
        entrustPrint_dao=new EntrustPrint_Dao();
        return entrustPrint_dao.GetEntrustPrintString(parameterApplication);
    }

    public static List<EntrustPrintModel> entrustPrintToModel(String jsonEP) {
        entrustPrint_dao=new EntrustPrint_Dao();
        return entrustPrint_dao.EntrustPrintToModel(jsonEP);
    }

    public static List<Map<String, Object>> getEntrustPrintMaps(String jsonEP) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<EntrustPrintModel> entrustPrintModelList = Service_EntrustPrint.entrustPrintToModel(jsonEP);
        if(entrustPrintModelList!=null){
            for (int i = 0; i < entrustPrintModelList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("EntrustPrint_ck", entrustPrintModelList.get(i).getEntrustPrintID());
                map.put("EntrustPrint_id", Integer.toString(i + 1));
                map.put("EntrustPrint_time", entrustPrintModelList.get(i).getEntrustPrintOpTime());
                map.put("EntrustPrint_filename",entrustPrintModelList.get(i).getEntrustPrintBussContent());
                map.put("EntrustPrint_state",entrustPrintModelList.get(i).getEntrustPrintOpState());
                map.put("EntrustPrint_clint",entrustPrintModelList.get(i).getEntrustPrintHUserName());
                list.add(map);
            }}
        return list;
    }

    public static String getPostEPResult(ParameterApplication parameterApplication,ArrayList<String> epIdList){
        String result="no";
        try {
            entrustPrint_dao = new EntrustPrint_Dao();
            String epIds = "0";
            if (epIdList != null) {
                if (epIdList.size() > 0) {
                    for (String tmp : epIdList) {
                        epIds = epIds + ";" + tmp;
                    }
                }
            }
            result = entrustPrint_dao.GetPostEPResult(parameterApplication, epIds);
            result = new JSONObject(result).getString("result");
        }catch (Exception e){
            result="no:"+e.getMessage();
        }
        return result;
    }
}
