package com.kangzhan.student.School.Bean;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class EduRemark {
    private String id;
    private String stu_id;
    private String stuname;
    private String instname;
    private String phone;
    private String coach_id;
    private String coaname;
    private String clerk_id;
    private String mobile;
    private String create_time;
    private String content;
    private String subject_content;
    private String source;
    private boolean isClick=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getInstname() {
        return instname;
    }

    public void setInstname(String instname) {
        this.instname = instname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public String getCoaname() {
        return coaname;
    }

    public void setCoaname(String coaname) {
        this.coaname = coaname;
    }

    public String getClerk_id() {
        return clerk_id;
    }

    public void setClerk_id(String clerk_id) {
        this.clerk_id = clerk_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject_content() {
        return subject_content;
    }

    public void setSubject_content(String subject_content) {
        this.subject_content = subject_content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
