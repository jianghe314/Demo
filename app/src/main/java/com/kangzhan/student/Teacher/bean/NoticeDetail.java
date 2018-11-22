package com.kangzhan.student.Teacher.bean;

/**
 * Created by kangzhan011 on 2017/4/27.
 */

public class NoticeDetail {
    private String summary;
    private String sender_id;
    private String send_time;
    private String content;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
