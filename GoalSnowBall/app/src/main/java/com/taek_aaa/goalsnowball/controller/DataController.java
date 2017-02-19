package com.taek_aaa.goalsnowball.controller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by taek_aaa on 2017. 1. 25..
 */

public class DataController extends Activity{


    public double makePercent(int current, int goal) {
        double result = 0;
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
        }
        return result;
    }


    /** preference 값 가져오기 **/
    public int getPreferencesIsFirstOpenApp(Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        int a;
        a = pref.getInt("isFirst", 1);
        return a;
    }

    /** preference 인자값 으로 저장하기 **/
    public void setPreferencesIsFirstOpenApp(Context context, int a) {
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isFirst", a);
        editor.commit();
    }


    /** preference 값 가져오기 **/
    public int getPreferencesIsGradeChange(Context context) {
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        int a;
        a = pref.getInt("isGradeChange", 0);
        return a;
    }

    /** preference 인자값 으로 저장하기 **/
    public void setPreferencesIsGradeChange(Context context, int a) {
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isGradeChange", a);
        editor.commit();
    }

    public int getPreferencesFailFlag(Context context) {
        SharedPreferences pref = context.getSharedPreferences("aaa", MODE_PRIVATE);
        int a;
        a = pref.getInt("failFlag", 0);
        return a;
    }

    /** preference 인자값 으로 저장하기 **/
    public void setPreferencesFailFlag(Context context, int a) {
        SharedPreferences pref = context.getSharedPreferences("aaa", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("failFlag", a);
        editor.commit();
    }

    public int getPreferencesLevelUpFlag(Context context) {
        SharedPreferences pref = context.getSharedPreferences("bbb", MODE_PRIVATE);
        int a;
        a = pref.getInt("levelFlag", 0);
        return a;
    }

    /** preference 인자값 으로 저장하기 **/
    public void setPreferencesLevelUpFlag(Context context, int a) {
        SharedPreferences pref = context.getSharedPreferences("bbb", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("levelFlag", a);
        editor.commit();
    }

    public void setProgressAnimate(ProgressBar progressBar) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.getProgress());
        animation.setDuration(2500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }





}
