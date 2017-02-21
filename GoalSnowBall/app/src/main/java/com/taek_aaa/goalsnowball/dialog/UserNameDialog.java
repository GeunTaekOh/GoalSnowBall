package com.taek_aaa.goalsnowball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.UserDBManager;

import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

/**
 * Created by taek_aaa on 2017. 1. 21..
 */

public class UserNameDialog extends Dialog implements View.OnClickListener {
    EditText usernameTv;

    public UserNameDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_username);

        usernameTv = (EditText) findViewById(R.id.usernametv);
        userDBManagerInstance =UserDBManager.getInstance(getContext());
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
                    userDBManagerInstance.setName(usernameTv.getText().toString());
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
