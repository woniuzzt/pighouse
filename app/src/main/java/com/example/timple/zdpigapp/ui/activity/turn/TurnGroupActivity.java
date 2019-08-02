package com.example.timple.zdpigapp.ui.activity.turn;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.PigHouseEntity;
import com.example.timple.zdpigapp.ui.adapter.PigHouseAdapter;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rxhttp.wrapper.param.RxHttp;

public class TurnGroupActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.rcv_list)
    RecyclerView rcvList;

    public static final int TYPE_TURN_GROUP = 1;

    private String data;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                hideDialog();
                List<PigHouseEntity> list = new Gson().fromJson(data, new TypeToken<List<PigHouseEntity>>() {
                }.getType());
                PigHouseAdapter adapter = new PigHouseAdapter(TurnGroupActivity.this, list, TYPE_TURN_GROUP);
                rcvList.setLayoutManager(new LinearLayoutManager(TurnGroupActivity.this));
                rcvList.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        tvToolbar.setText("转群");
        showDialog();
        RxHttp.postForm(Url.baseUrl + Url.PIG_HOUSE_LIST)
                .addHeader("x-access-token", SPUtils.getInstance().getString("token"))
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            if (!TextUtils.isEmpty(s.getData())) {
                                data = s.getData();
                                SPUtils.getInstance().put("recordList", data);
                                handler.sendEmptyMessage(0);
                            } else {
                                //网络环境不好，使用上次缓存的list
                                data = SPUtils.getInstance().getString("recordList");
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }
                }, throwable -> {
                    //网络环境不好，使用上次缓存的list
                    data = SPUtils.getInstance().getString("recordList");
                    handler.sendEmptyMessage(0);
                });
    }

    private ProgressDialog dialog;

    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(TurnGroupActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("请稍后...");
        }
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
