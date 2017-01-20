package com.taek_aaa.goalsnowball.controller;

import android.os.CountDownTimer;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */


//아니면 시간적인양에서 이미 구현한거 함수로만들고 타이머 스톱워치 만들어서 가져다 쓰기
public class CountDown extends CountDownTimer{
    public CountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
       // Timer.setText("" + formatTime(l));
    }

    @Override
    public void onFinish() {

    }

    public String formatTime(long millis) {
        String output = "00:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        String sec = String.valueOf(seconds);
        String min = String.valueOf(minutes);

        if (seconds < 10)
            sec = "0" + seconds;
        if (minutes < 10)
            min= "0" + minutes;

        output = min + " : " + sec;
        return output;
    }//formatTime
}
