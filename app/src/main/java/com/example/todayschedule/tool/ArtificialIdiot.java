package com.example.todayschedule.tool;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todayschedule.EditActivity;
import com.example.todayschedule.TodaySchedule;
import com.example.todayschedule.bean.Comment;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ClassName ArtificialIdiot
 * @Description 人工智障类。整个大作业最没用的一个类
 * @Author 找小样
 * @DATE 2023/6/7 20:28
 * @Version 1.0
 */
public class ArtificialIdiot {

    /**
     * 用于连接网络线程和主线程
     * 用于处理收到的人工智障发来的消息
     */
    class AIMessege{
        public int code;
        public HashMap<String,String> result;
        public String msg;
    }

    public static AIHandler myHandler = new AIHandler();
    public static String xiaoAiUri = "https://api.oioweb.cn/api/ai/chat";

    public static boolean isCallingXiaoAi(String text){
        return text.indexOf("@小爱")!=-1;
    }

    /*
     *  获取AI回帖的方法。调整要回的帖子请设置myhandler.postid
     */
    public static void getAIResponce(String text,String postid) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(xiaoAiUri + "?text=" + text).build();
        myHandler.postid = postid;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                Log.d("小爱:", json);
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                myHandler.sendMessage(message);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 机器人也能发帖了?
     * @param text
     */
    public static void AIPost(String text,String postAuthorID,String postID){
        Comment comment = new Comment(
                text,
                "xiaoai",
                "8b00fba8bf",
                postAuthorID,
                postID
        );
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e!=null){
                    Log.d("小爱","发了一个回复");
                }
            }
        });
    }

}
