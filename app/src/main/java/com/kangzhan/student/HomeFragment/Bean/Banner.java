package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/27.
 */

public class Banner {
    private String id;
    private String name;
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if(name==null||name==""){
            return "暂无标题";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        if(name==null||name==""){
            return "";
        }
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
