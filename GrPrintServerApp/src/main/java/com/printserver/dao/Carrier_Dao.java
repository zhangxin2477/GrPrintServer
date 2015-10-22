package com.printserver.dao;

import com.printserver.base.BaseHelp;
import com.printserver.model.CarrierModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/15.
 */
public class Carrier_Dao {
    private String ip="";
    private String port="";
    private String vdir="";

    public String GetCarrierString(ParameterApplication parameterApplication,String barcode,String comefrom){
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("barcode", barcode);
        parameters.put("comefrom",comefrom);
        String carrier =null;
        try {
            carrier = BaseHelp.GetAllData(ip, port, vdir, "AndroidGetCarrierInfo", parameters);
        }catch (Exception e){
            e.printStackTrace();
            carrier = null;
        }
        return carrier;
    }

    public List<CarrierModel> CarrierToModel(String Carrier) {
        List<CarrierModel> carrierList = new ArrayList<CarrierModel>();
        try {
            JSONArray jsonArray = new JSONObject(Carrier).getJSONArray("CarrierInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                CarrierModel carrierModel = new CarrierModel();
                carrierModel.setCarrierID(jsonObject.getString("id"));
                carrierModel.setCarrierName(jsonObject.getString("载体名称"));
                carrierModel.setCarrierCode(jsonObject.getString("载体编号"));
                carrierModel.setCarrierBarcode(jsonObject.getString("BarCode"));
                carrierModel.setCarrierCreateTime(jsonObject.getString("生成时间"));
                carrierModel.setCarrierManagerID(jsonObject.getString("责任人id"));
                carrierModel.setCarrierManagerName(jsonObject.getString("管理责任人"));
                carrierModel.setCarrierUpdateTime(jsonObject.getString("载体更新时间"));
                carrierList.add(carrierModel);
            }
        } catch (JSONException e) {
            carrierList =null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            carrierList =null;
        }
        return carrierList;
    }

    public String GetTransferCarrierResult(ParameterApplication parameterApplication,String cardID,String carrierIDs){
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("cardID", cardID);
        parameters.put("carrierIDs",carrierIDs);
        String transfercarrierResult =null;
        try {
            transfercarrierResult = BaseHelp.GetAllData(ip, port, vdir, "AndroidTransferCarrier", parameters);
        }catch (Exception e){
            e.printStackTrace();
            transfercarrierResult = null;
        }
        return transfercarrierResult;
    }
}
