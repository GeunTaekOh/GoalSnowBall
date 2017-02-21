package com.taek_aaa.goalsnowball.data;

import android.view.LayoutInflater;

/**
 * Created by taek_aaa on 2017. 2. 6..
 */

public class CommonData {
    public final static int FROM_TODAY = 991;
    public final static int FROM_WEEK = 992;
    public final static int FROM_MONTH = 993;
    public final static int NOTIFICATION_TERM = 12 * 1000 * 60 * 60;     //12시간 마다 확인
    public static LayoutInflater inflater;
    public static int viewHeight = 700;        //원하는 뷰의 높이(해상도)
    public static String[] categoryPhysicalArrays = {"개", "쪽", "권", ""};
    public static String[] categoryTimeArrays = {"이상", "이하"};
    public static float defaultHeight, defaultWidth;
    public static boolean isSuccessToday = false, isSuccessWeek = false, isSuccessMonth = false;
    public static boolean isTodayDueFinish, isWeekDueFinish, isMonthDueFinish;
    public static boolean isFailToday, isFailWeek, isFailMonth;
    public static int failBetToday, failBetWeek, failBetMonth;
    public static int totalLooseCoin = 0;
    public static int listViewPosition = 0;
    public static int whatGradeTo;
    public final static int TO_D = 881;
    public final static int TO_C = 882;
    public final static int TO_B = 883;
    public final static int TO_A = 884;
    public final static int TO_S = 885;
    public final static int TO_SS = 886;
    public final static int TO_SSS = 887;
    public final static int TO_MASTER = 888;
    public static int myRed;
    public static int myGreen;
    public static int myBlack;
    public static int headColor;


    public static void setFailStatus(boolean bool) {
        isFailToday = bool;
        isFailWeek = bool;
        isFailMonth = bool;
        isTodayDueFinish = bool;
        isWeekDueFinish = bool;
        isMonthDueFinish = bool;
    }
}
