package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBManager;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class AchievementRateActivity extends Activity implements AchievementRateInterface {
    TextView achievementStringtv, achievementAmounttv, currentAmounttv, percentAmounttv, remainAmounttv, betAmounttv, resultBettv, dueTv;
    String typeOfContents;
    ProgressBar progressBar;
    DBManager dbManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        init();
    }

    public void init() {
        achievementStringtv = (TextView) findViewById(R.id.goalAchievement);
        achievementAmounttv = (TextView) findViewById(R.id.goalAchievementAmount);
        currentAmounttv = (TextView) findViewById(R.id.currentAchievementAmount);
        percentAmounttv = (TextView) findViewById(R.id.PercentAchievementAmount);
        remainAmounttv = (TextView) findViewById(R.id.remainAchievementAmount);
        betAmounttv = (TextView) findViewById(R.id.betAchievementAmount);
        resultBettv = (TextView) findViewById(R.id.resultBetAchievementAmount);
        dueTv = (TextView) findViewById(R.id.dueAchievementAmount);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


    }


    public void drawDue() throws Exception {
        /*CalendarDatas calendarDatas = new CalendarDatas();
        //남은시간 타이머.
        //00:00:00 이 밤 12시
        CountDown countDown = new CountDown(600000,100);
        countDown.start();*/
    }


    public double makePercent(int current, int goal) {
        double result=0;
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
        }
        return result;
    }
}
