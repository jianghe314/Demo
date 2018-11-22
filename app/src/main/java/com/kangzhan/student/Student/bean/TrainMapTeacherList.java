package com.kangzhan.student.Student.bean;

/**
 * Created by kangzhan011 on 2017/5/15.
 */

public class TrainMapTeacherList {
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String sex;
    private String experience_years;
    private double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getExperience_years() {
        return experience_years;
    }

    public void setExperience_years(String experience_years) {
        this.experience_years = experience_years;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
