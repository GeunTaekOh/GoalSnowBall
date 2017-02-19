package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.Service.CurrentTimeService.gradeArray;

/**
 * Created by taek_aaa on 2017. 2. 18..
 */

public class GradeDialog extends Dialog {

    private Context c;
    UserDBManager userDBManager;
    TextView currentGradetv, nextGradetv;
    int result;

    public GradeDialog(Context context) {
        super(context);
        this.c = context;
        setContentView(R.layout.dialog_grade);
        userDBManager = new UserDBManager(getContext(), "userdb.db", null, 1);

        currentGradetv = (TextView)findViewById(R.id.prevGradeTv);
        nextGradetv = (TextView)findViewById(R.id.nextGradeTv);


        currentGradetv.setText(gradeArray[getCurrentGrad()]);
        nextGradetv.setText(gradeArray[getCurrentGrad()+1]);


    }


    public int getCurrentGrad(){
        String str;
        str = userDBManager.getGrade();
        Log.e("ttt",userDBManager.getGrade());
        if(str.equals("UnRank")){
            Log.e("ttt","여기들어오나?");
            result = 0;
        }else if(str.equals("D 등급")){
            Log.e("ttt","여기들어와야함");
            result = 1;
        }else if(str.equals("C 등급")){
            result = 2;
        }else if(str.equals("B 등급")){
            result = 3;
        }else if(str.equals("A 등급")){
            result = 4;
        }else if(str.equals("S 등급")){
            result = 5;
        }else if(str.equals("SS 등급")){
            result = 6;
        }else if(str.equals("SSS 등급")){
            result = 7;
        }else if(str.equals("Master")){
            result = 8;
        }

        Log.e("ttt",""+result);
        return result;
    }

}
