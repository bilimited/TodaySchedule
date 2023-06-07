package com.example.todayschedule.tool;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.todayschedule.TodaySchedule;
import com.google.gson.Gson;

/**
 * @ClassName AIHandler
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/6/7 20:31
 * @Version 1.0
 */
public class AIHandler extends Handler {

    public String postid;

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1){
            String json = msg.obj.toString();
            ArtificialIdiot.AIMessege obj = new Gson().fromJson(json, ArtificialIdiot.AIMessege.class);
            if(obj!=null){
                String reponse = obj.result.get("displayText");
                ArtificialIdiot.AIPost(reponse, TodaySchedule.UserID,postid);
            }
        }
    }
}