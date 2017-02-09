package com.taek_aaa.goalsnowball.controller;

/**
 * Created by taek_aaa on 2017. 1. 25..
 */

public class DataController{


    public double makePercent(int current, int goal) {
        double result = 0;
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
        }
        return result;
    }

}
