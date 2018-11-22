package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/6/30.
 */

public class EduTeacherRestList {
    private String id;
    private String coach_id;
    private String coach_name;
    private String coach_phone;
    private String start_time;
    private String end_time;
    private String reason;
    private String cancel_date;
    private String status;
    private String typer;
    private String remark;
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

    public String getCoach_phone() {
        return coach_phone;
    }

    public void setCoach_phone(String coach_phone) {
        this.coach_phone = coach_phone;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTyper() {
        return typer;
    }

    public void setTyper(String typer) {
        this.typer = typer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
