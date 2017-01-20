package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.SoundPool;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBManager;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 17..
 */

public class SuccessDialog extends Dialog {
    public static int whereSuccess;
    final public static int SUCCESS_FROM_TODAY = 60001;
    final public static int SUCCESS_FROM_WEEK = 60002;
    final public static int SUCCESS_FROM_MONTH = 60003;
    public static int getGoldToday;
    public static int getGoldWeek;
    public static int getGoldMonth;

    ImageView fire, coin;
    TextView msg;
    SoundPool soundPool;
    int tune;

    DBManager dbManager;

    public SuccessDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_success);
        dbManager = new DBManager(getContext(), "goaldb.db", null, 1);

        fire = (ImageView) findViewById(R.id.fireWork);
        coin = (ImageView) findViewById(R.id.coin);
        msg = (TextView) findViewById(R.id.SuccessCoinMsg);

        soundPool = new SoundPool(1, STREAM_MUSIC, 0);
        tune = soundPool.load(getContext(), R.raw.clap, 1);
        //soundPool.play(tune, 1, 1, 0, 0, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(tune, 1, 1, 0, 0, 1);
            }
        });

        GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(fire);
        Glide.with(getContext()).load(R.raw.firework2).into(imageViewTarget1);

        GlideDrawableImageViewTarget imageViewTarget2 = new GlideDrawableImageViewTarget(coin);
        Glide.with(getContext()).load(R.raw.coinmotion).into(imageViewTarget2);
        //오늘목표달성했을때

        if (whereSuccess == SUCCESS_FROM_TODAY) {
            msg.setText("" + goalDataSet.getBettingGoldToday() + "Gold를 획득하였습니다.");
            getGoldToday = goalDataSet.getBettingGoldToday();
            goalDataSet.setBettingGoldToday(0);
            whereSuccess = 0;
        } else if (whereSuccess == SUCCESS_FROM_WEEK) {
            msg.setText("" + goalDataSet.getBettingGoldWeek() + "Gold를 획득하였습니다.");
            getGoldWeek = goalDataSet.getBettingGoldWeek();
            goalDataSet.setBettingGoldWeek(0);
            whereSuccess = 0;
        } else if (whereSuccess == SUCCESS_FROM_MONTH) {
            msg.setText("" + goalDataSet.getBettingGoldMonth() + "Gold를 획득하였습니다.");
            getGoldMonth = goalDataSet.getBettingGoldMonth();
            goalDataSet.setBettingGoldMonth(0);
            whereSuccess = 0;
        } else {
            Log.e("error", "successDialog에서 에러");
        }
    }

}
