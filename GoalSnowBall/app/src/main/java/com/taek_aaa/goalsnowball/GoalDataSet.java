package com.taek_aaa.goalsnowball;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class GoalDataSet {
    private  String todayGoal;
    private  String weekGoal;
    private  String monthGoal;
    boolean isTodayGoal = false;
    boolean isWeekGoal = false;
    boolean isMonthGoal = false;

    public void setTodayGoal(String str){
        this.todayGoal=str;
        this.isTodayGoal=true;
    }
    public void setWeekGoal(String str){
        this.weekGoal=str;
        this.isWeekGoal=true;
    }
    public void setMonthGoal(String str){
        this.monthGoal=str;
        this.isMonthGoal=true;
    }
    public String getTodayGoal(){
        return this.todayGoal;
    }
    public String getWeekGoal(){
        return this.weekGoal;
    }
    public String getMonthGoal(){
        return this.monthGoal;
    }

}
