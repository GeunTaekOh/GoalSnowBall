package com.taek_aaa.goalsnowball;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.taek_aaa.goalsnowball.MainActivity.goalDataSet;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class WeekGoalDialog extends Dialog implements View.OnClickListener{
    EditText editText;
    String textContents;
    TextView title;

    public WeekGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_weekgoal);

        editText = (EditText) findViewById(R.id.weekDialogEditText);
        findViewById(R.id.weekDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.weekDialogExitButton).setOnClickListener(this);
        findViewById(R.id.weekDialogX).setOnClickListener(this);
        title = (TextView)findViewById(R.id.title_week_dialog);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weekDialogConfirmButton:
                textContents = editText.getText().toString();
                /*if(textContents.equals("")){
                    goalDataSet.isWeekGoal=false;
                }*/
                goalDataSet.setWeekGoal(textContents);
                Log.e("test",goalDataSet.getWeekGoal());
                Log.e("test",""+goalDataSet.isWeekGoal);
                if(goalDataSet.isWeekGoal==true){
                    title.setText("이번주의 목표를 수정하세요.");
                }else{
                    title.setText("이번주의 목표를 추가하세요.");
                }
                dismiss();
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
