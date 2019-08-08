package com.example.timple.zdpigapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.BreedInfo;
import com.example.timple.zdpigapp.entity.FattenInfo;
import com.example.timple.zdpigapp.entity.PigAllInfoEntity;
import com.example.timple.zdpigapp.entity.PigBaseInfo;
import com.example.timple.zdpigapp.ui.adapter.PigBreedAdapter;
import com.example.timple.zdpigapp.ui.adapter.PigSingleInfoAdapter;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class EditActivity extends AppCompatActivity {

    @BindView(R.id.et_birth)
    EditText etBirth;
    @BindView(R.id.et_fl)
    EditText etFl;
    @BindView(R.id.et_sszc)
    EditText etSszc;
    @BindView(R.id.ll_jbxx)
    LinearLayout llJbxx;
    @BindView(R.id.tv_clrq)
    TextView etClrq;
    @BindView(R.id.et_clz)
    EditText etClz;
    @BindView(R.id.ll_yfxx)
    LinearLayout llYfxx;
    @BindView(R.id.rcv_pzxx)
    RecyclerView rcvPzxx;
    @BindView(R.id.rcv_ccxx)
    RecyclerView rcvCcxx;
    @BindView(R.id.rcv_yzxx)
    RecyclerView rcvYzxx;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    private String token;
    private String ear_tag;
    private String current_type;
    private int type;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        token = SPUtils.getInstance().getString("token");
        ear_tag = getIntent().getStringExtra("ear_tag");
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", -1);

        switch (type) {
            case 0://基本信息
                PigBaseInfo baseInfo = (PigBaseInfo) getIntent().getSerializableExtra("info");
                tvToolbar.setText("基本信息");
                current_type = "base_info";

                llJbxx.setVisibility(View.VISIBLE);
                etBirth.setText(baseInfo.getBirth());
                etFl.setText(baseInfo.getCategory());
                etSszc.setText(baseInfo.getOwner_hog_farm());

                break;
            case 1://育肥
                List<FattenInfo> fattenInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");
                tvToolbar.setText("育肥信息");
                current_type = "fatten_info";

                llYfxx.setVisibility(View.VISIBLE);
                etClrq.setText(fattenInfos.get(0).getResult());
                etClz.setText(fattenInfos.get(1).getResult());


                break;
            case 2://配种信息
                List<BreedInfo> yFInfos = (List<BreedInfo>) getIntent().getSerializableExtra("info");
                tvToolbar.setText("配种信息");
                current_type = "breed_info";

                rcvPzxx.setVisibility(View.VISIBLE);
                PigBreedAdapter adapter = new PigBreedAdapter(EditActivity.this, yFInfos);
                rcvPzxx.setLayoutManager(new LinearLayoutManager(EditActivity.this));
                rcvPzxx.setAdapter(adapter);


                break;
            case 3://产仔信息
                List<FattenInfo> cZInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");
                tvToolbar.setText("产仔信息");
                current_type = "litter_info";

                rcvCcxx.setVisibility(View.VISIBLE);
                PigSingleInfoAdapter litterAdapter = new PigSingleInfoAdapter(EditActivity.this, cZInfos);
                rcvCcxx.setLayoutManager(new LinearLayoutManager(EditActivity.this));
                rcvCcxx.setAdapter(litterAdapter);


                break;
            case 4://育种信息
                List<FattenInfo> yZInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");
                tvToolbar.setText("育种信息");
                current_type = "breeding_info";

                rcvYzxx.setVisibility(View.VISIBLE);
                PigSingleInfoAdapter breedingAdapter = new PigSingleInfoAdapter(EditActivity.this, yZInfos);
                rcvYzxx.setLayoutManager(new LinearLayoutManager(EditActivity.this));
                rcvYzxx.setAdapter(breedingAdapter);


                break;

        }


    }

    @OnClick({R.id.btn_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                List<Map<String,String>> list = new ArrayList<>();

                switch (type) {
                    case 0:
                        Map<String,String> map = new HashMap<>();
                        map.put("result",etBirth.getText().toString());
                        map.put("status","birth");
                        list.add(map);

                        Map<String,String> map1 = new HashMap<>();
                        map1.put("result",etFl.getText().toString());
                        map1.put("status","category");
                        list.add(map1);

                        Map<String,String> map2 = new HashMap<>();
                        map2.put("result",etSszc.getText().toString());
                        map2.put("status","owner_hog_farm");
                        list.add(map2);

                        break;
                    case 1:

                        Map<String,String> map3 = new HashMap<>();
                        map1.put("result",etClz.getText().toString());
                        map1.put("status","category");
                        list.add(map3);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        break;

                }

                updateSingleStatus(new Gson().toJson(list));
                break;
        }
    }

    /**
     * 更新个体状态信息
     */
    private void updateSingleStatus(String result) {
        RxHttp.postForm(Url.baseUrl + Url.PIG_FULLINFO)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .add("current_type", current_type)
                .add("content", result)
                .add("operator_time", System.currentTimeMillis())
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            if (!TextUtils.isEmpty(s.getData())) {

                            }
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }
}
