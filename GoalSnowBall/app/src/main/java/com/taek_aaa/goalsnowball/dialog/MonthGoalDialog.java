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
 * Created by taek_aaa on 2017. 1. 14..
 */

public class MonthGoalDialog extends Dialog implements View.OnClickListener {
    EditText editTextContents, editTextAmonut, bettingGoldMonthet;
    String textContents;
    TextView title;
    RadioGroup radioGroup;
    Spinner spinner;
    RadioButton timeRadio, physicalRadio;
    int checkedId;
    int dafault_radioButton_id_month;
    int bettinggold;

    public MonthGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_monthgoal);

        init();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ((radioGroup.getCheckedRadioButtonId()) == dafault_radioButton_id_month) {

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
                goalDataSet.setUnitMonth(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.monthDialogConfirmButton:
                Log.e("test", "" + checkedId);
                try {
                    bettinggold = goalDataSet.getTotalGold() * 2;

                    if (Integer.parseInt(bettingGoldMonthet.getText().toString()) > bettinggold) {
                        Toast.makeText(getContext(), "배팅액은 총 보유 골드의 2배를 넘을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        int textAmount = Integer.parseInt(editTextAmonut.getText().toString());
                        textContents = editTextContents.getText().toString();
                        if(textContents.equals("")){
                            throw new Exception();
                        }

                        if (physicalRadio.isChecked()) {
                            goalDataSet.setTypeMonth("물리적양");
                            Log.e("tt", "" + physicalRadio.getId());
                        } else {
                            goalDataSet.setTypeMonth("시간적양");
                            Log.e("tt", "" + timeRadio.getId());
                        }
                        goalDataSet.setAmountMonth(textAmount);
                        goalDataSet.setMonthGoal(textContents);

                        Log.e("test", goalDataSet.getMonthGoal());
                        Log.e("test", "" + goalDataSet.isMonthGoal);


                        if (goalDataSet.isMonthGoal == true) {
                            title.setText("이번달의 목표를 수정하세요.");
                            editTextContents.setHint("목표를 수정하세요.");
                        } else {
                            title.setText("이번달의 목표를 추가하세요.");
                            editTextContents.setHint("목표를 추가하세요.");
                        }
                        Log.e("data", goalDataSet.getTypeMonth());
                        Log.e("data", "" + goalDataSet.getAmountMonth());
                        Log.e("data", "" + goalDataSet.getUnitMonth());
                        Log.e("data", goalDataSet.getMonthGoal());
                        Log.e("data", "" + goalDataSet.isMonthGoal);
                        goalDataSet.setBettingGoldMonth(Integer.parseInt(bettingGoldMonthet.getText().toString()));
                        dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "값을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.monthDialogExitButton:
                dismiss();
                break;
            case R.id.monthDialogX:
                dismiss();
                break;
        }
    }

    public void init() {
        editTextContents = (EditText) findViewById(R.id.monthDialogEditText);
        findViewById(R.id.monthDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.monthDialogExitButton).setOnClickListener(this);
        findViewById(R.id.monthDialogX).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_month_dialog);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_month);
        spinner = (Spinner) findViewById(R.id.month_spinner);
        editTextAmonut = (EditText) findViewById(R.id.setting_amount_month);
        physicalRadio = (RadioButton) findViewById(R.id.amountOfPhysical_month);
        timeRadio = (RadioButton) findViewById(R.id.amountOfTime_month);
        physicalRadio.setChecked(true);
        dafault_radioButton_id_month = physicalRadio.getId();
        checkedId = radioGroup.getCheckedRadioButtonId();
        bettingGoldMonthet = (EditText) findViewById(R.id.bettingGoldMonth);
        bettinggold = goalDataSet.getTotalGold() * 2;
        bettingGoldMonthet.setText("" + bettinggold);
    }

}
