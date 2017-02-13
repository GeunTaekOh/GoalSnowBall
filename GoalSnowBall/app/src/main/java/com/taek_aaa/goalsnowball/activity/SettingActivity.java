package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

/**
 * Created by taek_aaa on 2017. 2. 9..
 */

public class SettingActivity extends Activity {

    SwitchCompat notiSwtich, soundSwitch;
    UserDBManager userDBManager;
    CheckBox checkBox;
    DBManager dbManager;
    SQLiteDatabase db, db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userDBManager = new UserDBManager(getBaseContext(), "userdb.db", null, 1);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        notiSwtich=(SwitchCompat)findViewById(R.id.switchButton);
        soundSwitch=(SwitchCompat)findViewById(R.id.switchButton2);
        checkBox = (CheckBox)findViewById(R.id.checkbox);

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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                db = dbManager.getWritableDatabase();
                db2 = userDBManager.getWritableDatabase();

                if(b){
                    AlertDialog.Builder adb = new AlertDialog.Builder(SettingActivity.this);

                    adb
                            .setTitle("경고")
                            .setCancelable(false)
                            .setMessage("정말 데이터베이스를 삭제하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  //  dbManager.onUpgrade(db, 1, 2);
                                    dbManager.deleteAll(db);
                                    dbManager.close();
                                    userDBManager.onUpgrade(db2,1,2);
                                    userDBManager.close();

                                    Toast.makeText(SettingActivity.this, "DB를 삭제하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog ad = adb.create();
                    ad.show();

                }
            }
        });
    }
}
