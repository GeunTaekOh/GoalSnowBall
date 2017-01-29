package com.taek_aaa.goalsnowball.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.taek_aaa.goalsnowball.data.CalendarDatas;

import static com.taek_aaa.goalsnowball.activity.MainActivity.isMonthDueFinish;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isTodayDueFinish;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isWeekDueFinish;

public class CurrentTimeService extends Service {
    Boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("now","온크리에이트 커런트");
        isRunning=true;
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
                        getCurrentTime();
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }

    public void getCurrentTime(){
        CalendarDatas now = new CalendarDatas();
        Log.i("now",""+now.hour+"시 "+now.minute+"분 "+now.seconds+"초 ");

        int endDay= now.getEndOfMonth(now.cYear,now.cMonth);
        
        if(now.hour==23 && now.minute==59 && now.seconds==0){
            isTodayDueFinish=true;
            if(now.dayOfWeekIndex==1){      //일요일
                isWeekDueFinish=true;
            }
            if(now.cdate==endDay){           //마지막일
                isMonthDueFinish=true;
            }
        }

    }
}
