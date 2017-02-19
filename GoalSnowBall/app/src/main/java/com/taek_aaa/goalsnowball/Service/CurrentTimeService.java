package com.taek_aaa.goalsnowball.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_A;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_B;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_C;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_D;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_MASTER;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_S;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_SS;
import static com.taek_aaa.goalsnowball.data.CommonData.TO_SSS;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetToday;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetWeek;
import static com.taek_aaa.goalsnowball.data.CommonData.isFailMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.isFailWeek;
import static com.taek_aaa.goalsnowball.data.CommonData.isMonthDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.isTodayDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.isWeekDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.totalLooseCoin;
import static com.taek_aaa.goalsnowball.data.CommonData.whatGradeTo;

public class CurrentTimeService extends Service {
    Boolean isRunning;
    DBManager dbManager;
    UserDBManager userDBManager;
    DataController dataController;
    Context context;
    public static String[] gradeArray = {"UnRank", "D", "C", "B", "A", "S", "SS", "SSS", "Master"};
    public static int[] needsGold = {100, 300, 500, 1000, 5000, 20000, 100000, 1000000};
    public static int[] needsAmount = {10, 30, 50, 100, 300, 500, 1000, 3000};

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        userDBManager = new UserDBManager(getBaseContext(), "userdb.db", null, 1);
        Log.e("now", "온크리에이트 커런트");
        isRunning = true;
        dataController = new DataController();
        context = getBaseContext();
    }


    @Override
    public void onDestroy() {
        //  Log.e("dhrms", "destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        getCurrentTimeCheckFail();
                        checkLevelUp();
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }

    public void getCurrentTimeCheckFail() {
        CalendarDatas now = new CalendarDatas();
//        Log.i("now", "" + now.hour + "시 " + now.minute + "분 " + now.seconds + "초 ");

        int endDay = now.getEndOfMonth(now.cYear, now.cMonth);

        if (now.hour == 23 && now.minute == 59 && now.seconds == 0) {
            isTodayDueFinish = true;
            totalLooseCoin=0;

            if (dbManager.getGoalAmount(FROM_TODAY) > dbManager.getCurrentAmount(FROM_TODAY)) {
                dbManager.setIsSuccess(FROM_TODAY, 3);
                failBetToday=dbManager.getBettingGold(FROM_TODAY);
                totalLooseCoin += failBetToday;
                dataController.setPreferencesFailFlag(context, 1);
                //failFlag = true;
            }

            if (now.dayOfWeekIndex == 1) {      //일요일
                isWeekDueFinish = true;
                if (dbManager.getGoalAmount(FROM_WEEK) > dbManager.getCurrentAmount(FROM_WEEK)) {
                    isFailWeek = true;
                    dbManager.setIsSuccess(FROM_WEEK, 3);
                    failBetWeek=dbManager.getBettingGold(FROM_WEEK);
                    totalLooseCoin += failBetWeek;
                    dataController.setPreferencesFailFlag(context, 1);
                    //failFlag = true;
                }
            }
            if (now.cdate == endDay) {           //마지막일
                isMonthDueFinish = true;
                if (dbManager.getGoalAmount(FROM_MONTH) > dbManager.getCurrentAmount(FROM_MONTH)) {
                    isFailMonth = true;
                    dbManager.setIsSuccess(FROM_MONTH, 3);
                    failBetMonth=dbManager.getBettingGold(FROM_MONTH);
                    totalLooseCoin += failBetMonth;
                    dataController.setPreferencesFailFlag(context, 1);
                    //failFlag = true;
                }
            }
        }
    }

    public void checkLevelUp(){
        int gold = userDBManager.getGold();
        int count = dbManager.getLastPosition();
        if(gold >= 100 && count>= 10){
            userDBManager.setGrade("D 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo=TO_D;
        }else if (gold >= 300 && count >= 30){
            userDBManager.setGrade("C 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo=TO_C;
        }else if (gold >= 500 && count >= 50){
            userDBManager.setGrade("B 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_B;
        }else if (gold >= 1000 && count >= 100){
            userDBManager.setGrade("A 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_A;
        }else if (gold >= 5000 && count >= 300){
            userDBManager.setGrade("S 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_S;
        }else if (gold >= 20000 && count >= 500){
            userDBManager.setGrade("SS 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_SS;
        }else if (gold >= 100000 && count >= 1000){
            userDBManager.setGrade("SSS 등급");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_SSS;
        }else if(gold >= 1000000 && count >= 3000){
            userDBManager.setGrade("Master");
            dataController.setPreferencesLevelUpFlag(context, 1);
            whatGradeTo = TO_MASTER;
        }
    }

}

