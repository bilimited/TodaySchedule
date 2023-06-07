package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

/**
 * @ClassName Note
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/6/7 21:29
 * @Version 1.0
 */
public class Note extends BmobObject {

    private String content;
    private String userid;

    private float posx;
    private float posy;

    private int day;
    private int startTime;


}
