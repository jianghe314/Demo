package com.kangzhan.student.HomeFragment.Bean;


/**
 * Created by kangzhan011 on 2017/11/21.
 */

public class SchoolList {
    private String inst_id;
    private String shortname;
    private String logo;
    private String eval_count;
    private String score_eval;
    private String insti_name;
    private String distance;
    private String region_count;

    public String getInst_id() {
        if(inst_id==null){
            return "";
        }
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
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

    public String getLogo() {
        if(logo==null){
            return "";
        }
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getInsti_name() {
        if(insti_name==null){
            return "";
        }
        return insti_name;
    }

    public void setInsti_name(String insti_name) {
        this.insti_name = insti_name;
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

    public String getRegion_count() {
        if(region_count==null){
            return "";
        }
        return region_count;
    }

    public void setRegion_count(String region_count) {
        this.region_count = region_count;
    }
}
