package com.taek_aaa.goalsnowball.activity.AchievementRate;

import android.os.Bundle;
import android.widget.Toast;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class MonthAchievementRateActivity extends AchievementRateActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfContents = whatContentsType(FROM_MONTH);
        draw();
    }

    public void draw() {
        try {
            drawGoal(FROM_MONTH);
            drawGoalAmount(FROM_MONTH,typeOfContents);
            drawCurrentAmount(FROM_MONTH,typeOfContents);
            drawPercentProgressBar(FROM_MONTH,typeOfContents);
            drawRemainAmount(FROM_MONTH,typeOfContents);
            drawBettingGold(FROM_MONTH);
            drawBettingResult(FROM_MONTH);
            drawDueTime(FROM_MONTH);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "이번달의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        countDown.onStop();
        finish();
    }
}
