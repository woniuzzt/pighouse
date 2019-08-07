package com.example.timple.zdpigapp.ui.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanEarTagActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.btn_scan_ear_tag)
    Button btnScanEarTag;
    @BindView(R.id.btn_return)
    Button btnReturn;

    int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ear_tag);
        ButterKnife.bind(this);
        String toolbarText = getIntent().getStringExtra("name");
        type = Integer.valueOf(getIntent().getStringExtra("type"));
        tvToolbar.setText(toolbarText);
    }

    @OnClick({R.id.btn_scan_ear_tag, R.id.btn_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan_ear_tag:
                int houseId = Integer.valueOf(SPUtils.getInstance().getString("houseId"));
                if (type == 0) {   //录入
                    Intent intent = new Intent(ScanEarTagActivity.this, ScanEarTagDetailActivity.class);
                    intent.putExtra("type", type+"");
                    startActivity(intent);
                } else if (type == 1) { //转群
                    Intent intent = new Intent(ScanEarTagActivity.this, ScanEarTagDetailActivity.class);
                    intent.putExtra("type", type+"");
                    startActivity(intent);
                }else if (type == 2){//扫描
                    Intent intent = new Intent(ScanEarTagActivity.this, ScanEarTagDetailActivity.class);
                    intent.putExtra("type", type+"");
                    startActivity(intent);
                }

                break;
            case R.id.btn_return:
                finish();
                break;
        }
    }
}
