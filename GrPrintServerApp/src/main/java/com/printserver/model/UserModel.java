package com.printserver.model;

/**
 * Created by zhangxin on 2015/9/6.
 */
public class UserModel {
    private String UserID;
    private String UserName;
    private String UserAccount;
    private String UserIsActive;
    private String UserSec;
    private String MachineSec;
    private String UserPassword;
    private String DeptID;
    private String DeptName;
    private String PosiID;
    private String PosiName;
    private String UserIsLock;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    public String getUserIsActive() {
        return UserIsActive;
    }

    public void setUserIsActive(String userIsActive) {
        UserIsActive = userIsActive;
    }

    public String getUserSec() {
        return UserSec;
    }

    public void setUserSec(String userSec) {
        UserSec = userSec;
    }

    public String getMachineSec() {
        return MachineSec;
    }

    public void setMachineSec(String machineSec) {
        MachineSec = machineSec;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getPosiID() {
        return PosiID;
    }

    public void setPosiID(String posiID) {
        PosiID = posiID;
    }

    public String getPosiName() {
        return PosiName;
    }

    public void setPosiName(String posiName) {
        PosiName = posiName;
    }

    public String getUserIsLock() {
        return UserIsLock;
    }

    public void setUserIsLock(String userIsLock) {
        UserIsLock = userIsLock;
    }
}
