package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by taek_aaa on 2017. 1. 17..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1500); //2000에서 1500으로 줄임
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

