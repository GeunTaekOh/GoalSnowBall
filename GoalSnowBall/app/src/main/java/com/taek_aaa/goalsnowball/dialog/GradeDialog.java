package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.R.id.gradeExitbtn;
import static com.taek_aaa.goalsnowball.Service.CurrentTimeService.gradeArray;
import static com.taek_aaa.goalsnowball.Service.CurrentTimeService.needsAmount;
import static com.taek_aaa.goalsnowball.Service.CurrentTimeService.needsGold;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

/**
 * Created by taek_aaa on 2017. 2. 18..
 */

public class GradeDialog extends Dialog implements View.OnClickListener {

    private Context c;
    TextView currentGradetv, nextGradetv, needsAmountLeft, needsAmountRight, needsGoldLeft, needsGoldRight;
    int result;
    int currentIndex;
    ImageButton exit;
    ProgressBar progressBarGold, progressBarAmount;
    DataController dataController;

    public GradeDialog(Context context) {
        super(context);
        this.c = context;
        setContentView(R.layout.dialog_grade);
        init();
        dataController = new DataController();

        draw();

    }

    private void draw(){
        progressBarGold.setMax(needsGold[currentIndex] * 100);
        progressBarGold.setProgress(userDBManagerInstance.getGold() * 100);
        progressBarGold.setVisibility(ProgressBar.VISIBLE);

        progressBarAmount.setMax(needsAmount[currentIndex] * 100);
        progressBarAmount.setProgress(dbManagerInstance.getLastPosition() * 100);
        progressBarAmount.setVisibility(ProgressBar.VISIBLE);

        dataController.setProgressAnimate(progressBarGold);
        dataController.setProgressAnimate(progressBarAmount);
    }


    private int getCurrentGrade() {
        String str;
        str = userDBManagerInstance.getGrade();
        if (str.equals("UnRank")) {
            result = 0;
        } else if (str.equals("D 등급")) {
            result = 1;
        } else if (str.equals("C 등급")) {
            result = 2;
        } else if (str.equals("B 등급")) {
            result = 3;
        } else if (str.equals("A 등급")) {
            result = 4;
        } else if (str.equals("S 등급")) {
            result = 5;
        } else if (str.equals("SS 등급")) {
            result = 6;
        } else if (str.equals("SSS 등급")) {
            result = 7;
        } else if (str.equals("Master")) {
            result = 8;
        }

        return result;
    }

    private void init() {
        userDBManagerInstance = UserDBManager.getInstance(getContext());
        dbManagerInstance = DBManager.getInstance(getContext());
        needsGoldLeft = (TextView) findViewById(R.id.needsGoldLeft);
        needsGoldRight = (TextView) findViewById(R.id.needsGoldRight);
        needsAmountLeft = (TextView) findViewById(R.id.needsAmountLeft);
        needsAmountRight = (TextView) findViewById(R.id.needsAmountRight);

        progressBarGold = (ProgressBar) findViewById(R.id.gradeDialogGoldNeeds);
        progressBarAmount = (ProgressBar) findViewById(R.id.gradeDialogAmountNeeds);

        exit = (ImageButton) findViewById(gradeExitbtn);
        exit.setOnClickListener(this);

        currentGradetv = (TextView) findViewById(R.id.prevGradeTv);
        nextGradetv = (TextView) findViewById(R.id.nextGradeTv);

        currentIndex = getCurrentGrade();

        currentGradetv.setText(gradeArray[currentIndex]);
        nextGradetv.setText(gradeArray[currentIndex + 1]);


        needsAmountRight.setText("" + needsAmount[currentIndex]);
        needsGoldRight.setText("" + needsGold[currentIndex]);


        needsGoldLeft.setText("" + userDBManagerInstance.getGold());
        needsAmountLeft.setText("" + dbManagerInstance.getLastPosition());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gradeExitbtn:
                dismiss();
                break;
        }
    }

}
