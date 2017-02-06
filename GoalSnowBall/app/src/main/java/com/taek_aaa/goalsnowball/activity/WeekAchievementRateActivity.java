package com.taek_aaa.goalsnowball.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.controller.CountDown;

import java.util.Calendar;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessWeek;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldWeek;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class WeekAchievementRateActivity extends AchievementRateActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (dbManager.getType(FROM_WEEK).equals("물리적양")) {
            typeOfContents = "물리적양";
        } else if (dbManager.getType(FROM_WEEK).equals("시간적양")) {
            typeOfContents = "시간적양";
        } else {
            typeOfContents = "error";
        }
        draw();


    }

    public void drawGoal() {
        achievementStringtv.setText("" + dbManager.getGoal(FROM_WEEK));
    }

    public void drawGoalAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + dbManager.getGoalAmount(FROM_WEEK) + "" + dbManager.getUnit(FROM_WEEK));
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + dbManager.getGoalAmount(FROM_WEEK) + "분 " + dbManager.getUnit(FROM_WEEK));
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + dbManager.getCurrentAmount(FROM_WEEK) + "" + dbManager.getUnit(FROM_WEEK));
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + dbManager.getCurrentAmount(FROM_WEEK) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercentProgressBar(String type) throws Exception {
        double result;
        int goal = dbManager.getGoalAmount(FROM_WEEK);
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(dbManager.getGoalAmount(FROM_WEEK));
            progressBar.setProgress(dbManager.getCurrentAmount(FROM_WEEK));
            progressBar.setVisibility(ProgressBar.VISIBLE);
            current = dbManager.getCurrentAmount(FROM_WEEK);
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
            remainAmounttv.setText("" + (dbManager.getGoalAmount(FROM_WEEK) - dbManager.getCurrentAmount(FROM_WEEK)) + "" + dbManager.getUnit(FROM_WEEK));
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (dbManager.getGoalAmount(FROM_WEEK) - dbManager.getCurrentAmount(FROM_WEEK)) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingGold() {
        if(isSuccessWeek){
            betAmounttv.setText("" + getGoldWeek + " Gold");
        }else{
            betAmounttv.setText("" + dbManager.getBettingGold(FROM_WEEK) + " Gold");
        }
    }

    public void drawBettingResult() {
        if (dbManager.getIsSuccess(FROM_WEEK) == 1) {
            //획득
            resultBettv.setText("획득하였습니다.");
        } else if(dbManager.getIsSuccess(FROM_WEEK)==2){
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
        fromCountDownDday=1;


    }


    public void draw() {
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
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "이번주의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
