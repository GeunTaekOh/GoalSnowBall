package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.taek_aaa.goalsnowball.data.CommonData.totalLooseCoin;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

/**
 * Created by taek_aaa on 2017. 1. 29..
 */

public class FailDialog extends Dialog {
    TextView failMsg, failCoinMsg;
    SoundPool soundPool;
    int tune;
    private Context c;
    DataController dataController;
    public static int whereFail;


    public FailDialog(Context context) {
        super(context);
        this.c = context;
        setContentView(R.layout.dialog_fail);
        context = getContext();

        dataController = new DataController();
        userDBManagerInstance = UserDBManager.getInstance(getContext());
        dbManagerInstance = DBManager.getInstance(getContext());

        failMsg = (TextView)findViewById(R.id.failMsg);
        failCoinMsg = (TextView)findViewById(R.id.failCoinMsg);
        totalLooseCoin = dataController.getPreferencesLooseGold(c);
        //failCoinMsg.setText("실패하여서 총 "+totalLooseCoin+" Gold을 잃었습니다.");
        failCoinMsg.setText("실패하여서 총 "+dbManagerInstance.getBettingGold(whereFail)+"Gold를 잃었습니다.");
        int gold = userDBManagerInstance.getGold();
        gold -= totalLooseCoin;
        userDBManagerInstance.setGold(gold);
        totalLooseCoin = 0;
        dataController.setPreferencesLooseGold(c,0);


        playFailSound();

        dataController.setPreferencesFailFlag(context, 0);
    }

    private void playFailSound(){
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if ((mAudioManager.getRingerMode() == 2 ) && (userDBManagerInstance.getIsSound()==1)){                       //소리모드일때만 소리 출력
            soundPool = new SoundPool(1, STREAM_MUSIC, 0);
            tune = soundPool.load(getContext(), R.raw.failhorn, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundPool.play(tune, 1, 1, 0, 0, 1);
                }
            });
        }
        mAudioManager=null;
    }
}
