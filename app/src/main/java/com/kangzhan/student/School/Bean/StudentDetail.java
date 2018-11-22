package com.kangzhan.student.School.Bean;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/13.
 */

public class StudentDetail {
    private EduStudentInfo studentInfo;
    private ArrayList<EduSimulationScoreInfo> simulationscoreInfo;
    private ArrayList<EduExamScoreInfo> examscoreInfo;
    private ArrayList<EduAppointInfo> appointInfo;
    private ArrayList<EduSparringInfo> sparringInfo;
    private ArrayList<EduClassRecordInfo> classrecordInfo;
    private ArrayList<EduEvaluationInfo> evaluationInfo;
    private ArrayList<EduSuggestionInfo> suggestionInfo;
    private ArrayList<EduComplaintInfo> complaintInfo;
    private ArrayList<EduOrderInfo> orderInfo;

    public EduStudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(EduStudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public ArrayList<EduSimulationScoreInfo> getSimulationscoreInfo() {
        return simulationscoreInfo;
    }

    public void setSimulationscoreInfo(ArrayList<EduSimulationScoreInfo> simulationscoreInfo) {
        this.simulationscoreInfo = simulationscoreInfo;
    }

    public ArrayList<EduExamScoreInfo> getExamscoreInfo() {
        return examscoreInfo;
    }

    public void setExamscoreInfo(ArrayList<EduExamScoreInfo> examscoreInfo) {
        this.examscoreInfo = examscoreInfo;
    }

    public ArrayList<EduAppointInfo> getAppointInfo() {
        return appointInfo;
    }

    public void setAppointInfo(ArrayList<EduAppointInfo> appointInfo) {
        this.appointInfo = appointInfo;
    }

    public ArrayList<EduSparringInfo> getSparringInfo() {
        return sparringInfo;
    }

    public void setSparringInfo(ArrayList<EduSparringInfo> sparringInfo) {
        this.sparringInfo = sparringInfo;
    }

    public ArrayList<EduClassRecordInfo> getClassrecordInfo() {
        return classrecordInfo;
    }

    public void setClassrecordInfo(ArrayList<EduClassRecordInfo> classrecordInfo) {
        this.classrecordInfo = classrecordInfo;
    }

    public ArrayList<EduEvaluationInfo> getEvaluationInfo() {
        return evaluationInfo;
    }

    public void setEvaluationInfo(ArrayList<EduEvaluationInfo> evaluationInfo) {
        this.evaluationInfo = evaluationInfo;
    }

    public ArrayList<EduSuggestionInfo> getSuggestionInfo() {
        return suggestionInfo;
    }

    public void setSuggestionInfo(ArrayList<EduSuggestionInfo> suggestionInfo) {
        this.suggestionInfo = suggestionInfo;
    }

    public ArrayList<EduComplaintInfo> getComplaintInfo() {
        return complaintInfo;
    }

    public void setComplaintInfo(ArrayList<EduComplaintInfo> complaintInfo) {
        this.complaintInfo = complaintInfo;
    }

    public ArrayList<EduOrderInfo> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(ArrayList<EduOrderInfo> orderInfo) {
        this.orderInfo = orderInfo;
    }
}
