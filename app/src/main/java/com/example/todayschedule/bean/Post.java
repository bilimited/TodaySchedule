package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

public class Post extends BmobObject {

    private String content;
    private int type;
    private String author;
    private String authorid;
    private String authorinfo;
    private String imgID;

    private int likes = 0;
    private int comments = 0;

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorinfo() {
        return authorinfo;
    }

    public void setAuthorinfo(String authorinfo) {
        this.authorinfo = authorinfo;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Post( String content, int type, String author, String authorinfo,String authorid) {
        this.content = content;
        this.type = type;
        this.author = author;
        this.authorinfo = authorinfo;
        this.authorid = authorid;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public Post(String id){
        setObjectId(id);
    }
}
