package com.taek_aaa.goalsnowball.activity;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public interface AchievementRateInterface {
    void drawGoal();

    void drawGoalAmount(String str) throws Exception;

    void drawCurrentAmount(String str) throws Exception;

    void drawPercent(String str) throws Exception;

    void drawRemainAmount(String str) throws Exception;

    void drawBettingGold();

    void drawBettingResult(String str) throws Exception;

    void drawDue() throws Exception;
}
