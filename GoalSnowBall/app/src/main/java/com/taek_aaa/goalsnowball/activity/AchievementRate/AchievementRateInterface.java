package com.taek_aaa.goalsnowball.activity.AchievementRate;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public interface AchievementRateInterface {
     void drawGoal(int from);

    void drawGoalAmount(int from, String str) throws Exception;

    void drawCurrentAmount(int from, String type) throws Exception;

    void drawPercentProgressBar(int from, String type) throws Exception;

    void drawRemainAmount(int from, String type) throws Exception;

    void drawBettingGold(int from);

    void drawBettingResult(int from);

    void drawDueTime(int from);
}

