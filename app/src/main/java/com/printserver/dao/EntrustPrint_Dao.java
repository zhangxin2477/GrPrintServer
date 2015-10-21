package com.printserver.dao;

import com.printserver.base.BaseHelp;
import com.printserver.model.EntrustPrintModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/17.
 */
public class EntrustPrint_Dao {
    private String ip="";
    private String port="";
    private String vdir="";

    public String GetEntrustPrintString(ParameterApplication parameterApplication){
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        String userID = parameterApplication.getUserInfo().getUserID();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userID", userID);
        String ep =null;
        try {
            ep = BaseHelp.GetAllData(ip, port, vdir, "AndroidEntrustPrintInfo", parameters);
        }catch (Exception e){
            e.printStackTrace();
            ep = null;
        }
        return ep;
    }

    public List<EntrustPrintModel> EntrustPrintToModel(String jsonEP) {
        List<EntrustPrintModel> entrustPrintModelList = new ArrayList<EntrustPrintModel>();
        try {
            JSONArray jsonArray = new JSONObject(jsonEP).getJSONArray("EntrustPrintInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                EntrustPrintModel entrustPrintModel = new EntrustPrintModel();
                entrustPrintModel.setEntrustPrintID(jsonObject.getString("id"));
                entrustPrintModel.setEntrustPrintAUserID(jsonObject.getString("LAUserID"));
                entrustPrintModel.setEntrustPrintAUserName(jsonObject.getString("LAUserName"));
                entrustPrintModel.setEntrustPrintHUserID(jsonObject.getString("LHUserID"));
                entrustPrintModel.setEntrustPrintHUserName(jsonObject.getString("LHUserName"));
                entrustPrintModel.setEntrustPrintApplyTime(jsonObject.getString("LApplyTime"));
                entrustPrintModel.setEntrustPrintBussContent(jsonObject.getString("LBussContent"));
                entrustPrintModel.setEntrustPrintBussType(jsonObject.getString("LBussType"));
                entrustPrintModel.setEntrustPrintOpState(jsonObject.getString("LOpStatus"));
                entrustPrintModel.setEntrustPrintOpTime(jsonObject.getString("LOPTime"));
                entrustPrintModel.setEntrustPrintTrState(jsonObject.getString("LTransferStatus"));
                entrustPrintModel.setEntrustPrintTrTime(jsonObject.getString("LTransferTime"));
                entrustPrintModel.setEntrustPrintBussID(jsonObject.getString("LBussID"));
                entrustPrintModelList.add(entrustPrintModel);
            }
        } catch (JSONException e) {
            entrustPrintModelList =null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            entrustPrintModelList =null;
        }
        return entrustPrintModelList;
    }

    public String GetPostEPResult(ParameterApplication parameterApplication,String epIds){
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("epIds", epIds);
        String ep =null;
        try {
            ep = BaseHelp.GetAllData(ip, port, vdir, "AndroidPostEP", parameters);
        }catch (Exception e){
            e.printStackTrace();
            ep = null;
        }
        return ep;
    }
}
