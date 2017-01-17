package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.activity.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class WeekGoalDialog extends Dialog implements View.OnClickListener {
    EditText editTextContents, editTextAmonut, bettingGoldWeeket;
    String textContents;
    TextView title;
    RadioGroup radioGroup;
    Spinner spinner;
    RadioButton timeRadio, physicalRadio;
    int checkedId;
    int default_radioButton_id_week;
    int bettinggold;

    public WeekGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_weekgoal);

        editTextContents = (EditText) findViewById(R.id.weekDialogEditText);
        findViewById(R.id.weekDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.weekDialogExitButton).setOnClickListener(this);
        findViewById(R.id.weekDialogX).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_week_dialog);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_week);
        spinner = (Spinner) findViewById(R.id.week_spinner);
        editTextAmonut = (EditText) findViewById(R.id.setting_amount_week);
        physicalRadio = (RadioButton) findViewById(R.id.amountOfPhysical_week);
        timeRadio = (RadioButton) findViewById(R.id.amountOfTime_week);
        physicalRadio.setChecked(true);
        default_radioButton_id_week = physicalRadio.getId();
        checkedId = radioGroup.getCheckedRadioButtonId();
        bettingGoldWeeket = (EditText) findViewById(R.id.bettingGoldWeek);
        bettinggold = goalDataSet.getTotalGold();
        bettingGoldWeeket.setText(""+bettinggold);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ((radioGroup.getCheckedRadioButtonId()) == default_radioButton_id_week) {

                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.unit, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    editTextAmonut.setHint("양");
                } else {
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.upAndDown, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter2);
                    editTextAmonut.setHint("분");
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                goalDataSet.setUnitWeek(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weekDialogConfirmButton:
                Log.e("test", "" + checkedId);
                try {
                    if (Integer.parseInt(bettingGoldWeeket.getText().toString()) > bettinggold) {
                        Toast.makeText(getContext(), "배팅액은 총 보유 골드량을 넘을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        int textAmount = Integer.parseInt(editTextAmonut.getText().toString());

                        textContents = editTextContents.getText().toString();

                        if (physicalRadio.isChecked()) {
                            goalDataSet.setTypeWeek("물리적양");
                            Log.e("tt", "" + physicalRadio.getId());
                        } else {
                            goalDataSet.setTypeWeek("시간적양");
                            Log.e("tt", "" + timeRadio.getId());
                        }
                /*if(textContents.equals("")){
                    goalDataSet.isweekGoal=false;
                }*/

                        goalDataSet.setAmountWeek(textAmount);
                        goalDataSet.setWeekGoal(textContents);
                        Log.e("test", goalDataSet.getWeekGoal());
                        Log.e("test", "" + goalDataSet.isWeekGoal);


                        if (goalDataSet.isWeekGoal == true) {
                            title.setText("이번주의 목표를 수정하세요.");
                            editTextContents.setHint("목표를 수정하세요.");
                        } else {
                            title.setText("이번주의 목표를 추가하세요.");
                            editTextContents.setHint("목표를 추가하세요.");
                        }
                        Log.e("data", goalDataSet.getTypeWeek());
                        Log.e("data", "" + goalDataSet.getAmountWeek());
                        Log.e("data", "" + goalDataSet.getUnitWeek());
                        Log.e("data", goalDataSet.getWeekGoal());
                        Log.e("data", "" + goalDataSet.isWeekGoal);
                        goalDataSet.setBettingGoldWeek(Integer.parseInt(bettingGoldWeeket.getText().toString()));
                        dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "값을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.weekDialogExitButton:
                dismiss();
                break;
            case R.id.weekDialogX:
                dismiss();
                break;
        }
    }
}
