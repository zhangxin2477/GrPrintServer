package com.printserver.service;

import com.printserver.dao.Carrier_Dao;
import com.printserver.model.CarrierModel;
import com.printserver.model.PrintInfoModel;
import com.printserver.views.ParameterApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/16.
 */
public class Service_Carrier {
    private static Carrier_Dao carrier_dao;

    public static List<CarrierModel> CarrierToModel(String jsonCarrier){
        carrier_dao=new Carrier_Dao();
        return carrier_dao.CarrierToModel(jsonCarrier);
    }

    public static List<Map<String, Object>> getCarrierMaps(String jsonCarrier) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<CarrierModel> carrierModel = Service_Carrier.CarrierToModel(jsonCarrier);
        for (int i = 0; i < carrierModel.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("TransferCarrier_id", Integer.toString(i + 1));
            map.put("TransferCarrier_file", carrierModel.get(i).getCarrierName());
            map.put("TransferCarrier_time", carrierModel.get(i).getCarrierCreateTime());
            map.put("TransferCarrier_responsible",carrierModel.get(i).getCarrierManagerName());
            list.add(map);
        }
        return list;
    }
}
