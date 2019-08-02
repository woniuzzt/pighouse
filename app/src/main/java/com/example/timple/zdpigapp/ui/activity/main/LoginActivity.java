package com.example.timple.zdpigapp.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.LoginEntity;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvToolbar.setText("登录");
        try {
            String token = SPUtils.getInstance().getString("token");
            if (!TextUtils.isEmpty(token)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                RxHttp.postForm(Url.baseUrl + Url.LOGIN)
                        .add("username", etName.getText().toString())
                        .add("password", etPassword.getText().toString())
                        .asObject(BaseData.class)
                        .subscribe(s -> {
                            Looper.prepare();
                            if (s != null) {
                                Toast.makeText(LoginActivity.this, s.getMsg(), Toast.LENGTH_SHORT).show();
                                if (s.getCode() == 10000) {
                                    Gson gson = new Gson();
                                    LoginEntity loginEntity = gson.fromJson(s.getData(), LoginEntity.class);
                                    SPUtils.getInstance().put("token", loginEntity.getUserToken());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            Looper.loop();
                        }, throwable -> {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        });
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
