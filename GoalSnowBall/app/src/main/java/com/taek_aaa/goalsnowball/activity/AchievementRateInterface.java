package com.taek_aaa.goalsnowball.activity;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public interface AchievementRateInterface {
    void drawGoal();
    void drawGoalAmount(String str) throws Exception;
    void drawCurrentAmount(String type) throws Exception;
    void drawPercentProgressBar(String type) throws Exception;
    void drawRemainAmount(String type) throws Exception;
    void drawBettingGold();
    void drawBettingResult();
    void drawDueTime();
    void draw();
}

