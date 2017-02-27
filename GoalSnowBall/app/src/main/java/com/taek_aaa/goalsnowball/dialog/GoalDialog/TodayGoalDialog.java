package com.taek_aaa.goalsnowball.dialog.GoalDialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.data.CalendarDatas.TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.data.CommonData.categoryTimeArrays;
import static com.taek_aaa.goalsnowball.data.CommonData.failBetToday;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class TodayGoalDialog extends GoalDialog implements View.OnClickListener {


    public TodayGoalDialog(Context context) {
        super(context);
        title.setText("오늘의 목표를 입력하세요.");
        findViewById(R.id.DialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.DialogExitButton).setOnClickListener(this);
        findViewById(R.id.DialogX).setOnClickListener(this);

        bettinggold = returnGold(FROM_TODAY);
        if(bettinggold<=5){
            bettinggold = 2;
        }
        bettingGoldet.setText("" + bettinggold);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempUnit = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DialogConfirmButton:
                try {
                    if (Integer.parseInt(bettingGoldet.getText().toString()) > bettinggold) {
                        Toast.makeText(getContext(), "" + bettingToastMessage(FROM_TODAY), Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        String getMinuteValueString = hiddenEt.getText().toString();
                        String gethourValueString = editTextAmonut.getText().toString();
                        if (gethourValueString.equals("")) {
                            gethourValue = 0;
                        } else {
                            gethourValue = Integer.parseInt(editTextAmonut.getText().toString());
                        }

                        if (getMinuteValueString.equals("")) {
                            getMinuteValue = 0;
                        } else {
                            getMinuteValue = Integer.parseInt(hiddenEt.getText().toString());
                        }
                        textAmount = gethourValue * 60 + getMinuteValue;

                        textContents = editTextContents.getText().toString();
                        if (textContents.equals("")) {
                            throw new Exception();
                        }
                        if (physicalRadio.isChecked()) {
                            dbData.type = "물리적양";
                            dbData.unit = categoryPhysicalArrays[tempUnit];
                            textAmount /= 60;
                        } else {
                            dbData.type = "시간적양";
                            dbData.unit = categoryTimeArrays[tempUnit];
                        }
                        dbData.goalAmount = textAmount;
                        dbData.goal = textContents;
                        title.setText("오늘의 목표를 입력하세요.");
                        editTextContents.setHint("목표를 추가하세요.");
                        dbData.bettingGold = Integer.parseInt(bettingGoldet.getText().toString());
                    }
                    if (dbManagerInstance.hasGoal(FROM_TODAY)) {
                        Toast.makeText(getContext(), "이미 오늘의 목표를 설정하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        dbManagerInstance.insert(FROM_TODAY, dbData.goal, dbData.type, dbData.goalAmount, dbData.unit, 0, dbData.bettingGold, 2);
                        failBetToday = dbManagerInstance.getBettingGold(FROM_TODAY,TODAY);
                    }
                    dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "값을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.DialogExitButton:
                dismiss();
                break;
            case R.id.DialogX:
                dismiss();
                break;
        }
    }
}