package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.UserDBManager;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class UserNameDialog extends Dialog implements View.OnClickListener {
    EditText usernameTv;
    UserDBManager userDBManager;

    public UserNameDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_username);

        usernameTv = (EditText) findViewById(R.id.usernametv);
        userDBManager = new UserDBManager(getContext(), "user.db", null, 1);
        findViewById(R.id.confirm_username_btn).setOnClickListener(this);
        findViewById(R.id.exit_username_btn).setOnClickListener(this);
        findViewById(R.id.usernameExitbtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_username_btn:
                if(usernameTv.getText().toString().equals("")){
                    Toast.makeText(getContext(), "이름을 입력하세요. ", Toast.LENGTH_SHORT).show();
                }else {
                    userDBManager.setName(usernameTv.getText().toString());
                    Toast.makeText(getContext(), "이름을 저장하였습니다. ", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            case R.id.exit_username_btn:
                dismiss();
                break;
            case R.id.usernameExitbtn:
                dismiss();
                break;
        }
    }
}
