package com.kangzhan.student.wxapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kangzhan011 on 2017/6/2.
 */

public class WXpay {
    private String appid;

    private String partnerid;

    private String noncestr;

    private String timestamp;

    @SerializedName("package")
    private String mpackage;

    private String sign;

    private String prepayid;

    private String Out_trade_no;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMpackage() {
        return mpackage;
    }

    public void setMpackage(String mpackage) {
        this.mpackage = mpackage;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getOut_trade_no() {
        return Out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        Out_trade_no = out_trade_no;
    }
}
