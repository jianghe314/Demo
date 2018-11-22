package com.kangzhan.student.utils.mCalendar;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by kangzhan011 on 2017/5/11.
 */

public class mCalender {
    public static int[]  getCalender(){
        Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year=(c.get(Calendar.YEAR));
        int month=(c.get(Calendar.MONTH)+1);
        int day=(c.get(Calendar.DAY_OF_MONTH));
        int[] mcalender={year,month,day};
        return mcalender;
    }
}
