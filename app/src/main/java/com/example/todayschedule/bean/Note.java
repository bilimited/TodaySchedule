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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public float getPosx() {
        return posx;
    }

    public void setPosx(float posx) {
        this.posx = posx;
    }

    public float getPosy() {
        return posy;
    }

    public void setPosy(float posy) {
        this.posy = posy;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Note(String content, String userid, float posx, float posy, int day, int type) {
        this.content = content;
        this.userid = userid;
        this.posx = posx;
        this.posy = posy;
        this.day = day;
        this.type = type;
    }

    public Note(Note note){
        this.content = note.getContent();
        this.userid  = note.getUserid();
        this.posx    = note.getPosx();
        this.posy    = note.getPosy();
        this.day     = note.getDay();
        this.type    = note.getType();
        this.setObjectId(note.getObjectId());
    }

    private float posx;
    private float posy;

    private int day;
    private int type;


}
