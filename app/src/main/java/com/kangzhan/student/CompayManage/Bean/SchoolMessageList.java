package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class SchoolMessageList {
    private String sms_id;
    private String inst_id;
    private String insti_name;
    private String send_counts;
    private String amount;
    private String status;
    private boolean isClick=false;
    private boolean isShow=false;

    public String getSms_id() {
        return sms_id;
    }

    public void setSms_id(String sms_id) {
        this.sms_id = sms_id;
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

    public String getSend_counts() {
        return send_counts;
    }

    public void setSend_counts(String send_counts) {
        this.send_counts = send_counts;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
