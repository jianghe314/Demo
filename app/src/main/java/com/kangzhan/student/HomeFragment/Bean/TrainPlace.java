package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/24.
 */

public class TrainPlace {
    private String  name;
    private String  address;
    private String  latitude;
    private String  longitude;
    private String  distance;

    public String getName() {
        if(name==null||name.equals("")){
            return "暂无名称";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        if(address==null||address.equals("")){
            return "地址暂未上传";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        if(latitude==null||latitude.equals("")){
            return "";
        }
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        if(longitude==null||longitude.equals("")){
            return "";
        }
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        if(distance==null||distance.equals("")){
            return "0.000";
        }
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
