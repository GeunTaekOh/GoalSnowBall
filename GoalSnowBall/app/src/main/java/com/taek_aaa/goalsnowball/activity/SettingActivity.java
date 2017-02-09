package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.UserDBManager;

/**
 * Created by taek_aaa on 2017. 2. 9..
 */

public class SettingActivity extends Activity {

    SwitchCompat notiSwtich, soundSwitch;
    UserDBManager userDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userDBManager = new UserDBManager(getBaseContext(), "userdb.db", null, 1);
        notiSwtich=(SwitchCompat)findViewById(R.id.switchButton);
        soundSwitch=(SwitchCompat)findViewById(R.id.switchButton2);

        Log.e("rmsxor","&&&&&&&&&"+userDBManager.getIsNoti());
        Log.e("rmsxor","&&&&&&&&&&"+userDBManager.getIsSound());
        if(userDBManager.getIsNoti()==1){
            notiSwtich.setChecked(true);
        }else{
            notiSwtich.setChecked(false);
        }

        if(userDBManager.getIsSound()==1){
            soundSwitch.setChecked(true);
        }else{
            soundSwitch.setChecked(false);
        }

        notiSwtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Snackbar.make(buttonView, "상단바에서 알림을 받으실 수 있습니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    userDBManager.setIsNoti(1);

                }else{
                    Snackbar.make(buttonView, "상단바에서 알림을 받으실 수 없습니다.", Snackbar.LENGTH_LONG)
                            .setAction("ACTION", null).show();
                    userDBManager.setIsNoti(0);
                }
            }
        });


        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    Snackbar.make(compoundButton, "소리모드일시 소리를 재생합니다.", Snackbar.LENGTH_SHORT)
                            .setAction("ACTION", null).show();
                    userDBManager.setIsSound(1);
                }else{
                    Snackbar.make(compoundButton, "소리모드일시에도 소리를 재생하지 않습니다.", Snackbar.LENGTH_SHORT)
                            .setAction("ACTION", null).show();
                    userDBManager.setIsSound(0);
                }

            }
        });



    }
}
