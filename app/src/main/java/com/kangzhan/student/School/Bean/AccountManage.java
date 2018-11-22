package com.kangzhan.student.School.Bean;

import java.io.Serializable;

/**
 * Created by kangzhan011 on 2017/6/27.
 */

public class AccountManage implements Serializable {
    private String id;
    private String real_name;
    private String phone;
    private String idcard;
    private String addresss;
    private String home_address;
    private String email;
    private String qqnum;
    private String wechatnum;
    private String remark;
    private String status;
    private boolean isClick=false;
    private boolean Visible=false;

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

    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQqnum() {
        return qqnum;
    }

    public void setQqnum(String qqnum) {
        this.qqnum = qqnum;
    }

    public String getWechatnum() {
        return wechatnum;
    }

    public void setWechatnum(String wechatnum) {
        this.wechatnum = wechatnum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean visible) {
        Visible = visible;
    }
}
