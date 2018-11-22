package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class mClass {
    private String name;
    private String summary;
    private String price;
    private String id;

    public String getName() {
        if(name==null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        if(summary==null){
            return "直通驾考，优质服务";
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        if(price==null){
            return "暂无";
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
