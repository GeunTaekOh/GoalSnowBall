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


    public CalendarDatas() {
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

    public int getEndOfMonth(int year, int month) {
        int result=0;
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                result=31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                result= 30;
                break;
            case 1:
                if (isYoonYear(year)) {
                    result=29;
                } else {
                    result=28;
                }
                break;
        }
        return result;
    }


}
