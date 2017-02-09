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
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.NOTIFICATION_TERM;


public class NotificationService extends Service {
    NotificationManager notificationManager;
    public Boolean isRunning;
    DBManager dbManager;
    UserDBManager userDBManager;

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("dhrms", "bindtest");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        userDBManager = new UserDBManager(getBaseContext(), "userdb.db", null, 1);
        Log.e("dhrms", "onCreate");
        isRunning = true;
    }


    @Override
    public void onDestroy() {
        Log.e("dhrms", "destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("dhrms", "onStartCommand");
        int intToBooleanIsNoti = userDBManager.getIsNoti();
        if(intToBooleanIsNoti==1){
            isRunning=true;
        }else{
            isRunning=false;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        doNotification();
                        Thread.sleep(NOTIFICATION_TERM);

                    } catch (Exception e) {

                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }

    public void setNotificationBuild(String str, PendingIntent pendingIntent) {
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.goal);
        mBuilder.setTicker("Notification.Builder");
        mBuilder.setContentTitle("GoalSnowBall의 목표를 설정하세요.");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentText("" + str);
        if(userDBManager.getIsSound()==1) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }else{
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(111, mBuilder.build());
    }

    public void doNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        boolean todayNothing, weekNothing, monthNothing;
        todayNothing = dbManager.getGoal(FROM_TODAY).equals("");
        weekNothing = dbManager.getGoal(FROM_WEEK).equals("");
        monthNothing = dbManager.getGoal(FROM_MONTH).equals("");


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



    }
}