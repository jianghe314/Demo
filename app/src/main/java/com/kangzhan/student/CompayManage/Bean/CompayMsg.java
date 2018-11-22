package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class CompayMsg {
    private String id;
    private String receiver_phone;
    private String receiver_name;
    private String sender_source;
    private String sender_name;
    private String send_time;
    private String content;
    private String sms_status;
    private boolean isShow=false;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSender_source() {
        return sender_source;
    }

    public void setSender_source(String sender_source) {
        this.sender_source = sender_source;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
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

    public String getSms_status() {
        return sms_status;
    }

    public void setSms_status(String sms_status) {
        this.sms_status = sms_status;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
