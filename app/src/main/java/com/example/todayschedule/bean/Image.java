package com.example.todayschedule.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @ClassName Image
 * @Description TODO
 * @Author 找小样
 * @DATE 2023/5/24 11:15
 * @Version 1.0
 */
public class Image extends BmobObject {

    String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
