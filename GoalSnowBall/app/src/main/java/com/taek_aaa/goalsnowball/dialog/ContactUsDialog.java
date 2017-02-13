package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.taek_aaa.goalsnowball.R;

/**
 * Created by taek_aaa on 2017. 2. 9..
 */

public class ContactUsDialog extends Dialog implements View.OnClickListener{
    Context globalContext;

    public ContactUsDialog(Context context) {
        super(context);
        this.globalContext = context;
        setContentView(R.layout.dialog_contactus);

        findViewById(R.id.contactExitbtn).setOnClickListener(this);
        findViewById(R.id.mailBtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mailBtn:
                globalContext.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:rmsxor94@naver.com")));
                dismiss();
                break;
            case R.id.contactExitbtn:
                dismiss();
                break;
        }
    }
}
