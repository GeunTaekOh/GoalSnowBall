package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;
import com.taek_aaa.goalsnowball.dialog.FailDialog;
import com.taek_aaa.goalsnowball.dialog.SuccessDialog;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class GoalDoingActivity extends Activity implements GoalDoingInterface {

    TextView doingGoaltv, unittv,blackboardtv, timeOfCurrenttv, successGetGoldtv;
    EditText amountOfEdit;
    Boolean isAmount;
    Boolean isStartButtonClicked = true;      //start, pause구분
    long starttime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updatedtime = 0L;
    int secs = 0, mins = 0, hours = 0, tune, tmpAmount;
    Handler handler = new Handler();
    SoundPool soundPool;
    SuccessDialog successDialog;
    UserDBManager userDBManager;
    DBManager dbManager;
    Context context;
    DataController dataController;
    FailDialog failDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        userDBManager = new UserDBManager(getBaseContext(), "userdb.db", null, 1);
        context = getBaseContext();
        dataController = new DataController();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#99BADD"));
        }

    }

    /**
     * 타이머 start 버튼 클릭 시
     **/
    public void onClickTimerStartbtn(View v) {
        Button startbtn = (Button) findViewById(R.id.timerStartbtn);
        final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
        if (isStartButtonClicked) {
            startbtn.setText("Pause");
            starttime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimer, 0);
        } else {
            startbtn.setText("Start");
            timerTv.setTextColor(Color.BLUE);
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
        }
        isStartButtonClicked = !isStartButtonClicked;

    }

    /**
     * 타이머를 관리하는 쓰레드
     **/
    public Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView timerTv = (TextView) findViewById(R.id.timerTextView);
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            hours = mins / 60;
            secs = secs % 60;
            timerTv.setText("" + String.format("%02d", hours) + ":" + "" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            handler.postDelayed(this, 0);
        }
    };

    /** 시간 초기화 **/
    public void timerInit() {
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        secs = 0;
        mins = 0;
        hours = 0;
        handler.removeCallbacks(updateTimer);
        isStartButtonClicked = true;
    }

    /** 성공시 동전 획득 소리 출력 **/
    public void playCoinSound() {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if ((mAudioManager.getRingerMode() == 2) && (userDBManager.getIsSound()==1)){
            soundPool = new SoundPool(1, STREAM_MUSIC, 0);
            tune = soundPool.load(this, R.raw.coin, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(tune, 1, 1, 0, 0, 1);
                }
            });
        }
    }


    /**
     * 수행량 저장하는 함수
     **/
    public void saveCurrentAmountToEditText(int from) {
        dbManager.setCurrentAmount(from, Integer.parseInt(amountOfEdit.getText().toString()));
        if (dbManager.getCurrentAmount(from) < dbManager.getGoalAmount(from)) {
            Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}


