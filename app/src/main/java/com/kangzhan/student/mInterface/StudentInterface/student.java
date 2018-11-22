package com.kangzhan.student.mInterface.StudentInterface;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhan011 on 2017/4/17.
 */

public class student {
    public static String studentKey(Context context){
        SharedPreferences sp=context.getSharedPreferences("studentKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    public static String studentName(Context context){
        SharedPreferences sp=context.getSharedPreferences("studentName",Context.MODE_PRIVATE);
        String key=sp.getString("name","");
        return key;
    }
    public static String studentTradeId(Context context){
        SharedPreferences sp=context.getSharedPreferences("Trade_no",Context.MODE_PRIVATE);
        String key=sp.getString("TradeId","");
        return key;
    }


    public static String studentPhotoUrl(Context context){
        SharedPreferences sp=context.getSharedPreferences("studentPhoto",Context.MODE_PRIVATE);
        String key=sp.getString("photo","");
        return key;
    }
    public static String studentId(Context context){
        SharedPreferences sp=context.getSharedPreferences("studentId",Context.MODE_PRIVATE);
        String Id=sp.getString("Id","");
        return Id;
    }
    //学员注册
    public  static String studentRegist(){
        String m="http://app.kzxueche.com/studentapi/Intention/register";
        return m;
    }
    //学员注册验证码辅助接口
    public static String studentRegistHelpCode(){
        String v="http://app.kzxueche.com/studentapi/Intention/code";
        return v;
    }
    //验证码辅助接口
    public static String studentHelpCode(){
        String v="http://app.kzxueche.com/studentapi/Basestu/code";
        return v;
    }

    //学员端修改资料加上传头像
    /**
     * 修改资料
     * studentapi/Basestu/editInfomation
     * post 请求
     * 输入参数
     * file 文件名
     * stu_id 学员id
     * phone 手机号码
     * qqnum QQ号码
     * email 邮箱
     * wechatnum 微信号码
     * home_address 家庭住址
     * 输出参数 json
     * code 返回码
     * msg 返回信息
     */


    public static String studentResistVerify(){
        String v="http://app.kzxueche.com/studentapi/Intention/verify";
        return v;
    }
    public static String studentLoginVerify(){
        String v="http://app.kzxueche.com/studentapi/Basestu/verify";
        return v;
    }
    public static String studentLogin(){
        String v="http://app.kzxueche.com/studentapi/Basestu/login";
        return v;
    }
    public static String studentBookingList(){
        String v="http://app.kzxueche.com/studentapi/Appoint/haveappoint";
        return v;
    }
    public static String studentMsgNotice(){
        String v="http://app.kzxueche.com/api/Message/viewNotifications?receiver_type=10";
        return v;
    }
    public static String studentMsgDetail(){
        String v="http://app.kzxueche.com/api/Message/NotificationsDetail";
        return v;
    }
    public static String studentMsgAdvise(){
        String v="http://app.kzxueche.com/studentapi/Basestu/studentComplaintList";
        return v;
    }
    public static String studentMsgAdviseDetail(){
        String v="http://app.kzxueche.com/studentapi/Basestu/complaintDetail";
        return v;
    }
    public static String studentMydata(){
        String v="http://app.kzxueche.com/studentapi/Basestu/myInformation";
        return v;
    }
    public static String studentMyPurse(){
        String v="http://app.kzxueche.com/studentapi/Basestu/account";
        return v;
    }
    public static String studentMyPurseDetail(){
        String v="http://app.kzxueche.com/studentapi/Basestu/transactionRecord";
        return v;
    }
    public static String studentLearnHour(){
        String v="http://app.kzxueche.com/studentapi/Basestu/hourRecord";
        return v;
    }
    //预约订单的取消
    public static String studentCancelOrder(){
        String v="http://app.kzxueche.com/studentapi/Basestu/cancelReservation";
        return v;
    }
    public static String studentExam(){
        String v="http://app.kzxueche.com/studentapi/Basestu/studentExamscore";
        return v;
    }
    public static String studentTest(){
        String v="http://app.kzxueche.com/studentapi/Basestu/traineeSimulation";
        return v;
    }
    public static String studentBookingOrder(){
        String v="http://app.kzxueche.com/studentapi/Basestu/appointOrder";
        return v;
    }
    public static String studentChangePas(){
        String v="http://app.kzxueche.com/studentapi/Basestu/modifyPassword";
        return v;
    }
    public static String studentPay(){
        String v="http://182.168.0.112/studentapi/Basestu/hourPay";
        return v;
    }
    public static String studentDetail(){
        String v="http://app.kzxueche.com/studentapi/Basestu/hourDetail";
        return v;
    }

    public static String studentEditPersonData(){
        String v="http://app.kzxueche.com/studentapi/Basestu/editInfomation";
        return v;
    }
    public static String studentChoiceTeacher(){
        String v="http://app.kzxueche.com/Institutionapi/Baseinsti/choiceCoach";
        return v;
    }
    public static String studentChoiceSchool(){
        String v="http://app.kzxueche.com/Institutionapi/Baseinsti/choiceSti";
        return v;
    }
    public static String studentSendAdvise(){
        String v="http://app.kzxueche.com/studentapi/Basestu/studentComplain";
        return v;
    }

    //更多教练详
    public static String studentMoreTeacher(){
        String v="http://app.kzxueche.com/studentapi/Appoint/appointcoach";
        return v;
    }
    //模拟成绩取消
    public static String studentTestCancel(){
        String v="http://app.kzxueche.com/studentapi/Basestu/cancelSimulation";
        return v;
    }
    //获取课程
    public static String studentGetLesson(){
        String v="http://app.kzxueche.com/studentapi/Appoint/subject";
        return v;
    }
    public static String studentBookingLesson(){
        String v="http://app.kzxueche.com/studentapi/Appoint/appoint";
        return v;
    }
    public static String studentTrainApply(){
        String v="http://app.kzxueche.com/api/Sparring/practiceApplication";
        return v;
    }
    public static String studentTrainGetTeacher(){
        String v="http://app.kzxueche.com/api/Sparring/getCoachList";
        return v;
    }
    public static String studentTrainOrder(){
        String v="http://app.kzxueche.com/api/Sparring/studentOrderList";
        return v;
    }
    public static String studentTrainCancelOrder(){
        String v="http://app.kzxueche.com/api/Sparring/cancelStudentOrder";
        return v;
    }
    public static String studentTrainRecord(){
        String v="http://app.kzxueche.com/api/Sparring/StudentTrainingRecord";
        return v;
    }

    public static String studentTrainRemark(){
        String v="http://app.kzxueche.com/studentapi/Basestu/classEvaluation";
        return v;
    }
    public static String studentGetTrainRemarkInfo(){
        String v="http://app.kzxueche.com/api/Sparring/StudentTrainingDetail";
        return v;
    }
    public static String studentForgotPsd(){
        String v="http://app.kzxueche.com/studentapi/Basestu/forgotPassword";
        return v;
    }
    public static String getCode(){
        String v="http://app.kzxueche.com/api/Alisms/getcode";
        return v;
    }
    //考试接口,开始考试
    public static String startTest(){
        String v="http://app.kzxueche.com/studentapi/Simulation/startSimulation";
        return v;
    }
    //获取科目一的题目
    public static String startExam(){
        String v="http://app.kzxueche.com/studentapi/Simulation/getQuestion";
        return v;
    }
    //上传成绩接口
    public static String sendGrade(){
        String v="http://app.kzxueche.com/studentapi/Simulation/closeSimulation";
        return v;
    }


    /*  已废
	 * 学员端支付
	 * api/Pay/pay
	 * post请求
	 * 请求参数
	 * key登录秘钥
	 * type 30 余额支付  20微信支付 10支付宝支付
	 * name 订单名称
	 * price 订单价格
	 * order_type 订单类型(10-报名定金; 20-预约; 30-陪练, 40-充值)
	 * out_id 关联id 定金和充值不要传，如果为预约则为预约id，为陪练则为陪练id
	 */

    //多次支付
    /*
	 * 学员端支付
	 * api/Pay/pay
	 * post请求
	 * 请求参数
	 * key登录秘钥
	 * type 20微信支付 10支付宝支付
	 * blanceType 余额支付30 没有选择余额支付则不传
	 * name 订单名称
	 * price 订单价格
	 * order_type 订单类型(10-报名定金; 20-预约; 30-陪练, 40-充值)
	 * out_id 关联id 定金和充值不要传，如果为预约则为预约id，为陪练则为陪练id
	 */

    public static String studentMPay(){
        String v="http://app.kzxueche.com/api/Pay/pay";
        return v;
    }

}
