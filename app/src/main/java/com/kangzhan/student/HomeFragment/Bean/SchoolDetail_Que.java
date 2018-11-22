package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class SchoolDetail_Que {
    private String stu_name;
    private String stu_oss_photo;
    private String inst_name;
    private String create_time;
    private String stu_content;
    private String reply_content;

    public String getStu_name() {
        if(stu_name==null||stu_name==""){
            return "游客";
        }
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_oss_photo() {
        if(stu_oss_photo==null||stu_oss_photo==""){
            return "";
        }
        return stu_oss_photo;
    }

    public void setStu_oss_photo(String stu_oss_photo) {
        this.stu_oss_photo = stu_oss_photo;
    }

    public String getInst_name() {
        if(inst_name==null||inst_name==""){
            return "驾校";
        }
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }



    public String getCreate_time() {
        if(create_time==null||create_time==""){
            return "-----";
        }
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public String getStu_content() {
        if(stu_content==null||stu_content==""){
            return "暂无提问内容";
        }
        return stu_content;
    }

    public void setStu_content(String stu_content) {
        this.stu_content = stu_content;
    }

    public String getReply_content() {
        if(reply_content==null||reply_content==""){
            return "暂无回复内容";
        }
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }
}
