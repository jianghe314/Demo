package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class InfoCarManage {
    private String id;
    private String inst_id;
    private String licnum;
    private String franum;
    private String engnum;
    private String brand;
    private String coach_name;
    private String region_name;
    private String devnum;
    private String buydate;
    private String perdritype;
    private String status;
    private boolean isShow=false;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getLicnum() {
        return licnum;
    }

    public void setLicnum(String licnum) {
        this.licnum = licnum;
    }

    public String getFranum() {
        return franum;
    }

    public void setFranum(String franum) {
        this.franum = franum;
    }

    public String getEngnum() {
        return engnum;
    }

    public void setEngnum(String engnum) {
        this.engnum = engnum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public void setCoach_name(String coach_name) {
        this.coach_name = coach_name;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }

    public String getBuydate() {
        return buydate;
    }

    public void setBuydate(String buydate) {
        this.buydate = buydate;
    }

    public String getPerdritype() {
        return perdritype;
    }

    public void setPerdritype(String perdritype) {
        this.perdritype = perdritype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
