package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/26.
 */

public class TeacherMsgCheckNotice {
    private String id;
    private String phone_summary;
    private String phone_content;
    private String pc_content;
    private String pc_summary;
    private String sender_id;
    private String status;
    private String send_time;
    private String msg;

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

    public String getPhone_content() {
        return phone_content;
    }

    public void setPhone_content(String phone_content) {
        this.phone_content = phone_content;
    }

    public String getPc_content() {
        return pc_content;
    }

    public void setPc_content(String pc_content) {
        this.pc_content = pc_content;
    }

    public String getPc_summary() {
        return pc_summary;
    }

    public void setPc_summary(String pc_summary) {
        this.pc_summary = pc_summary;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
