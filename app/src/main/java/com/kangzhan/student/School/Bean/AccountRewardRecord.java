package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/7.
 */

public class AccountRewardRecord {
    private String id;
    private String coach_id;
    private String coach_name;
    private String coach_mobile;
    private String hiredate;
    private String caach_state;
    private String teachpermitted;
    private String training_category;
    private String attach;
    private String score;
    private String amount;
    private boolean isClick=false;
    private boolean isShow=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public void setCoach_name(String coach_name) {
        this.coach_name = coach_name;
    }

    public String getCoach_mobile() {
        return coach_mobile;
    }

    public void setCoach_mobile(String coach_mobile) {
        this.coach_mobile = coach_mobile;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public String getCaach_state() {
        return caach_state;
    }

    public void setCaach_state(String caach_state) {
        this.caach_state = caach_state;
    }

    public String getTeachpermitted() {
        return teachpermitted;
    }

    public void setTeachpermitted(String teachpermitted) {
        this.teachpermitted = teachpermitted;
    }

    public String getTraining_category() {
        return training_category;
    }

    public void setTraining_category(String training_category) {
        this.training_category = training_category;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
