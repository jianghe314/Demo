package com.kangzhan.student.HomeFragment.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class TeacherDetail {
    private String id;
    private String name;
    private String oss_photo;
    private String self_description;
    private String inst_name;
    private String mobile;
    private String inst_id;
    private String qqnum;
    private String region_count;
    @SerializedName("class")
    private ArrayList<mClass> classes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name==null||name.equals("")){
            return "xxx";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOss_photo() {
        if(oss_photo==null){
            return "";
        }
        return oss_photo;
    }

    public void setOss_photo(String oss_photo) {
        this.oss_photo = oss_photo;
    }

    public String getSelf_description() {
        if(self_description==null||self_description.equals("")){
            return "暂无教练简介";
        }
        return self_description;
    }

    public void setSelf_description(String self_description) {
        this.self_description = self_description;
    }

    public String getInst_name() {
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getMobile() {
        if(mobile==null||mobile.equals("")){
            return "15111364259";
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getQqnum() {
        if(qqnum==null||qqnum.equals("")){
            return "592657010";
        }
        return qqnum;
    }

    public void setQqnum(String qqnum) {
        this.qqnum = qqnum;
    }

    public String getRegion_count() {
        if(region_count==null||region_count.equals("")){
            return "0";
        }
        return region_count;
    }

    public void setRegion_count(String region_count) {
        this.region_count = region_count;
    }

    public ArrayList<mClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<mClass> classes) {
        this.classes = classes;
    }
}
