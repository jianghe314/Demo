package com.kangzhan.student.Student.bean;

/**
 * Created by kangzhan011 on 2017/3/28.
 */

public class Advise {
    private String id;
    private String phone_summary;
    private String pc_summary;
    private String state;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone_summary() {
        return phone_summary;
    }

    public void setPhone_summary(String phone_summary) {
        this.phone_summary = phone_summary;
    }

    public String getPc_summary() {
        return pc_summary;
    }

    public void setPc_summary(String pc_summary) {
        this.pc_summary = pc_summary;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
