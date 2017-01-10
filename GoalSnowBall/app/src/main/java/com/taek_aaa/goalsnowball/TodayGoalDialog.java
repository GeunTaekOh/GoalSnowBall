package com.taek_aaa.goalsnowball;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by taek_aaa on 2017. 1. 10..
 */

public class TodayGoalDialog extends Dialog implements View.OnClickListener {
    EditText editText;
    String textContents;

    public TodayGoalDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_todaygoal);
        editText = (EditText) findViewById(R.id.todayDialogEditText);

        findViewById(R.id.todayDialogConfirmButton).setOnClickListener(this);
        findViewById(R.id.todayDialogExitButton).setOnClickListener(this);
        findViewById(R.id.todayDialogX).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.todayDialogConfirmButton:
                textContents = editText.getText().toString();
                Log.e("test", textContents);
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
