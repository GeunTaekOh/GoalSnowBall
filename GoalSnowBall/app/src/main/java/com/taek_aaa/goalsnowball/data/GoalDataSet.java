package com.taek_aaa.goalsnowball.data;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class GoalDataSet {

    private int totalGold;

    private String todayGoal;
    public boolean isTodayGoal = false;
    private String typeToday;
    private int amountToday;
    private int unitToday;
    private int currentAmountToday;
    private int currentMinuteToday;
    private int bettingGoldToday;

    private String weekGoal;
    public boolean isWeekGoal = false;
    private String typeWeek;
    private int amountWeek;
    private int unitWeek;
    private int currentAmountWeek;
    private int currentMinuteWeek;
    private int bettingGoldWeek;

    private String monthGoal;
    public boolean isMonthGoal = false;
    private String typeMonth;
    private int amountMonth;
    private int unitMonth;
    private int currentAmountMonth;
    private int currentMinuteMonth;
    private int bettingGoldMonth;


    public void setTodayGoal(String str) {
        this.todayGoal = str;
        this.isTodayGoal = true;
    }

    public void setWeekGoal(String str) {
        this.weekGoal = str;
        this.isWeekGoal = true;
    }

    public void setMonthGoal(String str) {
        this.monthGoal = str;
        this.isMonthGoal = true;
    }

    public void setTypeToday(String ty) {
        this.typeToday = ty;
    }

    public void setAmountToday(int am) {
        this.amountToday = am;
    }

    public void setUnitToday(int un) {
        this.unitToday = un;
    }

    public void setTypeWeek(String ty) {
        this.typeWeek = ty;
    }

    public void setAmountWeek(int am) {
        this.amountWeek = am;
    }

    public void setUnitWeek(int un) {
        this.unitWeek = un;
    }

    public void setTypeMonth(String ty) {
        this.typeMonth = ty;
    }

    public void setAmountMonth(int am) {
        this.amountMonth = am;
    }

    public void setUnitMonth(int un) {
        this.unitMonth = un;
    }

    public void setCurrentAmountToday(int ca) {
        this.currentAmountToday = ca;
    }

    public void setCurrentAmountWeek(int ca) {
        this.currentAmountWeek = ca;
    }

    public void setCurrentAmountMonth(int ca) {
        this.currentAmountMonth = ca;
    }
    public void setCurrentMinuteToday(int cm){
        this.currentMinuteToday = cm;
    }
    public void setCurrentMinuteWeek(int cm){
        this.currentMinuteWeek = cm;
    }
    public void setCurrentMinuteMonth(int cm){
        this.currentMinuteMonth = cm;
    }

    public void setTotalGold(int gold){this.totalGold = gold;}
    public void setBettingGoldToday(int gold){this.bettingGoldToday = gold;}
    public void setBettingGoldWeek(int gold){this.bettingGoldWeek = gold;}
    public void setBettingGoldMonth(int gold){this.bettingGoldMonth = gold;}


    public String getTodayGoal() {
        return this.todayGoal;
    }

    public String getWeekGoal() {
        return this.weekGoal;
    }

    public String getMonthGoal() {
        return this.monthGoal;
    }

    public String getTypeToday() {
        return this.typeToday;
    }

    public int getAmountToday() {
        return this.amountToday;
    }

    public int getUnitToday() {
        return this.unitToday;
    }

    public String getTypeWeek() {
        return this.typeWeek;
    }

    public int getAmountWeek() {
        return this.amountWeek;
    }

    public int getUnitWeek() {
        return this.unitWeek;
    }

    public String getTypeMonth() {
        return this.typeMonth;
    }

    public int getAmountMonth() {
        return this.amountMonth;
    }

    public int getUnitMonth() {
        return this.unitMonth;
    }

    public int getCurrentAmountToday(){return this.currentAmountToday;}

    public int getCurrentAmountWeek(){return this.currentAmountWeek;}

    public int getCurrentAmountMonth(){return this.currentAmountMonth;}

    public int getCurrentMinuteToday(){return this.currentMinuteToday;}

    public int getCurrentMinuteWeek(){return this.currentMinuteWeek;}

    public int getCurrentMinuteMonth(){return this.currentMinuteMonth;}

    public int getBettingGoldToday(){return this.bettingGoldToday;}

    public int getBettingGoldWeek(){return this.bettingGoldWeek;}

    public int getBettingGoldMonth(){return this.bettingGoldMonth;}

    public int getTotalGold(){return  this.totalGold;}

}
