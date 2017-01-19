package com.taek_aaa.goalsnowball.activity;

/**
 * Created by taek_aaa on 2017. 1. 19..
 */

public interface AchievementRateInterface {
    public void drawGoal();
    public void drawGoalAmount(String str) throws Exception;
    public void drawCurrentAmount(String str)throws Exception;
    public void drawPercent(String str)throws Exception;
    public void drawRemainAmount(String str)throws Exception;
    public void drawBettingGold();
    public void drawBettingResult(String str)throws Exception;
    public void drawDue()throws Exception;
    public void init();
}
