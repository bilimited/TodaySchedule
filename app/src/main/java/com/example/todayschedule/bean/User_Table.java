package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

public class User_Table extends BmobObject {
    //账号
    private String Account;
    //密码
    private String Password;

    private boolean admin;

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getAdmin(){
        return admin;
    }

}
