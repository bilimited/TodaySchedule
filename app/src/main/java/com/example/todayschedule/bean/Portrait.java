package com.example.todayschedule.bean;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * @ClassName Portrait
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/5/24 14:27
 * @Version 1.0
 */
public class Portrait extends BmobObject {

    public static HashMap<String,Portrait> portrait_cache = new HashMap<>();

    public static void loadToCache(List<Portrait> list){
        for(Portrait info:list){
            portrait_cache.put(info.getObjectId(),info);
        }
    }

    String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
