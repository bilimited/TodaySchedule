package com.example.todayschedule.bean;


import com.google.gson.annotations.Expose;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Course extends BmobObject implements Serializable {


    private String userid;

    @Expose
    private String courseName;
    @Expose
    private String teacher;
    @Expose
    private String classRoom;
    @Expose
    private int day;
    @Expose
    private int classStart;
    @Expose
    private int classEnd;

    public Course(Course another_course){
        setObjectId(another_course.getObjectId());
    }

    public Course(String courseName, String teacher, String classRoom, int day, int classStart, int classEnd, String account) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
        this.userid = account;

    }

    public Course(String courseName, String teacher, String classRoom, int day, int classStart, int classEnd) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
        this.userid = "local";
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return classStart;
    }

    public void setStart(int classStart) {
        this.classEnd = classStart;
    }

    public int getEnd() {
        return classEnd;
    }

    public void setEnd(int classEnd) {
        this.classEnd = classEnd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
