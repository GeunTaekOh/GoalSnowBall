package com.taek_aaa.goalsnowball.activity.AchievementRate;

import android.app.Activity;
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
import static com.taek_aaa.goalsnowball.data.CalendarDatas.TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.DOING_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.DRAW_COUNT_DOWN_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.DRAW_COUNT_DOWN_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.DRAW_COUNT_DOWN_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.SUCCESS_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.headColor;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessToday;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessWeek;
import static com.taek_aaa.goalsnowball.data.CommonData.myBlack;
import static com.taek_aaa.goalsnowball.data.CommonData.myGreen;
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
        //dataController = new DataController();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(headColor);
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
            achievementAmounttv.setText("" + dbManagerInstance.getGoalAmount(from,TODAY) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            achievementAmounttv.setText("" + dbManagerInstance.getGoalAmount(from,TODAY) + "분 " + dbManagerInstance.getUnit(from));
        } else {
            throw new Exception();
        }
    }

    public void drawCurrentAmount(int from, String type) throws Exception {
        if (type.equals("물리적양")) {
            currentAmounttv.setText("" + dbManagerInstance.getCurrentAmount(from,TODAY) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            currentAmounttv.setText("" + dbManagerInstance.getCurrentAmount(from,TODAY) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawPercentProgressBar(int from, String type) throws Exception {
        double result;
        int goal = dbManagerInstance.getGoalAmount(from,TODAY);
        int current;

        if (type.equals("물리적양") || (type.equals("시간적양"))) {
            progressBar.setMax(dbManagerInstance.getGoalAmount(from,TODAY) * 100);
            progressBar.setProgress(dbManagerInstance.getCurrentAmount(from,TODAY) * 100);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            dataController.setProgressAnimate(progressBar);

            current = dbManagerInstance.getCurrentAmount(from,TODAY);
        } else {
            throw new Exception();
        }

        result = dataController.makePercent(current, goal);

        if (result == 100) {
            percentAmounttv.setTextColor(myGreen);
        } else {
            percentAmounttv.setTextColor(myBlack);
        }
        percentAmounttv.setText("" + result + "%");
    }

    public void drawRemainAmount(int from, String type) throws Exception {
        if (type.equals("물리적양")) {
            remainAmounttv.setText("" + (dbManagerInstance.getGoalAmount(from,TODAY) - dbManagerInstance.getCurrentAmount(from,TODAY)) + "" + dbManagerInstance.getUnit(from));
        } else if (type.equals("시간적양")) {
            remainAmounttv.setText("" + (dbManagerInstance.getGoalAmount(from,TODAY) - dbManagerInstance.getCurrentAmount(from,TODAY)) + "분");
        } else {
            throw new Exception();
        }
    }

    public void drawBettingResult(int from) {
        if (dbManagerInstance.getIsSuccess(from) == SUCCESS_STATUS) {
            //획득
            resultBettv.setText("획득하였습니다.");
        } else if (dbManagerInstance.getIsSuccess(from) == DOING_STATUS) {
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
            fromCountDownDday = DRAW_COUNT_DOWN_TODAY;      //오늘
        } else if (from == FROM_WEEK) {
            fromCountDownDday = DRAW_COUNT_DOWN_WEEK;      //이번주
        } else {
            fromCountDownDday = DRAW_COUNT_DOWN_MONTH;      //이번달
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
            betAmounttv.setText("" + dbManagerInstance.getBettingGold(from,TODAY) + " Gold");
        }
    }


    public void onClickBackSpace(View v){
        onBackPressed();
    }
}
