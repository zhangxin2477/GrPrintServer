package com.printserver.dao;

import com.printserver.base.BaseHelp;
import com.printserver.model.PrintInfoModel;
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
public class DoTask_Dao {
    private String ip="";
    private String port="";
    private String vdir="";
    private String userID="";

    public String GetDoTaskString(ParameterApplication parameterApplication,String pageIndex){
        userID=parameterApplication.getUserInfo().getUserID();
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userID);
        parameters.put("pageIndex",pageIndex);
        String dotask =null;
        try {
            dotask = BaseHelp.GetAllData(ip, port, vdir, "AndroidGetDoPrintInfo", parameters);
        }catch (Exception e){
            e.printStackTrace();
            dotask = null;
        }
        return dotask;
    }

    public List<PrintInfoModel> DoTaskToModel(String DoTask) {
        List<PrintInfoModel> doTaskList = new ArrayList<PrintInfoModel>();
        try {
            JSONArray jsonArray = new JSONObject(DoTask).getJSONArray("DoPrintInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                PrintInfoModel printInfoModel = new PrintInfoModel();
                printInfoModel.setPrintInfoID(jsonObject.getString("id"));
                printInfoModel.setPrintInfoUserID(jsonObject.getString("fi_new_userid"));
                printInfoModel.setPrintInfoUserName(jsonObject.getString("申请人"));
                printInfoModel.setPrintInfoDeptID(jsonObject.getString("部门id"));
                printInfoModel.setPrintInfoApplyTime(jsonObject.getString("申请时间"));
                printInfoModel.setPrintInfoSec(jsonObject.getString("密级"));
                printInfoModel.setPrintInfoFileName(jsonObject.getString("文件名称"));
                printInfoModel.setPrintInfoAuditResult(jsonObject.getString("审核结果"));
                printInfoModel.setPrintInfoAuditTime(jsonObject.getString("审核时间"));
                printInfoModel.setPrintInfoPrintState(jsonObject.getString("打印状态"));
                printInfoModel.setPrintInfoPrintTime(jsonObject.getString("打印时间"));
                printInfoModel.setPrintInfoCarrierCode(jsonObject.getString("载体编码"));
                printInfoModel.setPrintInfoPrinterID(jsonObject.getString("打印机id"));
                printInfoModel.setPrintInfoHelpOutState(jsonObject.getString("HelpOutJob"));
                printInfoModel.setPrintInfoHelpOutUser(jsonObject.getString("HelpOutUser"));
                //printInfoModel.setPrintInfoRowNumber(jsonObject.getString("rownumber"));
                doTaskList.add(printInfoModel);
            }
        } catch (JSONException e) {
            doTaskList =null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            doTaskList =null;
        }
        return doTaskList;
    }
}
