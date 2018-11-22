package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/28.
 */

public class SchoolAccontInfo {
    private String id;
    private String real_name;
    private String phone;
    private String idcard;
    private String inst_id;
    private String recorder_type;
    private String recorder_id;
    private String status;
    private String clerk_id;
    private boolean isShow=false;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getRecorder_type() {
        return recorder_type;
    }

    public void setRecorder_type(String recorder_type) {
        this.recorder_type = recorder_type;
    }

    public String getRecorder_id() {
        return recorder_id;
    }

    public void setRecorder_id(String recorder_id) {
        this.recorder_id = recorder_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClerk_id() {
        return clerk_id;
    }

    public void setClerk_id(String clerk_id) {
        this.clerk_id = clerk_id;
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
