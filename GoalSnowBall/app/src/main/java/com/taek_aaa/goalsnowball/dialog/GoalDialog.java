package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.DBData;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class GoalDialog extends Dialog implements GoalDialogInterface{
    EditText editTextContents, editTextAmonut, bettingGoldet;
    String textContents;
    TextView title;
    RadioGroup radioGroup;
    Spinner spinner;
    RadioButton timeRadio, physicalRadio;
    int checkedId;
    int default_radioButton_id;
    int bettinggold;
    UserDBManager userDBManager;
    DBManager dbManager;
    int tempUnit = 0;
    DBData dbData = new DBData();

    public GoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_goal);
        dbManager = new DBManager(getContext(), "goaldb.db", null, 1);
        init();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ((radioGroup.getCheckedRadioButtonId()) == default_radioButton_id) {
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

    }


    public void init() {
        userDBManager = new UserDBManager(getContext(), "user.db", null, 1);
        editTextContents = (EditText) findViewById(R.id.DialogEditText);
        title = (TextView) findViewById(R.id.title_dialog);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.spinner);
        editTextAmonut = (EditText) findViewById(R.id.setting_amount);
        physicalRadio = (RadioButton) findViewById(R.id.amountOfPhysical);
        timeRadio = (RadioButton) findViewById(R.id.amountOfTime);
        physicalRadio.setChecked(true);
        default_radioButton_id = physicalRadio.getId();
        checkedId = radioGroup.getCheckedRadioButtonId();
        bettingGoldet = (EditText) findViewById(R.id.bettingGold);

    }



}
