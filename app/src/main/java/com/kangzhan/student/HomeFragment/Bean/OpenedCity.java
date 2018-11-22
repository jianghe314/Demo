package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/24.
 */

public class OpenedCity {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name==null||name.equals("")){
            return "暂无";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
