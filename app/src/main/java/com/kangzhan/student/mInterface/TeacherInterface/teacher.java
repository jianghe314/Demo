package com.kangzhan.student.mInterface.TeacherInterface;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhan011 on 2017/4/25.
 */

public class teacher {
    public static String teacherKey(Context context){
        SharedPreferences sp=context.getSharedPreferences("teacherKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    public static String teacherName(Context context){
        SharedPreferences sp=context.getSharedPreferences("teacherName",Context.MODE_PRIVATE);
        String key=sp.getString("name","");
        return key;
    }
    public static String teacherPhoto(Context context){
        SharedPreferences sp=context.getSharedPreferences("teacherPhoto",Context.MODE_PRIVATE);
        String key=sp.getString("photo","");
        return key;
    }
    public static String teacherId(Context context){
        SharedPreferences sp=context.getSharedPreferences("teacherId",Context.MODE_PRIVATE);
        String id=sp.getString("id","");
        return id;
    }
    public static String teacherAttach(Context context){
        SharedPreferences sp=context.getSharedPreferences("teacherAttach",Context.MODE_PRIVATE);
        String id=sp.getString("Attach","");
        return id;
    }

    static String m="http://app.kzxueche.com/";
    //教练注册
    public static String teacherReigst(){
        String v="http://app.kzxueche.com/coachapi/Intention/register";
        return v;
    }
    //教练注册验证码辅助接口
    public static String teacherReigstHelpCode(){
        String v="http://app.kzxueche.com/coachapi/Intention/code";
        return v;
    }
    //教练验证码
    public static String teacherReigstCode(){
        String v="http://app.kzxueche.com/coachapi/Intention/verify";
        return v;
    }
    //验证码辅助接口
    public static String teacherCodeHelp(){
        String v="http://app.kzxueche.com/coachapi/Basecoa/code";
        return v;
    }
    public static String teacherLogin(){
        String v="http://app.kzxueche.com/coachapi/Basecoa/login";
        return v;
    }
    public static String teacherLoginverify(){
        String v="http://app.kzxueche.com/coachapi/Basecoa/verify";
        return v;
    }
    public static String teacherNewsCheckNotice(){
        String v="http://app.kzxueche.com/api/Message/viewNotifications";
        return v;
    }
    public static String teacherNewsCheckNoticeDetail(){
        String v="http://app.kzxueche.com/api/Message/NotificationsDetail";
        return v;
    }
    public static String teacherNewsAdvise(){
        String v="http://app.kzxueche.com/api/Ticket/ticketList";
        return v;
    }
    public static String teacherAdviseDetail(){
        String v="http://app.kzxueche.com/api/Ticket/ticketDetail";
        return v;
    }
    public static String teacherChoiceStu(){
        String v="http://app.kzxueche.com/studentapi/Basestu/choiceStudent";
        return v;
    }
    public static String teacherMydata(){
        String v="http://app.kzxueche.com/coachapi/Coach/myInfo";
        return v;
    }
    public static String teacherChangeData(){
        String v="http://app.kzxueche.com/coachapi/Coach/mofifyInfo";
        return v;
    }
    public static String teacherMyStudent(){
        String v="http://app.kzxueche.com/coachapi/Coach/myStudent";
        return v;
    }
    //教练记录评价
    public static String teacherRecordRemark(){
        String v="http://app.kzxueche.com//api/Sparring/suggestionstudent";
        return v;
    }
    public static String teacherChangePwd(){
        String v="http://app.kzxueche.com/coachapi/Coach/modifyPassword";
        return v;
    }
    public static String teacherAddAdvise(){
        String v="http://app.kzxueche.com/api/Ticket/newTicket";
        return v;
    }
    public static String teacherAddNotice(){
        String v="http://app.kzxueche.com/api/Message/addNotifications";
        return v;
    }
    public static String teacherAddMsg(){
        String v="http://app.kzxueche.com/api/Sms/addSms";
        return v;
    }
    public static String teacherPurse(){
        String v="http://app.kzxueche.com/coachapi/Coach/account";
        return v;
    }
    public static String teacherPurseDetail(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/transactionRecord";
        return v;
    }

    public static String  teacherReward(){
        String v="http://app.kzxueche.com/coachapi/Coach/myBonus";
        return v;
    }
    public static String teacherRewardDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/Bonusdetail";
        return v;
    }
    public static String teacherBooking1(){
        String v="http://app.kzxueche.com/coachapi/Coach/myAppoint";
        return v;
    }
    public static String teacherHasTrain(){
        String v="http://app.kzxueche.com/coachapi/Coach/myclassRecord";
        return v;
    }
    public static String teacherStartTrain2DCode(){
        String v="http://app.kzxueche.com/studentapi/Appoint/startTrainqrcode";
        return v;
    }
    public static String teacherEndTrain2DCode(){
        String v="http://app.kzxueche.com/studentapi/Appoint/endTrainqrcode";
        return v;
    }
    public static String teacherAbsence(){
        String v="http://app.kzxueche.com/studentapi/Appoint/absence";
        return v;
    }
    //学员详情
    public static String teacherStudentDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/stDetail";
        return v;
    }
    //显示单个学员信息内容
    public static String teacherShowStuInfo(){
        String v="http://app.kzxueche.com/coachapi/Coach/showGestion";
        return v;
    }
    //预约点评接口
    public static String teacherSuggestion(){
        String v="http://app.kzxueche.com/coachapi/Coach/Gestion";
        return v;
    }
    //发表点评内容
    public static String teacherUpdateRemark(){
        String v="http://app.kzxueche.com/api/Sparring/coachtSuggestion";
        return v;
    }
    public static String teacherMsgBill(){
        String v="http://app.kzxueche.com/coachapi/Coach/mySmsbill";
        return v;
    }
    public static String teacherMsgBillDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/smsbillDetail";
        return v;
    }
    public static String teacherMsgConfirmBill(){
        String v="http://app.kzxueche.com/coachapi/Coach/confirmSmsbill";
        return v;
    }
    public static String teacherBill(){
        String v="http://app.kzxueche.com/coachapi/Coach/myStatement";
        return v;
    }

    public static String teacherBillDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/statementDetail";
        return v;
    }
    public static String teacherConfirmBillDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/confirmStatement";
        return v;
    }
    public static String teacherMyLesson(){
        String v="http://app.kzxueche.com/coachapi/Coach/mySubject";
        return v;
    }
    //传入要调休的传入参数subject_ids  要调休的记录
    public static String teacherHaveRest(){
        String v="http://app.kzxueche.com/coachapi/Coach/seftRest";
        return v;
    }
    public static String teacherCancelRest(){
        String v="http://app.kzxueche.com/coachapi/Coach/cancelRest";
        return v;
    }
    //参数 key  obj
    public static String teacherChangeLesson(){
        String v="http://app.kzxueche.com/coachapi/Coach/modifySubject";
        return v;
    }
    public static String teacherGetOrder(){
        String v="http://app.kzxueche.com/api/Sparring/coachgrabSparring";
        return v;
    }
    public static String teacherHasOrder(){
        String v="http://app.kzxueche.com/api/Sparring/coachGetOrder";
        return v;
    }
    public static String teacherOrder(){
        String v="http://app.kzxueche.com/api/Sparring/coachOrder";
        return v;
    }
    public static String teacherTrainStart2DCode(){
        String v="http://app.kzxueche.com/api/Sparring/startTrainqrcode";
        return v;
    }
    public static String teacherTrainAbsence2DCode(){
        String v="http://app.kzxueche.com/api/Sparring/absence";
        return v;
    }
    public static String teacherTrainRecord(){
        String v="http://app.kzxueche.com/api/Sparring/coachTrainingRecord";
        return v;
    }
    public static String teacherTrainEnd2DCode(){
        String v="http://app.kzxueche.com/api/Sparring/endTrainqrcode";
        return v;
    }
    public static String teacherPay(){
        String v="http://app.kzxueche.com/api/Recharge/recharge";
        return v;
    }
    /*关闭工单
    api/Ticket/closeTicket
    post 请求
    输入参数
    ticketid 工单ID
    close_type 回复人10-学员; 20-教练; 30-驾校管理人员;40 运管人员 50业务员
    key 登录者KEY
    输出JSON
    code 返回代码200 表示回复成功
    msg  返回消息 如 系统繁忙，回复失败*/
    public static String teacherCloseAdvise(){
        String v="http://app.kzxueche.com/api/Ticket/closeTicket";
        return v;
    }
}
