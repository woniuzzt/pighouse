package com.example.timple.zdpigapp.ui.activity.main;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_valid)
    EditText etValid;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_cancle)
    Button btnCancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tvToolbar.setText("注册");
    }

    @OnClick({R.id.btn_register, R.id.btn_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etValid.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "请输入邀请码", Toast.LENGTH_SHORT).show();
                    return;
                }
                RxHttp.postForm(Url.baseUrl + Url.REGISTER)
                        .add("invite_code", etValid.getText().toString())
                        .add("username", etName.getText().toString())
                        .add("password", etPassword.getText().toString())
                        .asObject(BaseData.class)
                        .subscribe(s -> {
                            Looper.prepare();
                            if (s != null) {
                                Toast.makeText(RegisterActivity.this, s.getMsg(), Toast.LENGTH_SHORT).show();
                                if (s.getCode() == 10000) {
                                    finish();
                                }
                            }
                            Looper.loop();
                        }, throwable -> {
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        });

                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }
}
