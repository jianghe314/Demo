package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class Activities {
    private String title;
    private String id;

    public String getTitle() {
        if(title==null||title==""){
            return "暂无活动";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        if(id==null){
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
