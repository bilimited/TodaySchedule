package com.example.todayschedule;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todayschedule.bean.User_Table;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


public class TodaySchedule extends Application {

    public static String LoggedAccount = "local";
    public static String UserID = "";
    public static boolean IsAdmin = false;

    public static final int REQUEST_SELECT_PIC = 2;

    /**
     * 检查用户是否登录。
     * @return
     */
    public static boolean isLogged(){
        if(LoggedAccount=="local"||UserID==""){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 检查用户是否管理员
     * @return
     */
    public static boolean isAdmin(){
        return IsAdmin;
    }

    /**
     * 退出登录
     */
    public static void logout(){
        LoggedAccount = "local";
        UserID = "";
    }

    @Deprecated
    private void autoLogin(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "app初始化", Toast.LENGTH_SHORT).show();
        //Bmob.resetDomain("https://new.bmobapp.com/");

        Bmob.initialize(this, "6ad22596f1f07c0bee6a917bd1b86cce");

        /**
         * TODO: 自动登录是摆设
        BmobQuery<User_Table> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<User_Table>() {
            @Override
            public void done(List<User_Table> object, BmobException e) {
                SharedPreferences sharedPreferences = getSharedPreferences("global", MODE_PRIVATE);
                String lastAccount, lastpsw;
                lastAccount = sharedPreferences.getString("lastAccount", "");
                lastpsw = sharedPreferences.getString("lastPassword", "");
                if (e == null && !lastAccount.equals("") && !lastpsw.equals("")) {
                    for (User_Table user_table : object) {
                        if (user_table.getAccount().equals(lastAccount) && user_table.getPassword().equals(lastpsw)) {
                            TodaySchedule.LoggedAccount = user_table.getAccount();
                            Log.d("test","已自动登录");
                            break;
                        }
                    }
                }
            }
        });*/
    }
}
