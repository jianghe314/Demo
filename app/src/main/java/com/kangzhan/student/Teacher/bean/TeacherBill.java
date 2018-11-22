package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class TeacherBill {
    private String id;
    private String amount;
    private String draw;
    private String train_length;
    private String train_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTrain_length() {
        return train_length;
    }

    public void setTrain_length(String train_length) {
        this.train_length = train_length;
    }

    public String getTrain_date() {
        return train_date;
    }

    public void setTrain_date(String train_date) {
        this.train_date = train_date;
    }
}
