package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;

import com.taek_aaa.goalsnowball.R;

/**
 * Created by taek_aaa on 2017. 1. 29..
 */

public class FailDialog extends Dialog {
    public FailDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_fail);
    }
}
