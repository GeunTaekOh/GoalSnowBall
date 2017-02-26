package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;

import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

/**
 * Created by taek_aaa on 2017. 2. 26..
 */

public class TimePickerDialog extends Dialog implements View.OnClickListener{
    private Context c;
    TimePicker timePicker;
    static int pickerTime, pickerMinute;


    public TimePickerDialog(Context context) {
        super(context);
        this.c=context;
        setContentView(R.layout.dialog_timepicker);


        findViewById(R.id.DialogPickerCancelButton).setOnClickListener(this);
        findViewById(R.id.DialogPickerConfirmButton).setOnClickListener(this);
        findViewById(R.id.dialogPickerX).setOnClickListener(this);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int time, int minute) {
                pickerTime = time;
                pickerMinute = minute;
            }
        });
    }

    @Override
    public void onClick(View view) {
        String str="";
        switch (view.getId()){
            case R.id.DialogPickerCancelButton:
                dismiss();
                break;
            case R.id.DialogPickerConfirmButton:
                if(pickerTime>=12) {
                    Toast.makeText(getContext(),"시간을 오전으로만 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    str = "오전";
                    userDBManagerInstance.setNotiTime(pickerTime);
                    userDBManagerInstance.setNotiMinute(pickerMinute);
                    Toast.makeText(getContext(),""+str +" "+pickerTime+"시 "+pickerMinute+"분 으로 설정하였습니다.",Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            case R.id.dialogPickerX:
                dismiss();
                break;
        }
    }
}
