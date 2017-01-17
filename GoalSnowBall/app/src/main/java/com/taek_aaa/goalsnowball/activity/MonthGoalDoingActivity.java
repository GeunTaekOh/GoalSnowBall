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

import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 15..
 */

public class MonthGoalDoingActivity extends Activity {

    TextView doingGoalMonthtv;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            /** 물리적 양 일때 **/
            if (goalDataSet.getTypeMonth().equals("물리적양")) {
                setContentView(R.layout.activity_month_goal_amount_doing);
                Log.e("aa", "물리적양");
                isAmount = true;
                amountOfEdit = (EditText) findViewById(R.id.doing_current_amount_month);
                amountOfEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        amountOfEdit.setText("" + goalDataSet.getCurrentAmountMonth());
                    }
                });
            } else {
                /** 시간적 양 일때 **/
                setContentView(R.layout.activity_month_goal_time_doing);
                Log.e("aa", "시간적양");

                TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
                stopWatchtv.setText("00:00:00");
                timeOfCurrenttv = (TextView) findViewById(R.id.doing_current_time_month);
                timeOfCurrenttv.setText("수행 시간 : " + goalDataSet.getCurrentMinuteMonth() + "분");
                isAmount = false;
            }

            successGetGoldtv = (TextView)findViewById(R.id.successGetGoldtv);
            successGetGoldtv.setText("성공시 획득 골드 : "+""+goalDataSet.getBettingGoldMonth()+"Gold");
            blackboardtv = (TextView) findViewById(R.id.doing_goalAmount_month);
            doingGoalMonthtv = (TextView) findViewById(R.id.doing_goal_month);
            doingGoalMonthtv.setText("이번달의 목표 : " + goalDataSet.getMonthGoal());
            if (isAmount) {
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountMonth() + "" + categoryPhysicalArrays[goalDataSet.getUnitMonth()]);
                TextView unittv = (TextView) findViewById(R.id.doing_unit_month);
                unittv.setText("" + categoryPhysicalArrays[goalDataSet.getUnitMonth()]);
            } else {
                blackboardtv.setText("목표량 : " + goalDataSet.getAmountMonth() + "분 " + categoryTimeArrays[goalDataSet.getUnitMonth()]);
            }
            tmpAmount = goalDataSet.getCurrentAmountMonth();
        } catch (Exception e) {
            /** 목표 설정 안되어 있을 때 **/
            Toast.makeText(this, "이번달의 목표를 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
            Log.e("error", "" + e.getStackTrace());
            e.getStackTrace();
            finish();
        }
    }

    /**
     * 수행량 저장하는 함수
     **/
    public void saveCurrentAmountToEditText() {
        goalDataSet.setCurrentAmountMonth(Integer.parseInt(amountOfEdit.getText().toString()));
        Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    /**
     * upButton, downButton 클릭 시
     **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upButton:
                tmpAmount += 1;
                if (tmpAmount > goalDataSet.getAmountMonth()) {
                    tmpAmount = goalDataSet.getAmountMonth();
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
        finish();

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

        int temp = goalDataSet.getCurrentMinuteMonth();
        temp += ihowlongtime;
        goalDataSet.setCurrentMinuteMonth(temp);
        timeOfCurrenttv.setText("수행 시간 : " + goalDataSet.getCurrentMinuteMonth() + "분");

        Log.i("test", String.valueOf(howlongtime));
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
        Log.i("test", "찍힘");

        Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
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