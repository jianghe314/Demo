package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/27.
 */

public class Notice {
    private String title;
    private String id;

    public String getTitle() {
        if(title==null||title==""){
            return "暂无公告";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
