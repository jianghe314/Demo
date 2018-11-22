package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class TeacherMsg {
    private String id;
    private String year;
    private String month;
    private String amount;
    private String send_counts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
