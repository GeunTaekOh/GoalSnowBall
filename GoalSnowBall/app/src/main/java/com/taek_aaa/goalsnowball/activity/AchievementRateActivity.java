package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.CountDown;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.DBManager;


import java.util.Calendar;

import static com.taek_aaa.goalsnowball.R.id.dueAchievementAmount;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessToday;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessWeek;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;

import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldMonth;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldToday;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.getGoldWeek;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class AchievementRateActivity extends Activity implements AchievementRateInterface {
    TextView achievementStringtv, achievementAmounttv, currentAmounttv, percentAmounttv, remainAmounttv, betAmounttv, resultBettv, dueTv;
    String typeOfContents;
    ProgressBar progressBar;
    long finishTime = 1000 * 60 * 60 * 24;   //24시간
    CountDown countDown;
    public static int fromCountDownDday;
    public static TextView dueTimeFinish;
    DataController dataController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        dbManagerInstance = DBManager.getInstance(getBaseContext());
        dataController = new DataController();
        init();

    }

    private void init() {
        achievementStringtv = (TextView) findViewById(R.id.goalAchievement);
        achievementAmounttv = (TextView) findViewById(R.id.goalAchievementAmount);
        currentAmounttv = (TextView) findViewById(R.id.currentAchievementAmount);
        percentAmounttv = (TextView) findViewById(R.id.PercentAchievementAmount);
        remainAmounttv = (TextView) findViewById(R.id.remainAchievementAmount);
        betAmounttv = (TextView) findViewById(R.id.betAchievementAmount);
        resultBettv = (TextView) findViewById(R.id.resultBetAchievementAmount);
        dueTv = (TextView) findViewById(dueAchievementAmount);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        dueTimeFinish = (TextView) findViewById(dueAchievementAmount);
        dataController = new DataController();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#99BADD"));
        }

    }


    protected String whatContentsType(int type) {
        String str;
        if (dbManagerInstance.getType(type).equals("물리적양")) {
            str = "물리적양";
        } else if (dbManagerInstance.getType(type).equals("시간적양")) {
            str = "시간적양";
        } else {
            str = "error";
        }
        return str;
    }


    public void drawGoal(int from) {
        achievementStringtv.setText("" + dbManagerInstance.getGoal(from));
    }


    public void drawGoalAmount(int from, String type) throws Exception {
        if (type.equals("물리적양")) {
            achievementAmounttv.setText("" + dbManagerInstance.getGoalAmount(from) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + dbManagerInstance.getGoalAmount(from) + "분 " + dbManagerInstance.getUnit(from));
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(int from, String type) throws Exception {
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + dbManagerInstance.getCurrentAmount(from) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + dbManagerInstance.getCurrentAmount(from) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercentProgressBar(int from, String type) throws Exception {
        double result;
        int goal = dbManagerInstance.getGoalAmount(from);
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(dbManagerInstance.getGoalAmount(from) * 100);
            progressBar.setProgress(dbManagerInstance.getCurrentAmount(from) * 100);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            dataController.setProgressAnimate(progressBar);

            current = dbManagerInstance.getCurrentAmount(from);
        } else {
            throw new Exception();
        }

        result = dataController.makePercent(current, goal);

        if (result == 100) {
            percentAmounttv.setTextColor(Color.parseColor("#A7FC00"));
        } else {
            percentAmounttv.setTextColor(Color.parseColor("#808080"));

        }
        percentAmounttv.setText("" + result + "%");
    }

    public void drawRemainAmount(int from, String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (dbManagerInstance.getGoalAmount(from) - dbManagerInstance.getCurrentAmount(from)) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (dbManagerInstance.getGoalAmount(from) - dbManagerInstance.getCurrentAmount(from)) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingResult(int from) {
        if (dbManagerInstance.getIsSuccess(from) == 1) {
            //획득
            resultBettv.setText("획득하였습니다.");
        } else if (dbManagerInstance.getIsSuccess(from) == 2) {
            //도전중
            resultBettv.setText("도전중입니다.");
        } else {
            resultBettv.setText("실패하였습니다.");
        }
    }

    public void drawDueTime(int from) {
        Calendar today = Calendar.getInstance();
        long nowTime = (today.get(Calendar.HOUR_OF_DAY) * 60 * 60 + today.get(Calendar.MINUTE) * 60 + today.get(Calendar.SECOND)) * 1000;
        countDown = new CountDown(finishTime - nowTime, 1000);
        countDown.start();
        today = null;
        if (from == FROM_TODAY) {
            fromCountDownDday = 0;
        } else if (from == FROM_WEEK) {
            fromCountDownDday = 1;
        } else {
            fromCountDownDday = 2;
        }
    }

    public void drawBettingGold(int from) {
        Boolean isSuccess;
        int getGold;

        if (from == FROM_TODAY) {
            isSuccess = isSuccessToday;
            getGold = getGoldToday;
        } else if (from == FROM_WEEK) {
            isSuccess = isSuccessWeek;
            getGold = getGoldWeek;
        } else {
            isSuccess = isSuccessMonth;
            getGold = getGoldMonth;
        }
        if (isSuccess) {
            betAmounttv.setText("" + getGold + " Gold");
        } else {
            betAmounttv.setText("" + dbManagerInstance.getBettingGold(from) + " Gold");
        }
    }


    public void onClickBackSpace(View v){
        onBackPressed();
    }

}
