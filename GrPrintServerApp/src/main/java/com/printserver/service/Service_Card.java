package com.printserver.service;

import com.printserver.dao.Card_Dao;
import com.printserver.model.CardModel;
import com.printserver.views.ParameterApplication;

import java.util.List;

/**
 * Created by zhangxin on 2015/9/16.
 */
public class Service_Card {
    private static Card_Dao card_dao;

    public static String GetCardString(ParameterApplication parameterApplication,String cardInfo) {
        card_dao=new Card_Dao();
        return card_dao.GetCardString(parameterApplication,cardInfo);
    }

    public static List<CardModel> CardToModel(String jsonCard) {
        card_dao=new Card_Dao();
        return card_dao.CardToModel(jsonCard);
    }

}
