package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.dialog.SuccessDialog;

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;
import static com.taek_aaa.goalsnowball.activity.MainActivity.isSuccessToday;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.SUCCESS_FROM_TODAY;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.whereSuccess;

/**
 * Created by taek_aaa on 2017. 1. 15..
 */

public class TodayGoalDoingActivity extends Activity implements GoalDoingInterface{

    TextView doingGoalTodaytv;
    EditText amountOfEdit;
    Boolean isAmount;
    Boolean isStartButtonClicked = true;      //start, pause구분
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int secs = 0;
    int mins = 0;
    int hours = 0;
    Handler handler = new Handler();
    TextView blackboardtv, timeOfCurrenttv, successGetGoldtv;
    static int tmpAmount;
    SuccessDialog successDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            /** 물리적 양 일때 **/
            if (goalDataSet.getTypeToday().equals("물리적양")) {
                setContentView(R.layout.activity_today_goal_amount_doing);
                Log.e("aa", "물리적양");
                isAmount = true;
                amountOfEdit = (EditText) findViewById(R.id.doing_current_amount_today);
                amountOfEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        amountOfEdit.setText("" + goalDataSet.getCurrentAmountToday());
                    }
                });
            } else if (goalDataSet.getTypeToday().equals("시간적양")) {
                /** 시간적 양 일때 **/
                setContentView(R.layout.activity_today_goal_time_doing);
                Log.e("aa", "시간적양");

                TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
                stopWatchtv.setText("00:00:00");
                timeOfCurrenttv = (TextView) findViewById(R.id.doing_current_time_today);
                timeOfCurrenttv.setText("수행 시간 : " + goalDataSet.getCurrentAmountToday() + "분");
                isAmount = false;
            }
            successGetGoldtv = (TextView) findViewById(R.id.successGetGoldtv);
            successGetGoldtv.setText("성공시 획득 골드 : " + "" + goalDataSet.getBettingGoldToday() + "Gold");
            blackboardtv = (TextView) findViewById(R.id.doing_goalAmount_today);
            doingGoalTodaytv = (TextView) findViewById(R.id.doing_goal_today);
            doingGoalTodaytv.setText("오늘의 목표 : " + goalDataSet.getTodayGoal());
            if (isAmount) {
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountToday() + "" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
                TextView unittv = (TextView) findViewById(R.id.doing_unit_today);
                unittv.setText("" + categoryPhysicalArrays[goalDataSet.getUnitToday()]);
            } else {
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountToday() + "분 " + categoryTimeArrays[goalDataSet.getUnitToday()]);
            }
            tmpAmount = goalDataSet.getCurrentAmountToday();
        } catch (Exception e) {
            /** 목표 설정 안되어 있을 때 **/
            Toast.makeText(this, "오늘의 목표를 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
            Log.e("error", "" + e.getStackTrace());
            e.getStackTrace();
            finish();
        }
    }

    /**
     * 수행량 저장하는 함수
     **/
    public void saveCurrentAmountToEditText() {
        goalDataSet.setCurrentAmountToday(Integer.parseInt(amountOfEdit.getText().toString()));
        if (goalDataSet.getCurrentAmountToday() < goalDataSet.getAmountToday()) {
            Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * upButton, downButton 클릭 시
     **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upButton:
                tmpAmount += 1;
                if (tmpAmount > goalDataSet.getAmountToday()) {
                    tmpAmount = goalDataSet.getAmountToday();
                }
                amountOfEdit.setText("" + tmpAmount);
                break;
            case R.id.downButton:
                tmpAmount -= 1;
                if (tmpAmount < 0) {
                    tmpAmount = 0;
                }
                amountOfEdit.setText("" + tmpAmount);
                break;
        }
    }

    /**
     * 목표 수행량 저장하는 함수
     **/
    public void onClickSaveBtnGoal(View v) {
        saveCurrentAmountToEditText();
        Log.e("qq", "" + goalDataSet.getUnitToday());
        Log.e("qq", "" + goalDataSet.getCurrentAmountToday());
        Log.e("qq", "" + goalDataSet.getAmountToday());
        //물리적양일 경우임 저장버튼이 있는경우는 물리적 양일때만이기때문
        //물리적양 일때 성공하면
        if (goalDataSet.getAmountToday() <= goalDataSet.getCurrentAmountToday()) {
            whereSuccess = SUCCESS_FROM_TODAY;
            int a = (goalDataSet.getBettingGoldToday()) + (goalDataSet.getTotalGold());
            goalDataSet.setTotalGold(a);

            successDialog = new SuccessDialog(this);
            successDialog.show();
            isSuccessToday = true;
        }

    }

    /**
     * 타이머 start 버튼 클릭 시
     **/
    public void onClickTimerStartbtn(View v) {
        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
        try {

            if (isStartButtonClicked) {
                startbtn.setText("Pause");
                starttime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimer, 0);
                isStartButtonClicked = false;
            } else {
                startbtn.setText("Start");
                timerTv.setTextColor(Color.BLUE);
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimer);
                isStartButtonClicked = true;
            }
          //  isStartButtonClicked = !isStartButtonClicked
        } catch (Exception e) {
            Toast.makeText(this, "확인 버튼을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 타이머 End 버튼 클릭시
     **/
    public void onClickTimerEndbtn(View v) {
        String howlongtime;
        String shour;
        String sminute;
        String sseconds;
        int ihowlongtime;

        TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
        howlongtime = stopWatchtv.getText().toString();
        shour = howlongtime.substring(0, 2);
        sminute = howlongtime.substring(3, 5);
        sseconds = howlongtime.substring(6);
        ihowlongtime = Integer.valueOf(shour) * 60 * 60 + Integer.valueOf(sminute) * 60 + Integer.valueOf(sseconds);
        ihowlongtime = ihowlongtime / 60;

        int temp = goalDataSet.getCurrentAmountToday();
        temp += ihowlongtime;
        goalDataSet.setCurrentAmountToday(temp);
        timeOfCurrenttv.setText("수행 시간 : " + goalDataSet.getCurrentAmountToday() + "분");

        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        secs = 0;
        mins = 0;
        hours = 0;
        handler.removeCallbacks(updateTimer);
        stopWatchtv.setText("00:00:00");
        startbtn.setVisibility(View.VISIBLE);
        startbtn.setText("Start");
        isStartButtonClicked = true;
        if (goalDataSet.getCurrentAmountToday() < goalDataSet.getAmountToday()) {
            Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }

        else if ((goalDataSet.getUnitToday() == 0) && (goalDataSet.getCurrentAmountToday() >= goalDataSet.getAmountToday())) {  //이상이고 성공하면
            whereSuccess = SUCCESS_FROM_TODAY;

            int a = (goalDataSet.getBettingGoldToday()) + (goalDataSet.getTotalGold());
            goalDataSet.setTotalGold(a);

            successDialog = new SuccessDialog(this);
            successDialog.show();
            isSuccessToday = true;

        } else {  //이하        나중에 이상이고 실패할때도 else if로 처리하기

        }
    }


    /**
     * 타이머를 관리하는 쓰레드
     **/
    public Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            hours = mins / 60;
            secs = secs % 60;
            timerTv.setText("" + String.format("%02d", hours) + ":" + "" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            handler.postDelayed(this, 0);
        }
    };

}
