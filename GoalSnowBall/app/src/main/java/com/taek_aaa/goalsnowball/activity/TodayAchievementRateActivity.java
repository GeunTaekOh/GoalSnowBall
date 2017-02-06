package com.taek_aaa.goalsnowball.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.controller.CountDown;

import java.util.Calendar;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessToday;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldToday;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class TodayAchievementRateActivity extends AchievementRateActivity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (dbManager.getType(FROM_TODAY).equals("물리적양")) {
            typeOfContents = "물리적양";
        } else if (dbManager.getType(FROM_TODAY).equals("시간적양")) {
            typeOfContents = "시간적양";
        } else {
            Log.e("lk","오류");
            typeOfContents = "error";
        }
        draw();

    }

    public void drawGoal() {
        achievementStringtv.setText("" + dbManager.getGoal(FROM_TODAY));
    }

    public void drawGoalAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + dbManager.getGoalAmount(FROM_TODAY) + "" + dbManager.getUnit(FROM_TODAY));
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + dbManager.getGoalAmount(FROM_TODAY) + "분 " + dbManager.getUnit(FROM_TODAY));
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + dbManager.getCurrentAmount(FROM_TODAY) + "" + dbManager.getUnit(FROM_TODAY));
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + dbManager.getCurrentAmount(FROM_TODAY) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercentProgressBar(String type) throws Exception {
        double result;
        int goal = dbManager.getGoalAmount(FROM_TODAY);
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(dbManager.getGoalAmount(FROM_TODAY));
            progressBar.setProgress(dbManager.getCurrentAmount(FROM_TODAY));
            progressBar.setVisibility(ProgressBar.VISIBLE);
            current = dbManager.getCurrentAmount(FROM_TODAY);
        } else {
            throw new Exception();
        }

        result = makePercent(current,goal);

        if (result ==100) {
            percentAmounttv.setTextColor(Color.GREEN);
        }else{
            percentAmounttv.setTextColor(Color.BLACK);
        }
        percentAmounttv.setText("" + result + "%");
    }

    public void drawRemainAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (dbManager.getGoalAmount(FROM_TODAY) - dbManager.getCurrentAmount(FROM_TODAY)) + "" + dbManager.getUnit(FROM_TODAY));
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (dbManager.getGoalAmount(FROM_TODAY) - dbManager.getCurrentAmount(FROM_TODAY)) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingGold()  {
        if(isSuccessToday){
            betAmounttv.setText("" + getGoldToday + " Gold");
        }else{
            betAmounttv.setText("" + dbManager.getBettingGold(FROM_TODAY) + " Gold");
        }
    }

    public void drawBettingResult() {
        if(dbManager.getIsSuccess(FROM_TODAY)==1){
            //획득
            resultBettv.setText("획득하였습니다.");
        }else if (dbManager.getIsSuccess(FROM_TODAY)==2){
            //도전중
            resultBettv.setText("도전중입니다.");
        }else{
            resultBettv.setText("실패하였습니다.");
        }
        ////// 여기 실패해서 미획득 일때 구현하기

    }

    public void drawDueTime(){
        Calendar today = Calendar.getInstance();
        long nowTime = (today.get(Calendar.HOUR_OF_DAY) * 60 * 60 + today.get(Calendar.MINUTE) * 60 + today.get(Calendar.SECOND))*1000;
        countDown = new CountDown(finishTime - nowTime,1000);
        countDown.start();
        today=null;
        fromCountDownDday=0;
    }

    public void draw(){
        try {
            drawGoal();
            drawGoalAmount(typeOfContents);
            drawCurrentAmount(typeOfContents);
            drawPercentProgressBar(typeOfContents);
            drawRemainAmount(typeOfContents);
            drawBettingGold();
            drawBettingResult();
            drawDue();
            drawDueTime();

        }
         catch (Exception e) {
            Toast.makeText(getBaseContext(), "오늘의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        countDown.onStop();
        finish();

    }
}
