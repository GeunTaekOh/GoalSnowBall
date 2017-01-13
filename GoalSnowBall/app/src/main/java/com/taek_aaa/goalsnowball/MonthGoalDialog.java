package com.taek_aaa.goalsnowball;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.taek_aaa.goalsnowball.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 14..
 */

public class MonthGoalDialog extends Dialog implements View.OnClickListener{
    EditText editText;
    String textContents;
    TextView title;

    public MonthGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_monthgoal);

        editText = (EditText) findViewById(R.id.monthDialogEditText);
        findViewById(R.id.monthDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.monthDialogExitButton).setOnClickListener(this);
        findViewById(R.id.monthDialogX).setOnClickListener(this);
        title = (TextView)findViewById(R.id.title_month_dialog);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.monthDialogConfirmButton:
                textContents = editText.getText().toString();
                /*if(textContents.equals("")){
                    goalDataSet.isMonthGoal=false;
                }*/
                goalDataSet.setMonthGoal(textContents);
                Log.e("test",goalDataSet.getMonthGoal());
                Log.e("test",""+goalDataSet.isMonthGoal);
                if(goalDataSet.isMonthGoal==true){
                    title.setText("이번달의 목표를 수정하세요.");
                }else{
                    title.setText("이번달의 목표를 추가하세요.");
                }
                dismiss();
                break;
            case R.id.monthDialogExitButton:
                dismiss();
                break;
            case R.id.monthDialogX:
                dismiss();
                break;
        }
    }
}

