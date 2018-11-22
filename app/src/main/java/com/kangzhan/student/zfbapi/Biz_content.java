package com.kangzhan.student.zfbapi;

/**
 * Created by kangzhan011 on 2017/6/19.
 */

public class Biz_content {
    private String body;
    private String subject;
    private String out_trade_no;
    private String timeout_express;
    private String total_amount;
    private String product_code;

    @Override
    public String toString() {
        return "Biz_content[body="+body+",subject="+subject+",out_trade_no="+out_trade_no+",timeout_express="+timeout_express+",total="+total_amount+",product_code="+product_code+"]";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
