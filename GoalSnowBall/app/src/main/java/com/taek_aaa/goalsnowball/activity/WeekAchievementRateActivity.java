package com.taek_aaa.goalsnowball.activity;

import android.os.Bundle;
import android.widget.Toast;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class WeekAchievementRateActivity extends AchievementRateActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeOfContents = whatContentsType(FROM_WEEK);
        draw();
    }

    public void draw() {
        try {
            drawGoal(FROM_WEEK);
            drawGoalAmount(FROM_WEEK, typeOfContents);
            drawCurrentAmount(FROM_WEEK, typeOfContents);
            drawPercentProgressBar(FROM_WEEK, typeOfContents);
            drawRemainAmount(FROM_WEEK, typeOfContents);
            drawBettingGold(FROM_WEEK);
            drawBettingResult(FROM_WEEK);
            drawDueTime(FROM_WEEK);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "이번주의 목표를 먼저 입력하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        countDown.onStop();
        finish();

    }
}
