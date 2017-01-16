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

public class TodayGoalDialog extends Dialog implements View.OnClickListener {
    EditText editTextContents, editTextAmonut;
    String textContents;
    TextView title;
    RadioGroup radioGroup;
    Spinner spinner;
    RadioButton timeRadio, physicalRadio;
    int checkedId;
    int default_radioButton_id_today;

    public TodayGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_todaygoal);

        editTextContents = (EditText) findViewById(R.id.todayDialogEditText);
        findViewById(R.id.todayDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.todayDialogExitButton).setOnClickListener(this);
        findViewById(R.id.todayDialogX).setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_today_dialog);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_today);
        spinner = (Spinner) findViewById(R.id.today_spinner);
        editTextAmonut = (EditText) findViewById(R.id.setting_amount_today);
        physicalRadio = (RadioButton) findViewById(R.id.amountOfPhysical_today);
        timeRadio = (RadioButton) findViewById(R.id.amountOfTime_today);
        physicalRadio.setChecked(true);
        default_radioButton_id_today = physicalRadio.getId();
        checkedId = radioGroup.getCheckedRadioButtonId();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ((radioGroup.getCheckedRadioButtonId()) == default_radioButton_id_today) {

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
                goalDataSet.setUnitToday(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.todayDialogConfirmButton:
                Log.e("test", "" + checkedId);
                try {
                    int textAmount = Integer.parseInt(editTextAmonut.getText().toString());
                    textContents = editTextContents.getText().toString();

                    if (physicalRadio.isChecked()) {
                        goalDataSet.setTypeToday("물리적양");
                        Log.e("tt", "" + physicalRadio.getId());
                    } else {
                        goalDataSet.setTypeToday("시간적양");
                        Log.e("tt", "" + timeRadio.getId());
                    }
                /*if(textContents.equals("")){
                    goalDataSet.isTodayGoal=false;
                }*/

                    goalDataSet.setAmountToday(textAmount);
                    goalDataSet.setTodayGoal(textContents);


                    Log.e("test", goalDataSet.getTodayGoal());
                    Log.e("test", "" + goalDataSet.isTodayGoal);


                    if (goalDataSet.isTodayGoal == true) {
                        title.setText("오늘의 목표를 수정하세요.");
                        editTextContents.setHint("목표를 수정하세요.");
                    } else {
                        title.setText("오늘의 목표를 추가하세요.");
                        editTextContents.setHint("목표를 추가하세요.");
                    }
                    Log.e("data", goalDataSet.getTypeToday());
                    Log.e("data", "" + goalDataSet.getAmountToday());
                    Log.e("data", "" + goalDataSet.getUnitToday());
                    Log.e("data", goalDataSet.getTodayGoal());
                    Log.e("data", "" + goalDataSet.isTodayGoal);

                    dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "값을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.todayDialogExitButton:
                dismiss();
                break;
            case R.id.todayDialogX:
                dismiss();
                break;
        }
    }
}
