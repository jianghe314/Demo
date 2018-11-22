package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class SchoolNoticeDetail {
    private String send_time;
    private String summary;
    private String receiver_name;
    private String content;
    private String push_status;
    private String create_time;

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPush_status() {
        return push_status;
    }

    public void setPush_status(String push_status) {
        this.push_status = push_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
