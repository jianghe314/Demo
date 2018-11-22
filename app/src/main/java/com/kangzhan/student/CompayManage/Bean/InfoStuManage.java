package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class InfoStuManage {
    private String id;
    private String stunum;
    private String name;
    private String sex;
    private String phone;
    private String applydate;
    private String status_str;
    private String traintype;
    private String training_coach_name;
    private boolean isShow=false;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStunum() {
        return stunum;
    }

    public void setStunum(String stunum) {
        this.stunum = stunum;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
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

    public String getTraining_coach_name() {
        return training_coach_name;
    }

    public void setTraining_coach_name(String training_coach_name) {
        this.training_coach_name = training_coach_name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
