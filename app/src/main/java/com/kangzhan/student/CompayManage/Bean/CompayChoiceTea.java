package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class CompayChoiceTea {
    private String id;
    private String name;
    private String sex;
    private String mobile;
    private String teachpermitted;
    private boolean isClick=false ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTeachpermitted() {
        return teachpermitted;
    }

    public void setTeachpermitted(String teachpermitted) {
        this.teachpermitted = teachpermitted;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
