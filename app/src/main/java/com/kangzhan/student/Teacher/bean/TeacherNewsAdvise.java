package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/27.
 */

public class TeacherNewsAdvise {
    private String id;
    private String create_time;
    private String phone_summary;
    private String pc_summary;
    private String status_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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
}
