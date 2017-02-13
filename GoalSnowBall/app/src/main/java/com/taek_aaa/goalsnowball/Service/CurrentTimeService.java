package com.taek_aaa.goalsnowball.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetToday;
import static com.taek_aaa.goalsnowball.data.CommonData.failFlag;
import static com.taek_aaa.goalsnowball.data.CommonData.isFailMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.isFailWeek;
import static com.taek_aaa.goalsnowball.data.CommonData.isMonthDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.isTodayDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.isWeekDueFinish;
import static com.taek_aaa.goalsnowball.data.CommonData.totalLooseCoin;

public class CurrentTimeService extends Service {
    Boolean isRunning;
    DBManager dbManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        Log.e("now", "온크리에이트 커런트");
        isRunning = true;
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
            if (dbManager.getGoalAmount(FROM_TODAY) > dbManager.getCurrentAmount(FROM_TODAY)) {
                dbManager.setIsSuccess(FROM_TODAY, 3);
                failBetToday=dbManager.getBettingGold(FROM_TODAY);
                totalLooseCoin += failBetToday;
                failFlag = true;
            }

            if (now.dayOfWeekIndex == 1) {      //일요일
                isWeekDueFinish = true;
                if (dbManager.getGoalAmount(FROM_WEEK) > dbManager.getCurrentAmount(FROM_WEEK)) {
                    isFailWeek = true;
                    dbManager.setIsSuccess(FROM_WEEK, 3);
                    failBetToday=dbManager.getBettingGold(FROM_WEEK);
                    totalLooseCoin += failBetToday;
                    failFlag = true;
                }

            }
            if (now.cdate == endDay) {           //마지막일
                isMonthDueFinish = true;
                if (dbManager.getGoalAmount(FROM_MONTH) > dbManager.getCurrentAmount(FROM_MONTH)) {
                    isFailMonth = true;
                    dbManager.setIsSuccess(FROM_MONTH, 3);
                    failBetToday=dbManager.getBettingGold(FROM_MONTH);
                    totalLooseCoin += failBetToday;
                    failFlag = true;
                }
            }
        }
    }
}
