package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;
import com.taek_aaa.goalsnowball.dialog.SuccessDialog;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class GoalDoingActivity extends Activity implements GoalDoingInterface{

    TextView doingGoaltv, unittv;
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
    UserDBManager userDBManager;
    DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        userDBManager = new UserDBManager(getBaseContext(), "user.db", null, 1);

    }


    /**
     * 타이머 start 버튼 클릭 시
     **/
    public void onClickTimerStartbtn(View v) {
        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
        if (isStartButtonClicked) {
            startbtn.setText("Pause");
            starttime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimer, 0);
        } else {
            startbtn.setText("Start");
            timerTv.setTextColor(Color.BLUE);
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
        }
        isStartButtonClicked = !isStartButtonClicked;

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

    public void timerInit(){
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        secs = 0;
        mins = 0;
        hours = 0;
        handler.removeCallbacks(updateTimer);
        isStartButtonClicked = true;
    }

}

