package com.taek_aaa.goalsnowball;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class GoalDataSet {
    private String todayGoal;
    private String weekGoal;
    private String monthGoal;
    public boolean isTodayGoal = false;
    public boolean isWeekGoal = false;
    public boolean isMonthGoal = false;
    private String typeToday;
    private int amountToday;
    private int unitToday;






    private String typeWeek;
    private int amountWeek;
    private int unitWeek;

    private String typeMonth;
    private int amountMonth;
    private int unitMonth;


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
}
