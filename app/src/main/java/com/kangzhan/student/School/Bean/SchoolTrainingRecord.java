package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/5.
 */

public class SchoolTrainingRecord {
    private String stu_id;
    private String stuname;
    private String sex;
    private String shuttle_name;
    private String applydate;
    private String sdate;
    private String stime;
    private String etime;
    private String coachname;
    private String detail;
    private String licnum;
    private String timefiff;
    private String price;
    private boolean isClick=false;

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getShuttle_name() {
        return shuttle_name;
    }

    public void setShuttle_name(String shuttle_name) {
        this.shuttle_name = shuttle_name;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLicnum() {
        return licnum;
    }

    public void setLicnum(String licnum) {
        this.licnum = licnum;
    }

    public String getTimefiff() {
        return timefiff;
    }

    public void setTimefiff(String timefiff) {
        this.timefiff = timefiff;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
