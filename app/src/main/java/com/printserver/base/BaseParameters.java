package com.printserver.base;

import android.app.Activity;
import android.content.Context;

/**
 * Created by zhangxin on 2015/9/16.
 */
public interface BaseParameters {
    public static final String RESULT_SUCCESS = "ok";
    public static final String RESULT_FAIL = "no";

    public static final String LOADINGTYPE="LoadingType";
    public static final String INIT = "Init";
    public static final String INITSETUP = "InitSetup";
    public static final String LOGIN = "Login";
    public static final String DOTASK="DoTask";
    public static final String UNTASK="UnTask";
    public static final String TRANSFERCARRIER="TransferCarrier";
    public static final String ENTRUSTPRINT="EntrustPrint";
    public static final String POSTEP="PostEP";
    public static final String UPDATE="Update";
    public static final String POSTUT="PostUT";

    public static final String TAG="error";

//    public enum LoadingType {
//        INITSETUP("InitSetup", 1),
//        LOGIN("Login", 2),
//        INIT("Init", 3);
//
//        // 成员变量
//        private String name;
//        private int index;
//
//        // 构造方法
//        private LoadingType(String name, int index) {
//            this.name = name;
//            this.index = index;
//        }
//
//        // 普通方法
//        public static String getName(int index) {
//            for (LoadingType c : LoadingType.values()) {
//                if (c.getIndex() == index) {
//                    return c.name;
//                }
//            }
//            return null;
//        }
//
//        // get set 方法
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//
//        public void setIndex(int index) {
//            this.index = index;
//        }
//    }
//    ;
}
