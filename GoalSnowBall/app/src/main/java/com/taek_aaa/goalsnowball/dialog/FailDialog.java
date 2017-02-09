package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetToday;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetWeek;
import static com.taek_aaa.goalsnowball.data.CommonData.failFlag;

/**
 * Created by taek_aaa on 2017. 1. 29..
 */

public class FailDialog extends Dialog {
    TextView failMsg, failCoinMsg;
    int totalLooseCoin = 0;
    DBManager dbManager;
    UserDBManager userDBManager;
    SoundPool soundPool;
    int tune;

    public FailDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_fail);
        userDBManager = new UserDBManager(getContext(), "userdb.db", null, 1);
        dbManager = new DBManager(getContext(), "goaldb.db", null, 1);

        failMsg = (TextView)findViewById(R.id.failMsg);
        failCoinMsg = (TextView)findViewById(R.id.failCoinMsg);

        if(dbManager.getIsSuccess(FROM_TODAY)==3){
            failBetToday=dbManager.getBettingGold(FROM_TODAY);
            totalLooseCoin += failBetToday;
        }
        if(dbManager.getIsSuccess(FROM_WEEK)==3){
            failBetWeek=dbManager.getBettingGold(FROM_WEEK);
            totalLooseCoin += failBetWeek;
        }
        if(dbManager.getIsSuccess(FROM_MONTH)==3){
            failBetMonth=dbManager.getBettingGold(FROM_MONTH);
            totalLooseCoin += failBetMonth;
        }

        failCoinMsg.setText("실패하여서 총 "+totalLooseCoin+" Gold을 잃었습니다.");
        int gold = userDBManager.getGold();
        gold -= totalLooseCoin;
        userDBManager.setGold(gold);


        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if ((mAudioManager.getRingerMode() == 2 ) && (userDBManager.getIsSound()==1)){                       //소리모드일때만 소리 출력
            soundPool = new SoundPool(1, STREAM_MUSIC, 0);
            tune = soundPool.load(getContext(), R.raw.failhorn, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(tune, 1, 1, 0, 0, 1);
                }
            });

        }

        failFlag = false;
    }
}
