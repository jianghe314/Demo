package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class AccountSchoolChecking {
    private String state_id;
    private String inst_id;
    private String insti_name;
    private String clerk_name;
    private String location_name;
    private String month;
    private String amount;
    private String draw;
    private String remittance_number;
    private String status;
    private boolean isShow=false;
    private boolean isClick=false;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getInsti_name() {
        return insti_name;
    }

    public void setInsti_name(String insti_name) {
        this.insti_name = insti_name;
    }

    public String getClerk_name() {
        return clerk_name;
    }

    public void setClerk_name(String clerk_name) {
        this.clerk_name = clerk_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getRemittance_number() {
        return remittance_number;
    }

    public void setRemittance_number(String remittance_number) {
        this.remittance_number = remittance_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
