package com.printserver.model;

/**
 * Created by zhangxin on 2015/9/9.
 */
public class CardModel {
    private String CardID;
    private String CardInfo;
    private String CardUserID;
    private String CardUserName;
    private String CardState;
    private String CardType;
    private String CardUseAuthor;

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getCardInfo() {
        return CardInfo;
    }

    public void setCardInfo(String cardInfo) {
        CardInfo = cardInfo;
    }

    public String getCardUserID() {
        return CardUserID;
    }

    public void setCardUserID(String cardUserID) {
        CardUserID = cardUserID;
    }

    public String getCardUserName() {
        return CardUserName;
    }

    public void setCardUserName(String cardUserName) {
        CardUserName = cardUserName;
    }

    public String getCardState() {
        return CardState;
    }

    public void setCardState(String cardState) {
        CardState = cardState;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getCardUseAuthor() {
        return CardUseAuthor;
    }

    public void setCardUseAuthor(String cardUseAuthor) {
        CardUseAuthor = cardUseAuthor;
    }
}
