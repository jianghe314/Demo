package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/3.
 */

public class ChoiceTeacher {
    private String id;
    private String name;
    private String starLevel;
    private String sex;
    private String idcard;
    private String mobile;
    private String teachpermitted;
    private String attach;
    private String student_count;
    private boolean isClick=false;

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

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getStudent_count() {
        return student_count;
    }

    public void setStudent_count(String student_count) {
        this.student_count = student_count;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
