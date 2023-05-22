package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

public class User_Table extends BmobObject {
    //账号
    private String Account;
    //密码
    private String Password;

    private int flag;

    private boolean admin;

    private String info;

    private String nickname;

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

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getAdmin(){
        return admin;
    }

}
