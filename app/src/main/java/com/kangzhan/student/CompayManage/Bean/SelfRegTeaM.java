package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/25.
 */

public class SelfRegTeaM {
    private String id;
    private String real_name;
    private String mobile;
    private String clerk_id;
    private String process_flag;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getClerk_id() {
        return clerk_id;
    }

    public void setClerk_id(String clerk_id) {
        this.clerk_id = clerk_id;
    }

    public String getProcess_flag() {
        return process_flag;
    }

    public void setProcess_flag(String process_flag) {
        this.process_flag = process_flag;
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
