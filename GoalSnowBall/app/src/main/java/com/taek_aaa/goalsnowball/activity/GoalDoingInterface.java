package com.taek_aaa.goalsnowball.activity;

import android.view.View;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public interface GoalDoingInterface {
    void onClickTimerStartbtn(View v);
    void timerInit();
    void saveCurrentAmountToEditText(int from);
    void playCoinSound();
}
