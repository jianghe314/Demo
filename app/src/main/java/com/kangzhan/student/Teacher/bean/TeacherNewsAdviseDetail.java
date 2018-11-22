package com.kangzhan.student.Teacher.bean;

import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/21.
 */

public class TeacherNewsAdviseDetail {
    private String summary;
    private String description;
    private String status_id;
    private String status_name;
    private String close_time;
    private String close_name;
    private List<TeacherNewsAdviseDetailDeal> reply_record;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getClose_name() {
        return close_name;
    }

    public void setClose_name(String close_name) {
        this.close_name = close_name;
    }

    public List<TeacherNewsAdviseDetailDeal> getReply_record() {
        return reply_record;
    }

    public void setReply_record(List<TeacherNewsAdviseDetailDeal> reply_record) {
        this.reply_record = reply_record;
    }
}
