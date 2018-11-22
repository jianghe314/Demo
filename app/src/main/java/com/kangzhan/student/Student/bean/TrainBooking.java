package com.kangzhan.student.Student.bean;

/**
 * Created by kangzhan011 on 2017/3/29.
 */

public class TrainBooking {
    private String id;
    private String status;
    private String sex;
    private String start_time;
    private String end_time;
    private String shuttle;
    private String detail;
    private String price;
    private String coachname;
    private String coachmobile;
    private String diff;
    private String is_cancel;
    //private String oss_photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getShuttle() {
        return shuttle;
    }

    public void setShuttle(String shuttle) {
        this.shuttle = shuttle;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getCoachmobile() {
        return coachmobile;
    }

    public void setCoachmobile(String coachmobile) {
        this.coachmobile = coachmobile;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }
}
