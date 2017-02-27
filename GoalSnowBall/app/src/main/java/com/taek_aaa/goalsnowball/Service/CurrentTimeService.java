package com.taek_aaa.goalsnowball.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.activity.MainActivity;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.data.CalendarDatas.TODAY;
import static com.taek_aaa.goalsnowball.data.CalendarDatas.YESTERDAY;
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
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

public class CurrentTimeService extends Service {
    public Boolean isRunning;
    DataController dataController;
    Context context;
    NotificationManager notificationManager;
    public static String[] gradeArray = {"UnRank", "D", "C", "B", "A", "S", "SS", "SSS", "Master"};
    public static int[] needsGold = {100, 300, 500, 1000, 5000, 20000, 100000, 1000000};
    public static int[] needsAmount = {10, 30, 50, 100, 300, 500, 1000, 3000};
    Boolean notiFlag;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        dbManagerInstance = DBManager.getInstance(getBaseContext());
        userDBManagerInstance = UserDBManager.getInstance(getBaseContext());
        notiFlag=true;
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
                        checkNoitTime();
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
        CalendarDatas now = new CalendarDatas(TODAY);
//        Log.i("now", "" + now.hour + "시 " + now.minute + "분 " + now.seconds + "초 ");
        int endDay = now.getEndOfMonth(now.cYear, now.cMonth);

        if (now.hour == 0 && now.minute == 5) {
            isTodayDueFinish = true;
            totalLooseCoin=0;

            if (dbManagerInstance.getGoalAmount(FROM_TODAY,YESTERDAY) > dbManagerInstance.getCurrentAmount(FROM_TODAY,YESTERDAY)) {
                dbManagerInstance.setIsSuccess(FROM_TODAY, 3,YESTERDAY);
                failBetToday=dbManagerInstance.getBettingGold(FROM_TODAY,YESTERDAY);
                totalLooseCoin += failBetToday;
                dataController.setPreferencesLooseGold(context,totalLooseCoin);
                dataController.setPreferencesFailFlag(context, 1);
                //failFlag = true;
            }

            if (now.dayOfWeekIndex == 1) {      //일요일
                isWeekDueFinish = true;
                if (dbManagerInstance.getGoalAmount(FROM_WEEK,YESTERDAY) > dbManagerInstance.getCurrentAmount(FROM_WEEK,YESTERDAY)) {
                    isFailWeek = true;
                    dbManagerInstance.setIsSuccess(FROM_WEEK, 3,YESTERDAY);
                    failBetWeek=dbManagerInstance.getBettingGold(FROM_WEEK,YESTERDAY);
                    totalLooseCoin += failBetWeek;
                    dataController.setPreferencesFailFlag(context, 1);
                    dataController.setPreferencesLooseGold(context,totalLooseCoin);
                    //failFlag = true;
                }
            }
            if (now.cdate == endDay) {           //마지막일
                isMonthDueFinish = true;
                if (dbManagerInstance.getGoalAmount(FROM_MONTH,YESTERDAY) > dbManagerInstance.getCurrentAmount(FROM_MONTH,YESTERDAY)) {
                    isFailMonth = true;
                    dbManagerInstance.setIsSuccess(FROM_MONTH, 3,YESTERDAY);
                    failBetMonth=dbManagerInstance.getBettingGold(FROM_MONTH,YESTERDAY);
                    totalLooseCoin += failBetMonth;
                    dataController.setPreferencesFailFlag(context, 1);
                    dataController.setPreferencesLooseGold(context,totalLooseCoin);
                    //failFlag = true;
                }
            }
        }
    }

    public void checkLevelUp(){
        int gold = userDBManagerInstance.getGold();
        int count = dbManagerInstance.getLastPosition();


        if(gold >= 100 && count>= 10){
            userDBManagerInstance.setGrade("D 등급");
            whatGradeTo=TO_D;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 300 && count >= 30){
            userDBManagerInstance.setGrade("C 등급");
            whatGradeTo=TO_C;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 500 && count >= 50){
            userDBManagerInstance.setGrade("B 등급");
            whatGradeTo = TO_B;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 1000 && count >= 100){
            userDBManagerInstance.setGrade("A 등급");
            whatGradeTo = TO_A;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 5000 && count >= 300){
            userDBManagerInstance.setGrade("S 등급");
            whatGradeTo = TO_S;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 20000 && count >= 500){
            userDBManagerInstance.setGrade("SS 등급");
            whatGradeTo = TO_SS;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if (gold >= 100000 && count >= 1000){
            userDBManagerInstance.setGrade("SSS 등급");
            whatGradeTo = TO_SSS;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }else if(gold >= 1000000 && count >= 3000){
            userDBManagerInstance.setGrade("Master");
            whatGradeTo = TO_MASTER;
            dataController.setPreferencesLevelUpFlag(context, 1);
        }
    }

    public void checkNoitTime(){
        CalendarDatas now = new CalendarDatas(TODAY);
        if (now.hour == userDBManagerInstance.getNotiTime() && now.minute == userDBManagerInstance.getNotiMinute() && userDBManagerInstance.getIsNoti()==1 && notiFlag) {
            doNotification();
            notiFlag=false;
        }
    }
    public void doNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        boolean todayNothing, weekNothing, monthNothing;
        todayNothing = dbManagerInstance.getGoal(FROM_TODAY).equals("");
        weekNothing = dbManagerInstance.getGoal(FROM_WEEK).equals("");
        monthNothing = dbManagerInstance.getGoal(FROM_MONTH).equals("");

        if (todayNothing && weekNothing && monthNothing) {
            setNotificationBuild("오늘과 이번주와 이번달의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if (todayNothing && weekNothing) {
            setNotificationBuild("오늘과 이번주의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if (todayNothing && monthNothing) {
            setNotificationBuild("오늘과 이번달의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if (weekNothing && monthNothing) {
            setNotificationBuild("이번주와 이번달의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if (todayNothing) {
            setNotificationBuild("오늘의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if (weekNothing) {
            setNotificationBuild("이번주의 목표를 새롭게 설정하세요.", pendingIntent);
        } else if(monthNothing){
            setNotificationBuild("이번달의 목표를 새롭게 설정하세요.", pendingIntent);
        }

     //   this.isRunning=false;
    }
    public void setNotificationBuild(String str, PendingIntent pendingIntent) {
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.goal);
        mBuilder.setTicker("Notification.Builder");
        mBuilder.setContentTitle("주무시기 전에 미리 목표를 설정하세요.");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentText("" + str);
        if (userDBManagerInstance.getIsSound() == 1) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        } else {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(111, mBuilder.build());
    }

}

