package com.kangzhan.student.CompayManage.Bean;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class MessageCountDetail {
    private String sms_month;
    private String amount;
    private String success_counts;
    private ArrayList<MessageCountDetailRecord> detail;

    public String getSms_month() {
        return sms_month;
    }

    public void setSms_month(String sms_month) {
        this.sms_month = sms_month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSuccess_counts() {
        return success_counts;
    }

    public void setSuccess_counts(String success_counts) {
        this.success_counts = success_counts;
    }

    public ArrayList<MessageCountDetailRecord> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<MessageCountDetailRecord> detail) {
        this.detail = detail;
    }
}
