package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

public class Notice extends BmobObject {

    public Notice(String title,String content, String author,String authorid,String img){
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorid = authorid;
        this.imgID = img;
    }

    public Notice(String title,String content, String author,String authorid){
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorid = authorid;
        this.imgID = "";
    }

    private String title;
    private String content;
    private String author;
    private String authorid;
    private String imgID;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public String getImgID() {
        return imgID;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }
}
