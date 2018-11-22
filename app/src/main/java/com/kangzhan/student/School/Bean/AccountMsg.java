package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class AccountMsg {
    private String id;
    private String month;
    private String amount;
    private String send_counts;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSend_counts() {
        return send_counts;
    }

    public void setSend_counts(String send_counts) {
        this.send_counts = send_counts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
