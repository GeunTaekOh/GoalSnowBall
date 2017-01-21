package com.taek_aaa.goalsnowball.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBData;

import static com.taek_aaa.goalsnowball.activity.MainActivity.FROM_TODAY;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryPhysicalArrays;
import static com.taek_aaa.goalsnowball.activity.MainActivity.categoryTimeArrays;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class TodayGoalDialog extends GoalDialog implements View.OnClickListener {

    DBData dbData = new DBData();
    int tempUnit = 0;

    public TodayGoalDialog(Context context) {
        super(context);

        title.setText("오늘의 목표를 입력하세요.");
        findViewById(R.id.DialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.DialogExitButton).setOnClickListener(this);
        findViewById(R.id.DialogX).setOnClickListener(this);
        bettinggold = userDBManager.getGold() / 4;
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
                Log.e("test", "" + checkedId);
                try {
                    CalendarDatas today = new CalendarDatas();
                    bettinggold = userDBManager.getGold() / 2;
                    if (Integer.parseInt(bettingGoldet.getText().toString()) > bettinggold) {
                        Toast.makeText(getContext(), "배팅액은 총 보유 골드의 25%를 넘을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        int textAmount = Integer.parseInt(editTextAmonut.getText().toString());
                        textContents = editTextContents.getText().toString();
                        if (textContents.equals("")) {
                             throw new Exception();
                        }

                        if (physicalRadio.isChecked()) {
                            dbData.type = "물리적양";
                        } else {
                            dbData.type = "시간적양";
                        }


                        dbData.goalAmount = textAmount;
                        dbData.goal = textContents;


                        if (dbManager.hasGoal(today.cYear,today.cMonth,today.cdate,FROM_TODAY)) {
                            title.setText("오늘의 목표를 수정하세요.");
                            editTextContents.setHint("목표를 수정하세요.");
                        } else {
                            title.setText("오늘의 목표를 추가하세요.");
                            editTextContents.setHint("목표를 추가하세요.");
                        }
                        dbData.bettingGold = Integer.parseInt(bettingGoldet.getText().toString());
                    }
                    dismiss();

                    if (dbData.type == "물리적양") {
                        dbData.unit = categoryPhysicalArrays[tempUnit];
                    } else {
                        dbData.unit = categoryTimeArrays[tempUnit];
                    }


                    Log.e("dbdata", "" + today.cYear);
                    Log.e("dbdata", "" + today.cMonth);
                    Log.e("dbdata", "" + today.cdate);
                    Log.e("dbdata", "" + FROM_TODAY);
                    Log.e("dbdata", "" + dbData.goal);
                    Log.e("dbdata", "" + dbData.type);
                    Log.e("dbdata", "" + dbData.goalAmount);
                    Log.e("dbdata", "" + dbData.unit);
                    Log.e("dbdata", "" + dbData.bettingGold);
                    dbManager.insert(today.cYear, today.cMonth, today.cdate, FROM_TODAY, dbData.goal, dbData.type, dbData.goalAmount, dbData.unit, 0, dbData.bettingGold, 0);


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