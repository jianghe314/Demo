package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class NewPublish {
    private String consult_id;
    private String info_title;

    public String getConsult_id() {
        if(consult_id==null){
            return "";
        }
        return consult_id;
    }

    public void setConsult_id(String consult_id) {
        this.consult_id = consult_id;
    }

    public String getInfo_title() {
        if(info_title==null){
            return "";
        }
        return info_title;
    }

    public void setInfo_title(String info_title) {
        this.info_title = info_title;
    }
}
