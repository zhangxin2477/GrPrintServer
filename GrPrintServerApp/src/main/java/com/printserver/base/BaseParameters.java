package com.printserver.base;

import android.app.Activity;
import android.content.Context;

import java.security.PublicKey;

/**
 * Created by zhangxin on 2015/9/16.
 */
public interface BaseParameters {
    public static final String RESULT_SUCCESS = "ok";
    public static final String RESULT_FAIL = "no";
    public static final String RESULT_SETUP = "setup_result";
    public static final String RESULT_LOGIN = "login_result";
    public static final String RESULT_UNTASK = "untask_result";
    public static final String RESULT_POSTUT = "postut_result";
    public static final String RESULT_DOTASK = "dotask_result";
    public static final String RESULT_TC = "tc_result";
    public static final String RESULT_EP = "ep_result";
    public static final String RESULT_POSTEP = "postep_result";
    public static final String RESULT_UPDATE = "update_result";

    public static final String LOADINGTYPE = "LoadingType";
    public static final String INIT = "Init";
    public static final String INITSETUP = "InitSetup";
    public static final String LOGIN = "Login";
    public static final String DOTASK = "DoTask";
    public static final String UNTASK = "UnTask";
    public static final String TRANSFERCARRIER = "TransferCarrier";
    public static final String ENTRUSTPRINT = "EntrustPrint";
    public static final String POSTEP = "PostEP";
    public static final String UPDATE = "Update";
    public static final String POSTUT = "PostUT";

    //接口
    public static final String METHOTEST = "AndroidTestConnect";
    public static final String METHOUSERINFO = "AndroidGetUserInfo";
    public static final String METHOUNPRINTINFO = "AndroidGetUnPrintInfo";
    public static final String METHODOPRINTINFO = "AndroidGetDoPrintInfo";
    public static final String METHOCARRIERINFO = "AndroidGetCarrierInfo";
    public static final String METHOENTRUSTPRINTINFO = "AndroidEntrustPrintInfo";
    public static final String METHOPOSTEPI = "AndroidPostEP";

    public static final String TAG = "error";

}
