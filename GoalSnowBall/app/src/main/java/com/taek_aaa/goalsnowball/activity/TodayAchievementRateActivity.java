package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isSuccessToday;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldToday;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class TodayAchievementRateActivity extends Activity implements AchievementRateInterface {

    TextView achievementStringtv, achievementAmounttv, currentAmounttv, percentAmounttv, remainAmounttv, betAmounttv, resultBettv, dueTv;
    String typeOfContents;
    ProgressBar progressBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_achievement);

        init();

        if (goalDataSet.getTypeToday().equals("물리적양")) {
            typeOfContents = "물리적양";
        } else if (goalDataSet.getTypeToday().equals("시간적양")) {
            typeOfContents = "시간적양";
        } else {
            typeOfContents = "error";
        }
        try {
            drawGoal();
            drawGoalAmount(typeOfContents);
            drawCurrentAmount(typeOfContents);
            drawPercent(typeOfContents);
            drawRemainAmount(typeOfContents);
            drawBettingGold();
            drawBettingResult();
            drawDue();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "오늘의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void init() {
        achievementStringtv = (TextView) findViewById(R.id.goalTodayAchievement);
        achievementAmounttv = (TextView) findViewById(R.id.goalTodayAchievementAmount);
        currentAmounttv = (TextView) findViewById(R.id.currentTodayAchievementAmount);
        percentAmounttv = (TextView) findViewById(R.id.PercenttodayAchievementAmount);
        remainAmounttv = (TextView) findViewById(R.id.remainTodayAchievementAmount);
        betAmounttv = (TextView) findViewById(R.id.betTodayAchievementAmount);
        resultBettv = (TextView) findViewById(R.id.resultBetTodayAchievementAmount);
        dueTv = (TextView) findViewById(R.id.dueTodayAchievementAmount);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
    }

    public void drawGoal() {
        achievementStringtv.setText("" + goalDataSet.getTodayGoal());
    }

    public void drawGoalAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountToday() + "분 " + categoryTimeArrays[goalDataSet.getUnitToday()]);
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountToday() + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercent(String type) throws Exception {
        double result;
        int goal = goalDataSet.getAmountToday();
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(goalDataSet.getAmountToday());
            progressBar.setProgress(goalDataSet.getCurrentAmountToday());
            progressBar.setVisibility(ProgressBar.VISIBLE);
            current = goalDataSet.getCurrentAmountToday();
        } else {
            throw new Exception();
        }

        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result == 100.0) {
            percentAmounttv.setText("" + result + "%");
            percentAmounttv.setTextColor(Color.GREEN);
        }else{
            percentAmounttv.setText("" + result + "%");
        }
    }

    public void drawRemainAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountToday() - goalDataSet.getCurrentAmountToday()) + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountToday() - goalDataSet.getCurrentAmountToday()) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingGold()  {
        if(isSuccessToday==false) {
            betAmounttv.setText("" + goalDataSet.getBettingGoldToday() + " Gold");
        }else{
            betAmounttv.setText("" + getGoldToday + " Gold");
        }
    }

    public void drawBettingResult() {
        if(goalDataSet.getBettingGoldToday()==0){
            //획득
            resultBettv.setText("획득하였습니다.");
        }else{
            //도전중
            resultBettv.setText("도전중입니다.");
        }
        ////// 여기 실패해서 미획득 일때 구현하기



    }

    public void drawDue() throws Exception{
        /*CalendarDatas calendarDatas = new CalendarDatas();
        //남은시간 타이머.
        //00:00:00 이 밤 12시
        CountDown countDown = new CountDown(600000,100);
        countDown.start();*/
    }

}
