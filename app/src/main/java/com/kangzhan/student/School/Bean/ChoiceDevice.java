package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/8/8.
 */

public class ChoiceDevice {
    private String id;
    private String devnum;
    private String termtype;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevnum() {
        return devnum;
    }

    public void setDevnum(String devnum) {
        this.devnum = devnum;
    }

    public String getTermtype() {
        return termtype;
    }

    public void setTermtype(String termtype) {
        this.termtype = termtype;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
