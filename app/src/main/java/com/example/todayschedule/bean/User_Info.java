package com.example.todayschedule.bean;

import com.example.todayschedule.TodaySchedule;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @ClassName User_Info
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/5/28 22:58
 * @Version 1.0
 */
public class User_Info extends BmobObject {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    String nickName;
    String realName;
    String university;
    String age;
    String province;
    int gender;
    String portraitID;

    boolean isPublic;

    public boolean getPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    //该userinfo隶属哪个用户
    String userID;

    public String getPortraitID() {
        return portraitID;
    }

    public void setPortraitID(String portraitID) {
        this.portraitID = portraitID;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public User_Info(String nickName, String realName, String university, String age, String province, int gender, String userID) {
        this.realName = realName;
        this.university = university;
        this.age = age;
        this.province = province;
        this.gender = gender;
        this.userID = userID;
        this.nickName = nickName;
    }

    public static void findUserInfo(String userID,FindListener<User_Info> listener){
        BmobQuery<User_Info> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userID", userID);
        bmobQuery.findObjects(listener);
    }
}
