package com.kangzhan.student.CompayManage.Bean;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class AccountInstiState {
    private String start_time;
    private String end_time;
    private String train_length;
    private String amount;
    private String draw;
    private ArrayList<AccountCoachState> coachState;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    public ArrayList<AccountCoachState> getCoachState() {
        return coachState;
    }

    public void setCoachState(ArrayList<AccountCoachState> coachState) {
        this.coachState = coachState;
    }
}
