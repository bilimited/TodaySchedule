package com.example.todayschedule.bean;

public class University {
    private int id;
    private String name;
    private String province;

    public University(int id, String name, String province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return name;
    }

}
