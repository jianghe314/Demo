package com.kangzhan.student.zfbapi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/19.
 */

public class AliPay {
    private String app_id;
    private Biz_content biz_content;
    private String method;
    private String notify_url;
    private String sign_type;
    private String timestamp;
    private String version;
    private String sign;
    private String publickey;
    private String privatekey;
    private String charset;
    private String format;
    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public Biz_content getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(Biz_content biz_content) {
        this.biz_content = biz_content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "AliPay[appId="+app_id+",biz_contents="+biz_content+",method="+method+"]";
    }
}
