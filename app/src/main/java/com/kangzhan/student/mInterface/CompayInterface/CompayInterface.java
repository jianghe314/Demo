package com.kangzhan.student.mInterface.CompayInterface;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhan011 on 2017/7/24.
 */

public class CompayInterface {

    public static String CompayKey(Context context){
        SharedPreferences sp=context.getSharedPreferences("compayKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    public static String CompayId(Context context){
        SharedPreferences sp=context.getSharedPreferences("compayId",Context.MODE_PRIVATE);
        String key=sp.getString("Id","");
        return key;
    }


    public static String CompayLogin(){
        String v="http://app.kzxueche.com/clerkapi/Login/login";
        return v;
    }
    public static String LoginCode(){
        String v="http://app.kzxueche.com/clerkapi/Login/verify";
        return v;
    }
    public static String CodeHelp(){
        String v="http://app.kzxueche.com/clerkapi/Login/code";
        return v;
    }
    //侧拉菜单
    public static String SchoolAccountInfo(){
        String v="http://app.kzxueche.com/clerkapi/Login/instiMgr";
        return v;
    }
    public static String SchoolChangeStatus(){
        String v="http://app.kzxueche.com/clerkapi/Login/changeMsgState";
        return v;
    }
    public static String StaffManage(){
        String v="http://app.kzxueche.com/clerkapi/Login/platformClerk";
        return v;
    }
    public static String StaffChangePsd(){
        String v="http://app.kzxueche.com/clerkapi/Login/modifyClerkPassword";
        return v;
    }
    public static String CompayChangePsd(){
        String v="http://app.kzxueche.com/clerkapi/Login/modifyPassword";
        return v;
    }

    public static String SelfRegStu(){
        String v="http://app.kzxueche.com/clerkapi/Login/intentList";
        return v;
    }
    public static String SelfRegStuDele(){
        String v="http://app.kzxueche.com/clerkapi/Login/delIntention";
        return v;
    }
    //选择业务员
    public static String ChoiceClerk(){
        String v="http://app.kzxueche.com/clerkapi/Login/selectClerk";
        return v;
    }
    //分配业务员
    public static String AllocateClerk(){
        String v="http://app.kzxueche.com/clerkapi/Login/distributionClerk";
        return v;
    }

    public static String SelfStuDetail(){
        String v="http://app.kzxueche.com/clerkapi/Login/intentDetail";
        return v;
    }
    //查看跟进情况 http://app.kzxueche.com/clerkapi/Contact/studentInfo?key=2kzc150087577407832020&inten_stu_id=164
    public static String SelfStuInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/studentInfo";
        return v;
    }
    //添加跟进情况
    public static String AddFollowUpInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/student";
        return v;
    }
    //教练查看跟进情况
    public static String SelfTeaInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/coachInfo";
        return v;
    }
    //驾校查看情况
    public static String SelfSchoolInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/institutionInfo";
        return v;
    }
    //教练添加跟进情况
    public static String AddTeaFollowUpInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/coach";
        return v;
    }
    //驾校添加跟进情况
    public static String AddSchoolFollowUpInfo(){
        String v="http://app.kzxueche.com/clerkapi/Contact/institution";
        return v;
    }
    public static String AdviseManage(){
        String v="http://app.kzxueche.com/clerkapi/Login/ticket";
        return v;
    }
    public static String AdviseDelet(){
        String v="http://app.kzxueche.com/clerkapi/Login/delTicket";
        return v;
    }
    public static String AdviseDetail(){
        String v="http://app.kzxueche.com/clerkapi/Login/ticketDetail";
        return v;
    }
    //工单回复
    public static String AdviseReplay(){
        String v="http://app.kzxueche.com/clerkapi/Login/ticketReply";
        return v;
    }
    public static String TestGrade(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/score";
        return v;
    }
    public static String BookingOrder(){
        String v="http://app.kzxueche.com/clerkapi/Login/appoint";
        return v;
    }
    public static String TrainingOrder(){
        String v="http://app.kzxueche.com/api/Sparring/instSparringTask";
        return v;
    }
    public static String TrainingRecord(){
        String v="http://app.kzxueche.com/api/Sparring/instSparring";
        return v;
    }
    public static String LearnHourRecord(){
        String v="http://app.kzxueche.com/clerkapi/Login/classrecord";
        return v;
    }
    public static String LearnHourSyn(){
        String v="http://app.kzxueche.com/api/Synchronization/Synchron";
        return v;
    }
    public static String LearnHourDetail(){
        String v="http://app.kzxueche.com/clerkapi/Login/classrecordDetail";
        return v;
    }
    public static String EvalueRecord(){
        String v="http://app.kzxueche.com/clerkapi/Login/evaluation";
        return v;
    }
    public static String RemarkRecord(){
        String v="http://app.kzxueche.com/clerkapi/Login/suggestion";
        return v;
    }
    public static String InfoStuManage(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/studentList";
        return v;
    }
    public static String InfoStuDele(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/deleteStudent";
        return v;
    }
    public static String InfoTeaManage(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/coachList";
        return v;
    }
    public static String InfoTeaDele(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/deleteCoach";
        return v;
    }
    public static String InfoSchoolManage(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/institution";
        return v;
    }
    public static String InfoSchoolDetail(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/institutionDetail";
        return v;
    }
    public static String InfoSchoolAlloca(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/instiDistriClerk";
        return v;
    }
    public static String InfoCarManage(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/carList";
        return v;
    }
    public static String InfoCarMDele(){
        String v="http://app.kzxueche.com/clerkapi/Teaching/deleteCar";
        return v;
    }
    //账务
    public static String AccountSchoolList(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceSti";
        return v;
    }
    public static String AccountSchoolCheck(){
        String v="http://app.kzxueche.com/clerkapi/Login/instistatement";
        return v;
    }
    //驾校账单详情
    public static String AccountSchoolDetail(){
        String v="http://app.kzxueche.com/clerkapi/Login/instistatementDetail";
        return v;
    }
    //短信结算
    public static String AccountMsgCheck(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceSti";
        return v;
    }
    public static String AccountMsgList(){
        String v="http://app.kzxueche.com/clerkapi/Login/smsbill";
        return v;
    }
    //短信结算
    public static String AccountMsgSend(){
        String v="http://app.kzxueche.com/clerkapi/Login/sendSmsBill";
        return v;
    }
    //账单结算
    public static String AccountBillSend(){
        String v="http://app.kzxueche.com/clerkapi/Login/sendBill";
        return v;
    }
    public static String AccountMsgDetail(){
        String v="http://app.kzxueche.com/clerkapi/Login/instSmsBillDetail";
        return v;
    }
    //通知列表
    public static String CompayNoticeList(){
        String v="http://app.kzxueche.com/clerkapi/Login/pushBatch";
        return v;
    }
    //通知删除
    public static String CompayNoticeDele(){
        String v="http://app.kzxueche.com/api/Message/deletePush";
        return v;
    }
    public static String CompayNoticeAdd(){
        String v="http://app.kzxueche.com/api/Message/addNotifications";
        return v;
    }

    //通知详情
    public static String CompayNoticeDetail(){
        String v="http://app.kzxueche.com/api/Message/showNote";
        return v;
    }
    //短信管理列表
    public static String CompayMsgList(){
        String v="http://app.kzxueche.com/clerkapi/Login/querySms";
        return v;
    }
    //短信删除
    public static String CompayMsgDele(){
        String v="http://app.kzxueche.com/api/Sms/deleteSms";
        return v;
    }
    //短信详情
    public static String CompayMsgDetail(){
        String v="http://app.kzxueche.com/api/Sms/showSms";
        return v;
    }
    public static String CompayMsgAdd(){
        String v="http://app.kzxueche.com/api/Sms/addSms";
        return v;
    }
    public static String CompayChoiceSchool(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceSti";
        return v;
    }
    public static String CompayChoiceTeacher(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceCoach";
        return v;
    }
    public static String CompayChoiceStudent(){
        String v="http://app.kzxueche.com/studentapi/Basestu/choiceStudent";
        return v;
    }











}
