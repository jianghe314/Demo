package com.kangzhan.student.School.Bean;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class AccountMsgDetail {
    private String year;
    private String month;
    private String status;
    private String amount;
    private String send_counts;
    private String status_name;
    private ArrayList<AccountMsgDetailContent> sms_detail;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSend_counts() {
        return send_counts;
    }

    public void setSend_counts(String send_counts) {
        this.send_counts = send_counts;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public ArrayList<AccountMsgDetailContent> getSms_detail() {
        return sms_detail;
    }

    public void setSms_detail(ArrayList<AccountMsgDetailContent> sms_detail) {
        this.sms_detail = sms_detail;
    }
}
