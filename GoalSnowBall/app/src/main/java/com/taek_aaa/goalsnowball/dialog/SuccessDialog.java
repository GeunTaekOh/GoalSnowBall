package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.taek_aaa.goalsnowball.data.CalendarDatas.TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

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
    CalendarDatas today;
    DataController dataController;
    Context c;

    public SuccessDialog(Context context) {
        super(context);
        this.c=context;
        setContentView(R.layout.dialog_success);
        //dbManagerInstance = new dbManagerInstance(getContext(), "goaldb.db", null, 1);
        dbManagerInstance = DBManager.getInstance(getContext());
        userDBManagerInstance =UserDBManager.getInstance(getContext());
        today = new CalendarDatas(TODAY);
        fire = (ImageView) findViewById(R.id.fireWork);
        coin = (ImageView) findViewById(R.id.coin);
        msg = (TextView) findViewById(R.id.SuccessCoinMsg);

        dataController = new DataController();

        playSuccessSound();

        GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(fire);
        Glide.with(getContext()).load(R.raw.firework2).into(imageViewTarget1);

        GlideDrawableImageViewTarget imageViewTarget2 = new GlideDrawableImageViewTarget(coin);
        Glide.with(getContext()).load(R.raw.coinmotion).into(imageViewTarget2);
        //오늘목표달성했을때

        if (whereSuccess == SUCCESS_FROM_TODAY) {
            success(FROM_TODAY);
        } else if (whereSuccess == SUCCESS_FROM_WEEK) {
            success(FROM_WEEK);
        } else if (whereSuccess == SUCCESS_FROM_MONTH) {
            success(FROM_MONTH);
        } else {
            Log.e("error", "successDialog에서 에러");
        }
    }


    private void success(int from){
        msg.setText("" + dbManagerInstance.getBettingGold(from,TODAY) + "Gold를 획득하였습니다.");
        if(from==FROM_TODAY){
            getGoldToday = dbManagerInstance.getBettingGold(from,TODAY);
        }else if(from == FROM_WEEK){
            getGoldWeek = dbManagerInstance.getBettingGold(from,TODAY);
        }else{
            getGoldMonth = dbManagerInstance.getBettingGold(from,TODAY);
        }
        dbManagerInstance.setIsSuccess(from, 1,TODAY);
        whereSuccess = 0;
    }


    private void playSuccessSound() {
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if ((mAudioManager.getRingerMode() == 2) && (userDBManagerInstance.getIsSound() == 1)) {                       //소리모드일때만 소리 출력
            soundPool = new SoundPool(1, STREAM_MUSIC, 0);
            tune = soundPool.load(getContext(), R.raw.clap, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(tune, 1, 1, 0, 0, 1);

                }
            });
        }
        mAudioManager = null;
    }

}
