package com.nwuking.ytalk.activities;


import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nwuking.ytalk.MsgType;
import com.nwuking.ytalk.NetUtils;
import com.nwuking.ytalk.R;
import com.nwuking.ytalk.net.NetWorker;

public class RegisterActivity extends BaseActivity {
    private EditText et_name, et_nickname, et_password, et_surepwd;
    private Button makesure_register;
    private String mobile, username, password;
    private String u_name, u_nickname, u_password, surePasswd;

    @Override
    protected int getContentView() {
        //return 0;
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setData() {
        makesure_register = (Button)findViewById(R.id.makesure_register);
        makesure_register.setOnClickListener(this);
        et_name = (EditText)findViewById(R.id.et_name);
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        et_password = (EditText)findViewById(R.id.et_password);
        et_surepwd = (EditText)findViewById(R.id.et_surepwd);
    }

    @Override
    public void onClick(View v) {
        u_name = et_name.getText().toString().trim();
        u_nickname = et_nickname.getText().toString().trim();
        u_password = et_password.getText().toString().trim();
        surePasswd = et_surepwd.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_back:
                setResult(BaseActivity.REGISTER_RESULT_CANCEL);
                finish();
                break;

            case R.id.makesure_register:
                if (!NetUtils.isConnected(this))
                {
                    Toast.makeText(this, R.string.net_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                // 注册
                if (u_name.trim().length() != 11) {
                    Toast.makeText(this, "请输入11位的账户名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (u_nickname.trim().length() < 0) {
                    Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (u_password.length() < 3 || u_password.length() > 20) {
                    Toast.makeText(this, "密码请输入6-16位字符！！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!surePasswd.equals(u_password)) {
                    Toast.makeText(this, "两次密码输入不一致！！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Button btnRegister = (Button)findViewById(R.id.makesure_register);
                //btnRegister.setEnabled(false);
                //btnRegister.setVisibility();
                NetWorker.registerUser(u_name, u_password, u_nickname);
                //String opStr = ""+op;

                    //Toast.makeText(this, "连接不到服务器！！"+opStr, Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
    }

    @Override
    public void processMessage(Message msg) {
        super.processMessage(msg);
        if (msg.what == MsgType.msg_type_register) {
            if (msg.arg1 == MsgType.ERROR_CODE_SUCCESS) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                //将用户名和密码传给登录界面
                Intent intent = new Intent();
                intent.putExtra("register_username", u_name);
                intent.putExtra("register_password", u_password);
                setResult(BaseActivity.REGISTER_RESULT_OK, intent);
                //startActivity(intent);
                finish();
            } else if (msg.arg1 == MsgType.ERROR_CODE_REGISTERED) {
                Toast.makeText(this, "该用户已被注册", Toast.LENGTH_SHORT).show();

            } else  {
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
            }

            Button btnRegister = (Button)findViewById(R.id.makesure_register);
            btnRegister.setEnabled(true);


        }

    } //end processMessage
}// end class
