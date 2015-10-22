package com.printserver.service;

import com.printserver.dao.UnTask_Dao;
import com.printserver.model.PrintInfoModel;
import com.printserver.views.ParameterApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class Service_UnTask {
    private static UnTask_Dao unTask_dao;

    public static String getUnTaskJson(ParameterApplication parameterApplication,String pageIndex){
        unTask_dao=new UnTask_Dao();
        return unTask_dao.GetUnTaskString(parameterApplication,pageIndex);
    }

    public static List<PrintInfoModel> UnTaskToModel(String jsonTask) {
        unTask_dao=new UnTask_Dao();
        return unTask_dao.UnTaskToModel(jsonTask);
    }

    public static List<Map<String, Object>> getUnTaskMaps(String jsonTask) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<PrintInfoModel> printInfoModel = printInfoModel = Service_UnTask.UnTaskToModel(jsonTask);
        if(printInfoModel!=null){
        for (int i = 0; i < printInfoModel.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Task_check",printInfoModel.get(i).getPrintInfoID());
            map.put("Task_id", String.valueOf(i+1));
            map.put("Task_filename", printInfoModel.get(i).getPrintInfoFileName());
            map.put("Task_time", printInfoModel.get(i).getPrintInfoApplyTime());
            list.add(map);
        }}
        return list;
    }

    public static String getFirstUnTaskId(String jsonTask) {
        String firstUnTaskId = "";
        List<PrintInfoModel> printInfoModel = Service_UnTask.UnTaskToModel(jsonTask);
        if (printInfoModel != null) {
            firstUnTaskId = printInfoModel.get(0).getPrintInfoID();
        }
        return firstUnTaskId;
    }

    public static String getLastUnTaskId(String jsonTask){
        String lastUnTaskId = "";
        List<PrintInfoModel> printInfoModel = Service_UnTask.UnTaskToModel(jsonTask);
        if (printInfoModel != null) {
            lastUnTaskId = printInfoModel.get(printInfoModel.size()).getPrintInfoID();
        }
        return lastUnTaskId;
    }
}
