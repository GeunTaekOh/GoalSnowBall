package com.taek_aaa.goalsnowball.controller;

import android.os.CountDownTimer;
import android.os.Handler;

import com.taek_aaa.goalsnowball.data.CalendarDatas;

import static com.taek_aaa.goalsnowball.activity.AchievementRateActivity.dueTimeFinish;
import static com.taek_aaa.goalsnowball.activity.AchievementRateActivity.fromCountDownDday;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */


//아니면 시간적인양에서 이미 구현한거 함수로만들고 타이머 스톱워치 만들어서 가져다 쓰기
public class CountDown extends CountDownTimer{

    Handler handler = new Handler();

    public CountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        dueTimeFinish.setText(formatTime(l,fromCountDownDday));
    }

    @Override
    public void onFinish() {
        handler.removeCallbacksAndMessages(updateTimeTask);
        dueTimeFinish.setText("Finish!");

        handler = null;
        handler = new Handler();
        cancel();

    }

    public void onStop(){
        handler.removeCallbacksAndMessages(updateTimeTask);
        handler = null;
        handler = new Handler();
        cancel();

    }

    public String formatTime(long millis, int dday) {
        String output = "00:00:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String sec = String.valueOf(seconds);
        String min = String.valueOf(minutes);
        String hour = String.valueOf(hours);

        if (seconds < 10)
            sec = "0" + seconds;
        if (minutes < 10)
            min= "0" + minutes;
        if(hours < 10)
            hour = "0" + hours;

        CalendarDatas calendarDatas = new CalendarDatas();

        if(dday==0){                 //오늘 목표 달성률 남은 기간
            output = hour + " : " + min + " : " + sec;
        }else if(dday==1){          //이번주 목표 달성률 남은 기간
            int countDownDday = calendarDatas.getDdayWeek(calendarDatas.dayOfWeekIndex) -1;
            output = ""+countDownDday+"일  "+hour + " : " + min + " : " + sec;
        }else{                      //이번달 목표 달성률 남은 기간
            int countDownDday= calendarDatas.getEndOfMonth(calendarDatas.cYear, calendarDatas.cMonth) -1 ;
            countDownDday = (countDownDday - calendarDatas.cdate + 1);
            output = ""+countDownDday+"일  "+hour + " : " + min + " : " + sec;
        }
        return output;
    }//formatTime

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);
        }
    };


}
