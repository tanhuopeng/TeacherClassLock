package com.jci.teacherclasslock.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.utils.ToastUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
        initEvent();
    }

    private void initEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtils.showShort("账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtils.showShort("密码不能为空！");
            return;
        }
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    jump();
                }
                else {
                    ToastUtils.showShort("登录失败，请检查网络");
                }
            }
        });
    }

    private void jump() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initWidget() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
    }
}
