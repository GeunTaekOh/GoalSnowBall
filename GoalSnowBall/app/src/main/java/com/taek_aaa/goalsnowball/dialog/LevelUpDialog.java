package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by taek_aaa on 2017. 2. 14..
 */

public class LevelUpDialog extends Dialog {
    int tune;
    private Context c;
    SoundPool soundPool;
    UserDBManager userDBManager;
    ImageView leveluplogo;
    DataController dataController;

    public LevelUpDialog(Context context) {
        super(context);
        this.c = context;
        setContentView(R.layout.dialog_levelup);
        dataController = new DataController();


        leveluplogo = (ImageView) findViewById(R.id.levelup);
        userDBManager = new UserDBManager(getContext(), "userdb.db", null, 1);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(leveluplogo);
        Glide.with(getContext()).load(R.raw.levelupimage).into(imageViewTarget);

        playLevelUpSound();

        dataController.setPreferencesLevelUpFlag(c, 0);

    }

    private void playLevelUpSound() {
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if ((mAudioManager.getRingerMode() == 2) && (userDBManager.getIsSound() == 1)) {                       //소리모드일때만 소리 출력
            soundPool = new SoundPool(1, STREAM_MUSIC, 0);
            tune = soundPool.load(getContext(), R.raw.levelup, 1);
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
