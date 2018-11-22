package com.kangzhan.student.mInterface.SchoolInterface;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhan011 on 2017/6/14.
 */

public class school {
    public static String schoolKey(Context context){
        SharedPreferences sp=context.getSharedPreferences("schoolKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    public static String schoolName(Context context){
        SharedPreferences sp=context.getSharedPreferences("schoolName",Context.MODE_PRIVATE);
        String key=sp.getString("name","");
        return key;
    }
    //忘记密码
    /*
   * 忘记密码
   * studentapi/Basestu/forgotPassword
   * post 请求
   * 请求参数
   * type 1学员 2教练 3驾校
   * phone 手机号
   * code 验证码
   * 输出json
   */

    public static String schoolForgetPsd(){
        String v="http://app.kzxueche.com/studentapi/Basestu/forgotPassword";
        return v;
    }
    //驾校注册验证码
    public static String schoolRegistCode(){
        String v="http://app.kzxueche.com/institutionapi/Intention/verify";
        return v;
    }

    public static String schoolLogin(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/login";
        return v;
    }
    //驾校注册辅助codeId
    public static String schoolRegistHelpCode(){
        String v="http://app.kzxueche.com/institutionapi/Intention/code";
        return v;
    }
    //驾校注册
    public static String schoolRegist(){
        String v="http://app.kzxueche.com/institutionapi/Intention/register";
        return v;
    }

    //获取codeId
    public static String schoolCodeId(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/code";
        return v;
    }
    //验证码
    public static String LoginCode(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/verify";
        return v;
    }

    //侧拉
    public static String AccountMange(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/mgrList";
        return v;
    }
    public static String AccountProhibit(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/stopMgr";
        return v;
    }
    public static String AccountAdd(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/addMgr";
        return v;
    }
    public static String UnitInfo(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/instDetail";
        return v;
    }
    public static String ModifUnitInfo(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/modifyInstinfo";
        return v;
    }
    public static String ModifPsd(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/modifyPassword";
        return v;
    }

    //教务管理-学员管理
    public static String EduStuManage(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/studentList";
        return v;
    }
    public static String EduStMdele(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/deleteStudent";
        return v;
    }
    //分配教练
    public static String EduAllocaTeacher(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/setCoach";
        return v;
    }
    //班别设置
    public static String EduSetClass(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/setClass";
        return v;
    }
    //分配教练-选择教练
    public static String EduStmChocieTeac(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceCoach";
        return v;
    }
    //班别设置
    public static String EduShowClass(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/instClass";
        return v;
    }
    //教练管理-教练管理
    public static String EduTeaManage(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/coachList";
        return v;
    }
    public static String EduTeaManageDele(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/deleteCoach";
        return v;
    }
    public static String EduTeaMShowLesson(){
        String v="http://app.kzxueche.com/institutionapi/Institution/getSubjectTemplate";
        return v;
    }
    public static String EduTeaMSetLesson(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/setSubject";
        return v;
    }
    public static String EduTeacherDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/coachInfodetail";
        return v;
    }
    public static String EduTeacherRest(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/addVacation";
        return v;
    }

   public static String EduTeaherRestList(){
       String v="http://app.kzxueche.com/institutionapi/Teaching/coachVacation";
       return v;
   }
   public static String EduCancelRest(){
       String v="http://app.kzxueche.com/institutionapi/Teaching/cancelVacation";
       return v;
   }
   //教练车管理
    public static String EduCarManage(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/carList";
        return v;
    }
    public static String EduCarManage_Dele(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/deleteCar";
        return v;
    }
    //选择训练场
    public static String EduChoiceRegion(){
        String v="http://app.kzxueche.com/institutionapi/Institution/getRegion";
        return v;
    }
    //选择计时终端
    public static String EduChoiceDeviceId(){
        String v="http://app.kzxueche.com/institutionapi/Institution/getDevice";
        return v;
    }
    //选择车型
    public static String  EduChoiceCarType(){
        String v="http://app.kzxueche.com/institutionapi/Institution/getVehtype";
        return v;
    }
    //新增教练车
    public static String  EduAddCar(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/addCar";
        return v;
    }
    public static String EduTestList(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/scoreList";
        return v;
    }
    public static String EduTestDele(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/deleteScore";
        return v;
    }
    public static String EduLearnHour(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/recordList";
        return v;
    }
    //学员详情
    public static String EduStudentDetail(){
        String v="http://app.kzxueche.com/coachapi/Coach/studentDetail";
        return v;
    }

    public static String EduEvaluList(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/evaluationlist";
        return v;
    }
    public static String EduRemarkList(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/suggestionlist";
        return v;
    }
    //课程管理
    public static String LessonManage(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/subjectTemplate";
        return v;
    }
    public static String LessonDetail(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/showTemplate";
        return v;
    }
    public static String BookingOrderList(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/appointList";
        return v;
    }
    public static String BookingClassList(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/classList";
        return v;
    }
    public static String TrainingRecordList(){
        String v="http://app.kzxueche.com/api/Sparring/instSparring";
        return v;
    }
    public static String TrainingOrderList(){
        String v="http://app.kzxueche.com/api/Sparring/instSparringTask";
        return v;
    }
    public static String AccountAffirm(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/statementList";
        return v;
    }
    public static String AccountAffirmDetail(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/statementDetail";
        return v;
    }
    public static String AccountAffirmDetailConfirm(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/confirmStatement";
        return v;
    }
    public static String AccountMsg(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/smsbillList";
        return v;
    }
    public static String AccountMsgDetail(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/smsbillDetail";
        return v;
    }
    public static String AccountMsgDetailConfirm(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/confirmSmsbill";
        return v;
    }
    public static String AccountQulifiedSet(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/addbonusStandard";
        return v;
    }
    public static String AccountRewardManage(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/bonusList";
        return v;
    }
    public static String AccountRewardDetail(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/bonusDetail";
        return v;
    }
    public static String SchoolNotice(){
        String v="http://app.kzxueche.com/institutionapi/Teaching/noteList";
        return v;
    }
    public static String SchoolNoticeDele(){
        String v="http://app.kzxueche.com/api/Message/deletePush";
        return v;
    }
    public static String SchoolNoticeDetail(){
        String v="http://app.kzxueche.com/api/Message/showNote";
        return v;
    }
    public static String SchoolNoticeAdd(){
        String v="http://app.kzxueche.com/api/Message/addNotifications";
        return v;
    }
    public static String SchoolNoticeChoiceStu(){
        String v="http://app.kzxueche.com/studentapi/Basestu/choiceStudent";
        return v;
    }
    public static String SchoolNoticeChoiceTea(){
        String v="http://app.kzxueche.com/institutionapi/Baseinsti/choiceCoach";
        return v;
    }
    public static String SchoolAdviseList(){
        String v="http://app.kzxueche.com/api/Ticket/ticketList";
        return v;
    }
    public static String SchoolAdviseDetail(){
        String v="http://app.kzxueche.com/api/Ticket/ticketDetail";
        return v;
    }
    public static String SchoolAdviseClose(){
        String v="http://app.kzxueche.com/api/Ticket/closeTicket";
        return v;
    }
    public static String SchoolAdviseListDele(){
        String v="http://app.kzxueche.com/api/Ticket/deleteTicket";
        return v;
    }
    public static String SchoolAdviseAdd(){
        String v="http://app.kzxueche.com/api/Ticket/newTicket";
        return v;
    }
    public static String SchoolMsgList(){
        String v="http://app.kzxueche.com/api/Sms/viewSms";
        return v;
    }
    public static String SchoolMsgListDele(){
        String v="http://app.kzxueche.com/api/Sms/deleteSms";
        return v;
    }
    public static String SchoolMsgDeatil(){
        String v="http://app.kzxueche.com/api/Sms/showSms";
        return v;
    }
    public static String SchoolMsgSend(){
        String v="http://app.kzxueche.com/api/Sms/addSms";
        return v;
    }







}
