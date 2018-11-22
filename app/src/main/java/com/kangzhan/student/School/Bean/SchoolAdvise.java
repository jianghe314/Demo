package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/11.
 */

public class SchoolAdvise {
    private String id;
    private String sender_name;
    private String create_time;
    private String clerk_name;
    private String clerk_phone;
    private String phone_summary;
    private String pc_summary;
    private String status_name;
    private boolean isClick=false;
    private boolean isShow=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getClerk_name() {
        return clerk_name;
    }

    public void setClerk_name(String clerk_name) {
        this.clerk_name = clerk_name;
    }

    public String getClerk_phone() {
        return clerk_phone;
    }

    public void setClerk_phone(String clerk_phone) {
        this.clerk_phone = clerk_phone;
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

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
