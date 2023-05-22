package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String content;
    private String author;
    private String author_id;
    private String to_uid;
    private String to_post;

    public Comment(String content, String author, String author_id, String to_uid, String to_post) {
        this.content = content;
        this.author = author;
        this.author_id = author_id;
        this.to_uid = to_uid;
        this.to_post = to_post;
    }

    public Comment(String id){
        setObjectId(id);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getTo_post() {
        return to_post;
    }

    public void setTo_post(String to_post) {
        this.to_post = to_post;
    }




}
