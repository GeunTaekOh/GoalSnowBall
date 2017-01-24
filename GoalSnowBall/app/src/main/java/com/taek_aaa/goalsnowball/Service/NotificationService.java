package com.taek_aaa.goalsnowball.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.activity.MainActivity;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class NotificationService extends Service {

    NotificationManager notificationManager;
    ServiceThread thread;
    Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스 종료될 때 할 작업
    public void onDestroy(){
        thread.stopForever();
        thread=null;
    }

    class myServiceHandler extends Handler{

        //@Override
        public void handleMessage(android.os.Message msg){
            Intent intent = new Intent(NotificationService.this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.goal)
            .setTicker("Notification.Builder")
            .setWhen(System.currentTimeMillis())
            .setContentTitle("GoalSnowBall의 목표를 설정하세요.")
            //.setContentText("" + str)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build();
            //알람소리 한번만
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            //확인하면 자동으로 알람 제거
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(777,notification);

        }

        @Override
        public void publish(LogRecord logRecord) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    }
}
