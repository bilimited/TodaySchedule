package com.example.todayschedule;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todayschedule.bean.User_Info;
import com.example.todayschedule.bean.User_Table;
import com.example.todayschedule.tool.Base64Coder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class TodaySchedule extends Application {

    public static String LoggedAccount = "local";
    public static String UserID = "";
    public static boolean IsAdmin = false;
    public static int background_id = 2;
    public static Uri background_url;

    public static final int REQUEST_SELECT_PIC = 2;
    public static final int REQUEST_PUT_JSON = 4;
    public static final int REQUEST_ADD_NOTE = 5;

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

    public static void add_nothingHere(Context context, LinearLayout container){
        final View v = LayoutInflater.from(context).inflate(R.layout.card_nothing_here, null);
        container.addView(v);
    }

    /*
    封装版的获取个人信息...
     */
    public static void setInfoView(Activity activity, String UserID, TextView userinfo, TextView username, ImageView portrait){
        User_Info.findUserInfo(UserID, new FindListener<User_Info>() {
            @Override
            public void done(List<User_Info> list, BmobException e) {
                if(e==null&&!list.isEmpty()){
                    User_Info user_info = list.get(0);
                    userinfo.setText(user_info.getUniversity());
                    username.setText(user_info.getNickName());
                    Base64Coder.LoadProtrait(activity,user_info.getPortraitID(),portrait);
                }
            }
        });
    }

    public static void setInfoView(Activity activity, String UserID, TextView username, ImageView portrait){
        User_Info.findUserInfo(UserID, new FindListener<User_Info>() {
            @Override
            public void done(List<User_Info> list, BmobException e) {
                if(e==null&&!list.isEmpty()){
                    User_Info user_info = list.get(0);
                    username.setText(user_info.getNickName());
                    Base64Coder.LoadProtrait(activity,user_info.getPortraitID(),portrait);
                }
            }
        });
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
