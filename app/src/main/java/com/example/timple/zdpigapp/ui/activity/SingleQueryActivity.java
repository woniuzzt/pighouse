package com.example.timple.zdpigapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.BreedInfo;
import com.example.timple.zdpigapp.entity.FattenInfo;
import com.example.timple.zdpigapp.entity.PigAllInfoEntity;
import com.example.timple.zdpigapp.entity.PigBaseInfo;
import com.example.timple.zdpigapp.entity.PigStatusEntity;
import com.example.timple.zdpigapp.ui.activity.turn.TurnGroupActivity;
import com.example.timple.zdpigapp.ui.adapter.PigBreedAdapter;
import com.example.timple.zdpigapp.ui.adapter.PigHouseAdapter;
import com.example.timple.zdpigapp.ui.adapter.PigSingleInfoAdapter;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class SingleQueryActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_qs)
    TextView tvQs;
    @BindView(R.id.tv_lw)
    TextView tvLw;

    @BindView(R.id.tv_jbxx)
    TextView tvJbxx;
    @BindView(R.id.tv_yfxx)
    TextView tvYfxx;
    @BindView(R.id.tv_clrq)
    TextView tvClrq;
    @BindView(R.id.tv_clz)
    TextView tvClz;

    @BindView(R.id.ll_jbxx)
    LinearLayout llJbxx;
    @BindView(R.id.ll_yfxx)
    LinearLayout llYfxx;
    @BindView(R.id.rcv_ccxx)
    RecyclerView rcvCcxx;

    @BindView(R.id.rcv_yzxx)
    RecyclerView rcvYzxx;
    @BindView(R.id.tv_pzxx)
    TextView tvPzxx;
    @BindView(R.id.rcv_pzxx)
    RecyclerView rcvPzxx;
    @BindView(R.id.tv_ccxx)
    TextView tvCcxx;
    @BindView(R.id.tv_yzxx)
    TextView tvYzxx;
    @BindView(R.id.btn_return)
    Button btnReturn;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.tv_birth)
    TextView tvBirth;
    @BindView(R.id.tv_fl)
    TextView tvFl;
    @BindView(R.id.tv_sszc)
    TextView tvSszc;

    private String token;
    private String ear_tag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://获取状态更新UI
                    PigStatusEntity current_info = pigEntity.getCurrent_info();
                    base_info = pigEntity.getBase_info();
                    fatten_info = pigEntity.getFatten_info();
                    breed_info = pigEntity.getBreed_info();
                    breeding_info = pigEntity.getBreeding_info();
                    litter_info = pigEntity.getLitter_info();

                    tvId.setText("当前ID:" + current_info.getHog_id());
                    tvStatus.setText("状态:" + current_info.getStatus_desc());
                    tvQs.setText("圈舍:" + current_info.getHog_house_name());
                    tvLw.setText("栏位:" + current_info.getFiled());


                    PigBreedAdapter adapter = new PigBreedAdapter(SingleQueryActivity.this, breed_info);
                    rcvPzxx.setLayoutManager(new LinearLayoutManager(SingleQueryActivity.this));
                    rcvPzxx.setAdapter(adapter);


                    PigSingleInfoAdapter breedingAdapter = new PigSingleInfoAdapter(SingleQueryActivity.this, breeding_info);
                    rcvYzxx.setLayoutManager(new LinearLayoutManager(SingleQueryActivity.this));
                    rcvYzxx.setAdapter(breedingAdapter);

                    PigSingleInfoAdapter litterAdapter = new PigSingleInfoAdapter(SingleQueryActivity.this, litter_info);
                    rcvCcxx.setLayoutManager(new LinearLayoutManager(SingleQueryActivity.this));
                    rcvCcxx.setAdapter(litterAdapter);

                    break;

            }
        }
    };
    private PigAllInfoEntity pigEntity;
    private PigBaseInfo base_info;
    private List<FattenInfo> fatten_info;//育肥
    private List<BreedInfo> breed_info;//配种
    private List<FattenInfo> breeding_info;//育种
    private List<FattenInfo> litter_info;//产仔

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_query);
        ButterKnife.bind(this);

        tvToolbar.setText("个体查询");
        token = SPUtils.getInstance().getString("token");
        ear_tag = getIntent().getExtras().getString("ear_tag");


        querySingleStatus();
    }

    /**
     * 查询个体所有信息
     */
    private void querySingleStatus() {
        RxHttp.postForm(Url.baseUrl + Url.PIG_FULLINFO)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            if (!TextUtils.isEmpty(s.getData())) {
                                pigEntity = new Gson().fromJson(s.getData(), PigAllInfoEntity.class);
                                handler.sendEmptyMessage(0);

                            }
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }

    private int type = -1;

    @OnClick({R.id.tv_pzxx, R.id.tv_jbxx, R.id.tv_ccxx, R.id.tv_yzxx, R.id.tv_yfxx, R.id.btn_return, R.id.btn_edit})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_jbxx://基本信息
                type = 0;
                if (!TextUtils.isEmpty(base_info.getBirth()))
                    tvBirth.setText(base_info.getBirth());
                if (!TextUtils.isEmpty(base_info.getCategory()))
                    tvFl.setText(base_info.getCategory());
                if (!TextUtils.isEmpty(base_info.getOwner_hog_farm()))
                    tvSszc.setText(base_info.getOwner_hog_farm());
                llJbxx.setVisibility(View.VISIBLE);
                btnEdit.setEnabled(true);
                break;
            case R.id.tv_yfxx://育肥
                type = 1;
                tvClrq.setText(fatten_info.get(0).getResult());
                tvClz.setText(fatten_info.get(1).getResult());
                llYfxx.setVisibility(View.VISIBLE);
                btnEdit.setEnabled(true);
                break;
            case R.id.tv_pzxx://配种信息
                type = 2;
                rcvPzxx.setVisibility(View.VISIBLE);
                btnEdit.setEnabled(true);
                break;

            case R.id.tv_ccxx://产仔信息
                type = 3;
                rcvCcxx.setVisibility(View.VISIBLE);
                btnEdit.setEnabled(true);
                break;

            case R.id.tv_yzxx://育种信息
                type = 4;
                rcvYzxx.setVisibility(View.VISIBLE);
                btnEdit.setEnabled(true);
                break;
            case R.id.btn_return:
                finish();
                break;
            case R.id.btn_edit:
                if (btnEdit.isEnabled()) {
                    Intent intent = new Intent(SingleQueryActivity.this, EditActivity.class);
                    intent.putExtra("type", type);
                    switch (type) {
                        case 0:
                            intent.putExtra("info", base_info);
                            break;
                        case 1:
                            intent.putExtra("info", (Serializable) fatten_info);
                            break;
                        case 2:
                            intent.putExtra("info", (Serializable) breed_info);
                            break;
                        case 3:
                            intent.putExtra("info", (Serializable) litter_info);

                            break;
                        case 4:
                            intent.putExtra("info", (Serializable) breeding_info);
                            break;

                    }

                    intent.putExtra("ear_tag", ear_tag);
                    intent.putExtra("id", base_info.getId());
                    startActivity(intent);
                }

                break;
        }
    }
}
