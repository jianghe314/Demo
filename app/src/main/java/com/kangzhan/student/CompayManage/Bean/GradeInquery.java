package com.kangzhan.student.CompayManage.Bean;

/**
 * Created by kangzhan011 on 2017/8/2.
 */

public class GradeInquery {
    private String stu_id;
    private String id;
    private String name;
    private String stage;
    private String qualified;
    private String instname;
    private boolean isClick=false;
    private boolean isShow=false;

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getQualified() {
        return qualified;
    }

    public void setQualified(String qualified) {
        this.qualified = qualified;
    }

    public String getInstname() {
        return instname;
    }

    public void setInstname(String instname) {
        this.instname = instname;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
