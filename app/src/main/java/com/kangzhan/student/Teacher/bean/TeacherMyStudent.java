package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/10.
 */

public class TeacherMyStudent {
    //少了学员头像地址字段
    private String id;
    private String name;
    private String sex;
    private String status_str;
    private String traintype;
    private String phone;

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

    public String getStatus_str() {
        return status_str;
    }

    public void setStatus_str(String status_str) {
        this.status_str = status_str;
    }

    public String getTraintype() {
        return traintype;
    }

    public void setTraintype(String traintype) {
        this.traintype = traintype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
