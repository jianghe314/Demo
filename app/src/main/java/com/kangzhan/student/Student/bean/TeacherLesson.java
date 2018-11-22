package com.kangzhan.student.Student.bean;

/**
 * Created by kangzhan011 on 2017/5/5.
 */

public class TeacherLesson {
    private String name;
    private String subj_id;
    private String max_stu;
    private String inst_id;
    private String perdritype;
    private String licnum;
    private String brand;
    private String car_id;
    private String starttime;
    private String endtime;
    private String stage;
    private String price;
    private String number;
    private String is_gray;
    private String msg;
    private boolean isClick=false;

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubj_id() {
        return subj_id;
    }

    public void setSubj_id(String subj_id) {
        this.subj_id = subj_id;
    }

    public String getMax_stu() {
        return max_stu;
    }

    public void setMax_stu(String max_stu) {
        this.max_stu = max_stu;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getPerdritype() {
        return perdritype;
    }

    public void setPerdritype(String perdritype) {
        this.perdritype = perdritype;
    }

    public String getLicnum() {
        return licnum;
    }

    public void setLicnum(String licnum) {
        this.licnum = licnum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }


    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIs_gray() {
        return is_gray;
    }

    public void setIs_gray(String is_gray) {
        this.is_gray = is_gray;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
