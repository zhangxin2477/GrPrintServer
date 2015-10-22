package com.printserver.dao;

import com.printserver.base.BaseHelp;
import com.printserver.model.CardModel;
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
 * Created by zhangxin on 2015/9/16.
 */
public class Card_Dao {
    private String ip="";
    private String port="";
    private String vdir="";

    public String GetCardString(ParameterApplication parameterApplication,String cardInfo){
        ip = parameterApplication.getConnectIP();
        port = parameterApplication.getConnectPort();
        vdir = parameterApplication.getConnectVrid();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("cardInfo", cardInfo);
        String carrier =null;
        try {
            carrier = BaseHelp.GetAllData(ip, port, vdir, "AndroidGetCardInfo", parameters);
        }catch (Exception e){
            e.printStackTrace();
            carrier = null;
        }
        return carrier;
    }

    public List<CardModel> CardToModel(String jsonCard) {
        List<CardModel> cardModelList = new ArrayList<CardModel>();
        try {
            JSONArray jsonArray = new JSONObject(jsonCard).getJSONArray("CardInfo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                CardModel cardModel = new CardModel();
                cardModel.setCardID(jsonObject.getString("id"));
                cardModel.setCardInfo(jsonObject.getString("cardID"));
                cardModel.setCardType(jsonObject.getString("type"));
                cardModel.setCardState(jsonObject.getString("state"));
                cardModel.setCardUseAuthor(jsonObject.getString("useScope"));
                cardModel.setCardUserID(jsonObject.getString("userID"));
                cardModel.setCardUserName(jsonObject.getString("userName"));
                cardModelList.add(cardModel);
            }
        } catch (JSONException e) {
            cardModelList =null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            cardModelList =null;
        }
        return cardModelList;
    }
}
