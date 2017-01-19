package com.taek_aaa.goalsnowball.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class CalendarDatas {
    public Calendar today;
    public int cYear, hMonth, cMonth, cdate, dayOfWeekIndex;
    public int hour, minute, seconds;
    public static String[] dayOfWeekArray = {"", "일", "월", "화", "수", "목", "금", "토"};
    public static int[] endOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public CalendarDatas(){
        today = Calendar.getInstance();
        cYear = today.get(Calendar.YEAR);
        hMonth = today.get(Calendar.MONTH) + 1;
        cMonth = today.get(Calendar.MONTH);
        cdate = today.get(Calendar.DAY_OF_MONTH);
        dayOfWeekIndex = today.get(Calendar.DAY_OF_WEEK);
        hour = today.get(Calendar.HOUR_OF_DAY);
        minute = today.get(Calendar.MINUTE);
        seconds = today.get(Calendar.SECOND);
    }

    public Boolean isYoonYear(int year) {
        GregorianCalendar gr = new GregorianCalendar();
        return gr.isLeapYear(year);
    }




}
