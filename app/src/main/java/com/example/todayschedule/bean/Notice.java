package com.example.todayschedule.bean;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.example.todayschedule.R;

import cn.bmob.v3.BmobObject;

public class Notice extends BmobObject {

    public Notice(String title,String content, String author,String authorid,@DrawableRes int img){
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorid = authorid;
        this.img = img;
    }

    public Notice(String title,String content, String author,String authorid){
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorid = authorid;
        this.img = R.drawable.ic_launcher_background;
    }

    private String title;
    private String content;
    private String author;
    private String authorid;
    private int img;

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

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }
}
