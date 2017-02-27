package com.taek_aaa.goalsnowball.data;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class CalendarDatas {
    public Calendar today;
    public int cYear, hMonth, cMonth, cdate, dayOfWeekIndex;
    public int hour, minute, seconds;
    public int weekOfYear;
    public final static int TODAY = 100;
    public final static int YESTERDAY = 101;


    public CalendarDatas(int dayType) {

        today = Calendar.getInstance();
        if(dayType == YESTERDAY){
            today.add(Calendar.DATE, -1);
        }

        today.setFirstDayOfWeek(Calendar.MONDAY);
        cYear = today.get(Calendar.YEAR);
        hMonth = today.get(Calendar.MONTH) + 1;
        cMonth = today.get(Calendar.MONTH);
        cdate = today.get(Calendar.DAY_OF_MONTH);
        dayOfWeekIndex = today.get(Calendar.DAY_OF_WEEK);
        hour = today.get(Calendar.HOUR_OF_DAY);
        minute = today.get(Calendar.MINUTE);
        seconds = today.get(Calendar.SECOND);
        weekOfYear = today.get(Calendar.WEEK_OF_YEAR);
    }

    private Boolean isYoonYear(int year) {
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

    public int getDdayWeek(int dayOfWeekIndex){
        int countWeek = 0;

        switch (dayOfWeekIndex) {
            case 1:
                countWeek = 1;
                break;
            case 2:
                countWeek = 7;
                break;
            case 3:
                countWeek = 6;
                break;
            case 4:
                countWeek = 5;
                break;
            case 5:
                countWeek = 4;
                break;
            case 6:
                countWeek = 3;
                break;
            case 7:
                countWeek = 2;
                break;
        }

        return countWeek;
    }

    public int getWeekOfYear(int year, int month, int date){
        int result=0;
        String sResult="";
        String sYear, sMonth, sDate;
        sYear = String.valueOf(year);
        if(month<10){
            sMonth="0"+month;
        }else{
            sMonth = String.valueOf(month);
        }
        if(date<10){
            sDate="0"+date;
        }else{
            sDate = String.valueOf(date);
        }
        sResult=sYear+sMonth+sDate;

        SimpleDateFormat df = new SimpleDateFormat("yyyymmdd");

        try {
            Date tempDate = df.parse(sResult);
            Calendar today = Calendar.getInstance();
            today.setTime(tempDate);
            result = today.get(Calendar.WEEK_OF_YEAR);
        }catch (Exception e){
            Log.e("rmsxor","캘린더데이타 오류");
        }
        return result;
    }


}
