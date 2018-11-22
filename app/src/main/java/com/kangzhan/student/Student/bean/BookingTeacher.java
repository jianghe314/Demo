package com.kangzhan.student.Student.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kangzhan011 on 2017/3/28.
 */

public class BookingTeacher {
    private String id;
    private String name;
    private int sex;
    private String mobile;
    private String licnum;
    private String institutionname;
    private String score;
    private String oss_photo;
    private String brand;
    private String address;
    private String experience_years;
    private String latitude;
    private String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLicnum() {
        return licnum;
    }

    public void setLicnum(String licnum) {
        this.licnum = licnum;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOss_photo() {
        return oss_photo;
    }

    public void setOss_photo(String oss_photo) {
        this.oss_photo = oss_photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExperience_years() {
        return experience_years;
    }

    public void setExperience_years(String experience_years) {
        this.experience_years = experience_years;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
