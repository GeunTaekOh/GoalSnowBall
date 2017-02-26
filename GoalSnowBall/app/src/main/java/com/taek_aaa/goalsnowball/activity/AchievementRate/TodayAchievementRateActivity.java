package com.taek_aaa.goalsnowball.activity.AchievementRate;

import android.os.Bundle;
import android.widget.Toast;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public class TodayAchievementRateActivity extends AchievementRateActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfContents = whatContentsType(FROM_TODAY);
        draw();
    }
    protected void draw(){
        try {
            drawGoal(FROM_TODAY);
            drawGoalAmount(FROM_TODAY,typeOfContents);
            drawCurrentAmount(FROM_TODAY,typeOfContents);
            drawPercentProgressBar(FROM_TODAY,typeOfContents);
            drawRemainAmount(FROM_TODAY,typeOfContents);
            drawBettingGold(FROM_TODAY);
            drawBettingResult(FROM_TODAY);
            drawDueTime(FROM_TODAY);
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
