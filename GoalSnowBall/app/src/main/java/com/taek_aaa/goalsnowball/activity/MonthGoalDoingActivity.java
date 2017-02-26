package com.taek_aaa.goalsnowball.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.dialog.FailDialog;
import com.taek_aaa.goalsnowball.dialog.SuccessDialog;

import static com.taek_aaa.goalsnowball.data.CommonData.FAIL_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.SUCCESS_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.isSuccessMonth;
import static com.taek_aaa.goalsnowball.data.CommonData.setFailStatus;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;
import static com.taek_aaa.goalsnowball.dialog.FailDialog.whereFail;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.SUCCESS_FROM_MONTH;
import static com.taek_aaa.goalsnowball.dialog.SuccessDialog.whereSuccess;

/**
 * Created by taek_aaa on 2017. 1. 15..
 */

public class MonthGoalDoingActivity extends GoalDoingActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            /** 물리적 양 일때 **/
            if (dbManagerInstance.getType(FROM_MONTH).equals("물리적양")) {
                setContentView(R.layout.activity_goal_amount_doing);
                isAmount = true;
                amountOfEdit = (EditText) findViewById(R.id.doing_current_amount);
                unittv = (TextView) findViewById(R.id.doing_unit);
            } else if (dbManagerInstance.getType(FROM_MONTH).equals("시간적양")) {
                /** 시간적 양 일때 **/
                setContentView(R.layout.activity_goal_time_doing);
                TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
                stopWatchtv.setText("00:00:00");
                timeOfCurrenttv = (TextView) findViewById(R.id.doing_current_time);
                isAmount = false;
            }
            blackboardtv = (TextView) findViewById(R.id.doing_goalAmount);
            successGetGoldtv = (TextView) findViewById(R.id.successGetGoldtv);
            doingGoaltv = (TextView) findViewById(R.id.doing_goal);

            if (isAmount) {
                amountOfEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        amountOfEdit.setText("" + dbManagerInstance.getCurrentAmount(FROM_MONTH));
                    }
                });

                blackboardtv.setText("목표량 : " + dbManagerInstance.getGoalAmount(FROM_MONTH) + "" + dbManagerInstance.getUnit(FROM_MONTH));
                unittv.setText("" + dbManagerInstance.getUnit(FROM_MONTH));
            } else {
                timeOfCurrenttv.setText("수행 시간 : " + dbManagerInstance.getCurrentAmount(FROM_MONTH) + "분");
                blackboardtv.setText("목표량 : " + dbManagerInstance.getGoalAmount(FROM_MONTH) + "분 " + dbManagerInstance.getUnit(FROM_MONTH));
            }

            successGetGoldtv.setText("성공시 획득 골드 : " + "" + dbManagerInstance.getBettingGold(FROM_MONTH) + "Gold");
            doingGoaltv.setText("이번달의 목표 : " + dbManagerInstance.getGoal(FROM_MONTH));
        } catch (Exception e) {
            /** 목표 설정 안되어 있을 때 **/
            Log.e("error", "" + e.getStackTrace());
            Toast.makeText(this, "이번달의 목표를 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
        tmpAmount = dbManagerInstance.getCurrentAmount(FROM_MONTH);
    }


    /**
     * 목표 수행량 저장하는 함수
     **/
    public void onClickSaveBtnGoal(View v) {
        if (dbManagerInstance.getIsSuccess(FROM_MONTH) == FAIL_STATUS) {
            Toast.makeText(context, "이미 실패하였습니다. 저장하지 못하였습니다.", Toast.LENGTH_SHORT).show();
        } else if (dbManagerInstance.getIsSuccess(FROM_MONTH) == SUCCESS_STATUS) {
            Toast.makeText(this, "이미 성공하여서 Gold를 수령했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            saveCurrentAmountToEditText(FROM_MONTH);
            if (dbManagerInstance.getGoalAmount(FROM_MONTH) <= dbManagerInstance.getCurrentAmount(FROM_MONTH)) {
                whereSuccess = SUCCESS_FROM_MONTH;
                int a = (dbManagerInstance.getBettingGold(FROM_MONTH)) + (userDBManagerInstance.getGold());
                userDBManagerInstance.setGold(a);
                successDialog = new SuccessDialog(this);
                successDialog.show();
                isSuccessMonth = true;

                successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        playCoinSound();
                    }
                });
            }
        }
    }

    /**
     * 타이머 End 버튼 클릭시
     **/
    public void onClickTimerEndbtn(View v) {
        String howlongtime;
        String shour;
        String sminute;
        String sseconds;
        int ihowlongtime;

        TextView stopWatchtv = (TextView) findViewById(R.id.timerTextView);
        howlongtime = stopWatchtv.getText().toString();
        shour = howlongtime.substring(0, 2);
        sminute = howlongtime.substring(3, 5);
        sseconds = howlongtime.substring(6);
        ihowlongtime = Integer.valueOf(shour) * 60 * 60 + Integer.valueOf(sminute) * 60 + Integer.valueOf(sseconds);
        ihowlongtime = ihowlongtime / 60;

        int temp = dbManagerInstance.getCurrentAmount(FROM_MONTH);
        temp += ihowlongtime;

        timeOfCurrenttv.setText("수행 시간 : " + dbManagerInstance.getCurrentAmount(FROM_MONTH) + "분");

        Button startbtn = (Button) findViewById(R.id.timerStartbtn);

        if (dbManagerInstance.getIsSuccess(FROM_MONTH) == FAIL_STATUS) {
            Toast.makeText(getBaseContext(), "이미 실패하였습니다. 저장하지 못하였습니다.", Toast.LENGTH_SHORT).show();
        } else if (dbManagerInstance.getIsSuccess(FROM_MONTH) == SUCCESS_STATUS) {
            Toast.makeText(getBaseContext(), "이미 성공하여서 Gold를 수령했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            dbManagerInstance.setCurrentAmount(FROM_MONTH, temp);
            if (dbManagerInstance.getUnit(FROM_MONTH).equals("이상") && dbManagerInstance.getCurrentAmount(FROM_MONTH) >= dbManagerInstance.getGoalAmount(FROM_MONTH)) {      //이상이고 성공
                whereSuccess = SUCCESS_FROM_MONTH;
                int a = (dbManagerInstance.getBettingGold(FROM_MONTH)) + (userDBManagerInstance.getGold());
                userDBManagerInstance.setGold(a);

                successDialog = new SuccessDialog(this);
                successDialog.show();
                isSuccessMonth = true;
                successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        playCoinSound();
                    }
                });

            } else if (dbManagerInstance.getUnit(FROM_MONTH).equals("이하") && dbManagerInstance.getCurrentAmount(FROM_MONTH) >= dbManagerInstance.getGoalAmount(FROM_MONTH)) {       //이하이고 실패
                dbManagerInstance.setIsSuccess(FROM_MONTH,3);
                dataController.setPreferencesFailFlag(context, 1);

                whereFail = FROM_WEEK;
                int a = (userDBManagerInstance.getGold() - dbManagerInstance.getBettingGold(FROM_TODAY));
                userDBManagerInstance.setGold(a);

                //totalLooseCoin = dbManagerInstance.getBettingGold(FROM_MONTH);
                failDialog = new FailDialog(this);
                failDialog.show();

                setFailStatus(false);

            } else {          // 그냥 단순히 저장되는 경우
                Toast.makeText(getBaseContext(), "수고하셨어요. 수행량이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        timerInit();
        stopWatchtv.setText("00:00:00");
        startbtn.setVisibility(View.VISIBLE);
        startbtn.setText("Start");
    }


    /**
     * upButton, downButton 클릭 시
     **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upButton:
                tmpAmount += 1;
                if (tmpAmount > dbManagerInstance.getGoalAmount(FROM_MONTH)) {
                    tmpAmount = dbManagerInstance.getGoalAmount(FROM_MONTH);
                }
                amountOfEdit.setText("" + tmpAmount);
                break;
            case R.id.downButton:
                tmpAmount -= 1;
                if (tmpAmount < 0) {
                    tmpAmount = 0;
                }
                amountOfEdit.setText("" + tmpAmount);
                break;
        }
    }
}
