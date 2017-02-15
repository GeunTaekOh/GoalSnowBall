package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
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

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class GoalDialog extends Dialog implements GoalDialogInterface{
    EditText editTextContents, editTextAmonut, bettingGoldet,hiddenEt;
    String textContents;
    TextView title;
    RadioGroup radioGroup;
    Spinner spinner;
    RadioButton timeRadio, physicalRadio;
    int checkedId, default_radioButton_id, bettinggold;
    UserDBManager userDBManager;
    DBManager dbManager;
    int tempUnit = 0;
    DBData dbData = new DBData();
    int getMinuteValue, gethourValue;
    int textAmount;
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
                    hiddenEt.setVisibility(View.GONE);
                } else {
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.upAndDown, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter2);
                    editTextAmonut.setHint("시");
                    hiddenEt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void init() {
        userDBManager = new UserDBManager(getContext(), "userdb.db", null, 1);
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
        hiddenEt = (EditText)findViewById(R.id.hiddenEt);
    }

    public int returnGold(int from){
        int result=0;

        if (userDBManager.getGold() <= 0) {
            result = 2;
        } else {
            if(from==FROM_TODAY){
                result = userDBManager.getGold() / 4;
            }else if(from==FROM_WEEK){
                result = userDBManager.getGold() / 2;
            }else{
                result = userDBManager.getGold();
            }
        }
        return result;
    }

    public String bettingToastMessage(int from){
        String msg="";

        if (userDBManager.getGold() <= 5) {
            msg = "보유 Gold가 작아서 2Gold 이하만 베팅 가능합니다.";
        } else {
            if(from==FROM_TODAY){
                msg = "배팅액은 총 보유 골드의 25%를 넘을 수 없습니다.";
            }else if(from==FROM_WEEK){
                msg = "배팅액은 총 보유 골드의 절반을 넘을 수 없습니다.";
            }else{
                msg = "배팅액은 총 보유 골드를 넘을 수 없습니다.";
            }
        }
        return msg;
    }

}
