package com.kangzhan.student.HomeFragment.Bean;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class TeacherList {
    private String id;
    private String shortname;
    private String inst_id;
    private String name;
    private String oss_photo;
    private String eval_count;
    private String score_eval;
    private String inst_name;
    private String distance;

    public String getId() {
        if(id==null){
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortname() {
        if(shortname==null){
            return "";
        }
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getInst_id() {
        if(inst_id==null){
            return "";
        }
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getName() {
        if(name==null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOss_photo() {
        if(oss_photo==null){
            return "";
        }
        return oss_photo;
    }

    public void setOss_photo(String oss_photo) {
        this.oss_photo = oss_photo;
    }

    public String getEval_count() {
        if(eval_count==null){
            return "";
        }
        return eval_count;
    }

    public void setEval_count(String eval_count) {
        this.eval_count = eval_count;
    }

    public String getScore_eval() {
        if(score_eval==null){
            return "";
        }
        return score_eval;
    }

    public void setScore_eval(String score_eval) {
        this.score_eval = score_eval;
    }

    public String getInst_name() {
        if(inst_name==null){
            return "";
        }
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getDistance() {
        if(distance==null){
            return "";
        }
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
