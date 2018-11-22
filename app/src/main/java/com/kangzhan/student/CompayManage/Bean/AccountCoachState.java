package com.kangzhan.student.CompayManage.Bean;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class AccountCoachState {
    private String id;
    private String train_length;
    private String amount;
    private String draw;
    private String coachname;
    private ArrayList<AccountCoachRecord> coachRecord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrain_length() {
        return train_length;
    }

    public void setTrain_length(String train_length) {
        this.train_length = train_length;
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

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public ArrayList<AccountCoachRecord> getCoachRecord() {
        return coachRecord;
    }

    public void setCoachRecord(ArrayList<AccountCoachRecord> coachRecord) {
        this.coachRecord = coachRecord;
    }
}
