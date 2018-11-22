package com.kangzhan.student.HomeFragment.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class SchoolDetail {
    private String inst_id;
    private String pic;
    private String insti_name;
    private String summary;
    private String phone;
    private String qq;
    private String region_count;
    private Activities activity;
    private ArrayList<Notice> notice;
    @SerializedName("class")
    private ArrayList<mClass> mclass;

    public String getInst_id() {
        if(inst_id==null){
            return "";
        }
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getPic() {
        if(pic==null){
            return "";
        }
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getInsti_name() {
        if(insti_name==null){
            return "";
        }
        return insti_name;
    }

    public void setInsti_name(String insti_name) {
        this.insti_name = insti_name;
    }

    public String getSummary() {
        if(summary==null){
            return "";
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPhone() {
        if(phone==""||phone==null){
            return "15111364259";
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        if(qq==""||qq==null){
            return "592657010";
        }
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRegion_count() {
        if(region_count==null){
            return "";
        }
        return region_count;
    }

    public void setRegion_count(String region_count) {
        this.region_count = region_count;
    }

    public ArrayList<mClass> getMclass() {
        return mclass;
    }

    public void setMclass(ArrayList<mClass> mclass) {
        this.mclass = mclass;
    }

    public Activities getActivity() {
        if(activity==null){
            return new Activities();
        }
        return activity;
    }

    public void setActivity(Activities activity) {
        this.activity = activity;
    }

    public ArrayList<Notice> getNotice() {
        return notice;
    }

    public void setNotice(ArrayList<Notice> notice) {
        this.notice = notice;
    }
}
