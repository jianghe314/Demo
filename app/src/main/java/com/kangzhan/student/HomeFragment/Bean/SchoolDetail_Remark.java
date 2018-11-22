package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class SchoolDetail_Remark {
    private String stu_name;
    private String teachlevel;
    private String stu_oss_photo;
    private String overall_id;
    private String evaluatetime;

    public String getStu_name() {
        if(stu_name==null||stu_name==""){
            return "游客";
        }
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getTeachlevel() {
        if(teachlevel==null||teachlevel==""){
            return "暂无评价";
        }

        return teachlevel;
    }

    public void setTeachlevel(String teachlevel) {
        this.teachlevel = teachlevel;
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

    public String getOverall_id() {
        if(overall_id==null||overall_id==""){
            return "0";
        }
        return overall_id;
    }

    public void setOverall_id(String overall_id) {
        this.overall_id = overall_id;
    }

    public String getEvaluatetime() {
        if(evaluatetime==null||evaluatetime==""){
            return "-----";
        }
        return evaluatetime;
    }

    public void setEvaluatetime(String evaluatetime) {
        this.evaluatetime = evaluatetime;
    }
}
