package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 15..
 */

public class TodayGoalDoingActivity extends Activity {

    TextView doingGoalTodaytv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_goal_doing);

        doingGoalTodaytv = (TextView) findViewById(R.id.doing_goal_today);
        if(goalDataSet.isTodayGoal==false) {
            doingGoalTodaytv.setText("오늘의 목표를 먼저 입력하세요.");
        }else {
            doingGoalTodaytv.setText("오늘의 목표 : " + goalDataSet.getTodayGoal());
        }

    }
}
