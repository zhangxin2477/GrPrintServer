package com.printserver.service;

import com.printserver.dao.DoTask_Dao;
import com.printserver.model.PrintInfoModel;
import com.printserver.views.ParameterApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/10.
 */
public class Service_DoTask {
    private static DoTask_Dao doTask_dao;

    public static String getDoTaskJson(ParameterApplication parameterApplication,String pageIndex){
        doTask_dao=new DoTask_Dao();
        return doTask_dao.GetDoTaskString(parameterApplication,pageIndex);
    }

    public static List<PrintInfoModel> DoTaskToModel(String jsonTask) {
        doTask_dao=new DoTask_Dao();
        return doTask_dao.DoTaskToModel(jsonTask);
    }

    public static List<Map<String, Object>> getDoTaskMaps(String jsonTask) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<PrintInfoModel> printInfoModel = Service_DoTask.DoTaskToModel(jsonTask);
        if (printInfoModel!=null){
        for (int i = 0; i < printInfoModel.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Task_id", String.valueOf(i+1));
            map.put("Task_filename", printInfoModel.get(i).getPrintInfoFileName());
            map.put("Task_time", printInfoModel.get(i).getPrintInfoApplyTime());
            list.add(map);
        }}
        return list;
    }

    public static String getFirstDoTaskId(String jsonTask) {
        String firstDoTaskId = "";
        List<PrintInfoModel> printInfoModel = Service_DoTask.DoTaskToModel(jsonTask);
        if (printInfoModel != null) {
            firstDoTaskId = printInfoModel.get(0).getPrintInfoID();
        }
        return firstDoTaskId;
    }

    public static String getLastDoTaskId(String jsonTask){
        String lastDoTaskId = "";
        List<PrintInfoModel> printInfoModel = Service_DoTask.DoTaskToModel(jsonTask);
        if (printInfoModel != null) {
            lastDoTaskId = printInfoModel.get(printInfoModel.size()).getPrintInfoID();
        }
        return lastDoTaskId;
    }
}
