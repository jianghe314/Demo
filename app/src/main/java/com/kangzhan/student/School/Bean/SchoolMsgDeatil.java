package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/12.
 */

public class SchoolMsgDeatil {
    private String send_time;
    private String receiver_phone;
    private String receiver_name;
    private String content;
    private String counts;
    private String sms_status;
    private String create_time;

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
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

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getSms_status() {
        return sms_status;
    }

    public void setSms_status(String sms_status) {
        this.sms_status = sms_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
