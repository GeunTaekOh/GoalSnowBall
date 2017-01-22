package com.taek_aaa.goalsnowball.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isSuccessWeek;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldWeek;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class WeekAchievementRateActivity extends AchievementRateActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (goalDataSet.getTypeWeek().equals("물리적양")) {
            typeOfContents = "물리적양";
        } else if (goalDataSet.getTypeWeek().equals("시간적양")) {
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
            Toast.makeText(getBaseContext(), "이번주의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    public void drawGoal() {
        achievementStringtv.setText("" + goalDataSet.getWeekGoal());
    }

    public void drawGoalAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountWeek() + "" + categoryPhysicalArrays[goalDataSet.getUnitWeek()]);
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + goalDataSet.getAmountWeek() + "분 " + categoryTimeArrays[goalDataSet.getUnitWeek()]);
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(String type) throws Exception{
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountWeek() + "" + categoryPhysicalArrays[goalDataSet.getUnitWeek()]);
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + goalDataSet.getCurrentAmountWeek() + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercent(String type) throws Exception {
        double result;
        int goal = goalDataSet.getAmountWeek();
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(goalDataSet.getAmountWeek());
            progressBar.setProgress(goalDataSet.getCurrentAmountWeek());
            progressBar.setVisibility(ProgressBar.VISIBLE);
            current = goalDataSet.getCurrentAmountWeek();
        } else {
            throw new Exception();
        }

        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
            percentAmounttv.setText("" + result + "%");
            percentAmounttv.setTextColor(Color.GREEN);
        }else{
            percentAmounttv.setText("" + result + "%");
        }
    }

    public void drawRemainAmount(String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountWeek() - goalDataSet.getCurrentAmountWeek()) + "" + categoryPhysicalArrays[goalDataSet.getUnitWeek()]);
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (goalDataSet.getAmountWeek() - goalDataSet.getCurrentAmountWeek()) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingGold()  {
        if(isSuccessWeek==false) {
            betAmounttv.setText("" + goalDataSet.getBettingGoldWeek() + " Gold");
        }else{
            betAmounttv.setText("" + getGoldWeek + " Gold");
        }
    }

    public void drawBettingResult() {
        if(goalDataSet.getBettingGoldWeek()==0){
            //획득
            resultBettv.setText("획득하였습니다.");
        }else{
            //도전중
            resultBettv.setText("도전중입니다.");
        }
        ////// 여기 실패해서 미획득 일때 구현하기



    }

}
