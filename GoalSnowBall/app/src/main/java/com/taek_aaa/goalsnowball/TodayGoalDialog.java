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

public class TodayGoalDialog extends Dialog implements View.OnClickListener{
    EditText editText;
    String textContents;
    TextView title;

    public TodayGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_todaygoal);

        editText = (EditText) findViewById(R.id.todayDialogEditText);
        findViewById(R.id.todayDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.todayDialogExitButton).setOnClickListener(this);
        findViewById(R.id.todayDialogX).setOnClickListener(this);
        title = (TextView)findViewById(R.id.title_today_dialog);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.todayDialogConfirmButton:
                textContents = editText.getText().toString();
                /*if(textContents.equals("")){
                    goalDataSet.isTodayGoal=false;
                }*/
                goalDataSet.setTodayGoal(textContents);
                Log.e("test",goalDataSet.getTodayGoal());
                Log.e("test",""+goalDataSet.isTodayGoal);
                if(goalDataSet.isTodayGoal==true){
                    title.setText("오늘의 목표를 수정하세요.");
                }else{
                    title.setText("오늘의 목표를 추가하세요.");
                }
                dismiss();
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
