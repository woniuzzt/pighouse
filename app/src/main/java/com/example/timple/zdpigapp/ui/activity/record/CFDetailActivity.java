package com.example.timple.zdpigapp.ui.activity.record;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.DetailEntity;
import com.example.timple.zdpigapp.entity.PigEntity;
import com.example.timple.zdpigapp.entity.PigStatusEntity;
import com.example.timple.zdpigapp.entity.UpdatePigEntity;
import com.example.timple.zdpigapp.ui.activity.scan.ScanEarTagDetailActivity;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class CFDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.rl_msg)
    RelativeLayout rlMsg;
    @BindView(R.id.tv_fm)
    TextView tvFm;
    @BindView(R.id.et_czs)
    EditText etCzs;
    @BindView(R.id.et_sws)
    EditText etSws;
    @BindView(R.id.et_mny)
    EditText etMny;
    @BindView(R.id.et_cj)
    EditText etCj;
    @BindView(R.id.et_g)
    EditText etG;
    @BindView(R.id.et_wz)
    EditText etWz;
    @BindView(R.id.ll_fm)
    LinearLayout llFm;
    @BindView(R.id.tv_hl)
    TextView tvHl;
    @BindView(R.id.rg_hl)
    RadioGroup rgHl;
    @BindView(R.id.tv_dn)
    TextView tvDn;
    @BindView(R.id.et_hzs)
    EditText etHzs;
    @BindView(R.id.et_gzs)
    EditText etGzs;
    @BindView(R.id.et_dn_wz)
    EditText etDnWz;
    @BindView(R.id.ll_dn)
    LinearLayout llDn;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rb_jw)
    RadioButton rbJw;
    @BindView(R.id.rb_dy)
    RadioButton rbDy;
    @BindView(R.id.rb_bt)
    RadioButton rbBt;
    @BindView(R.id.rb_my)
    RadioButton rbMy;

    private PigStatusEntity pigStatusEntity;
    private String token;
    private String ear_tag;
    private String houseId;
    private PigEntity pigEntity;
    private List<UpdatePigEntity> updateList;
    private int status = 0;//分娩
    private String id;
    private String statusName;
    private int rb = -1;
    private boolean isCreate = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (pigEntity.getDetail() != null && pigEntity.getDetail().size() != 0) {
                    tvDn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tvHl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    List<DetailEntity> list = pigEntity.getDetail();
                    for (DetailEntity entity : list) {
                        switch (entity.getStatus()) {
                            case "childbirth":
                                isCreate = false;
                                initStatus();
                                status = 0;
                                List<UpdatePigEntity> list1 = new ArrayList<>();
                                list1 = new Gson().fromJson(entity.getResult(), new TypeToken<List<UpdatePigEntity>>() {
                                }.getType());
                                for (UpdatePigEntity entity1 : list1) {
                                    switch (entity1.getStatus()) {
                                        case "litter_size":  //产仔数
                                            etCzs.setText(entity1.getResult());
                                            break;
                                        case "death_number":   //死亡数
                                            etSws.setText(entity1.getResult());
                                            break;
                                        case "mummy": //木乃伊
                                            etMny.setText(entity1.getResult());
                                            break;
                                        case "disability": //残疾
                                            etCj.setText(entity1.getResult());
                                            break;
                                        case "boar": //公猪
                                            etG.setText(entity1.getResult());
                                            break;
                                        case "nest_weight": //窝重
                                            etWz.setText(entity1.getResult());
                                            break;
                                    }
                                }
                                break;
                            case "nursing": //护理
                                status = 1;
//                                List<UpdatePigEntity> list2 = new ArrayList<>();
//                                list2 = new Gson().fromJson(entity.getResult(), new TypeToken<List<UpdatePigEntity>>() {
//                                }.getType());
                                if (entity.getResult().equals("剪尾")) {
                                    rbJw.setChecked(true);
                                    rb = 0;
                                } else if (entity.getResult().equals("断牙")) {
                                    rbDy.setChecked(true);
                                    rb = 1;
                                } else if (entity.getResult().equals("补铁")) {
                                    rbBt.setChecked(true);
                                    rb = 2;
                                } else {
                                    rbMy.setChecked(true);
                                    rb = 3;
                                }
                                break;
                            case "weaning": //断奶
                                status = 2;
                                List<UpdatePigEntity> list3 = new ArrayList<>();
                                list3 = new Gson().fromJson(entity.getResult(), new TypeToken<List<UpdatePigEntity>>() {
                                }.getType());
                                for (UpdatePigEntity entity1 : list3) {
                                    switch (entity1.getStatus()) {
                                        case "alive_number":
                                            etHzs.setText(entity1.getResult());
                                            break;
                                        case "boar_number":
                                            etGzs.setText(entity1.getResult());
                                            break;
                                        case "nest_weight":
                                            etDnWz.setText(entity1.getResult());
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            if (msg.what == 1) {
                tvId.setText("当前ID:" + id);
                tvStatus.setText("状态:" + statusName);
            }
            if (msg.what == 2) {
                showDialog1();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cf_detail);
        ButterKnife.bind(this);

        tvToolbar.setText("产房");
        tvStatus.setText("状态:");

        token = SPUtils.getInstance().getString("token");
        ear_tag = getIntent().getExtras().getString("ear_tag");
//        ear_tag = "s" + ear_tag;
        tvId.setText("当前ID:" + ear_tag);
        houseId = SPUtils.getInstance().getString("houseId");
        updateList = new ArrayList<>();
        closeAll();
        initData();
    }

    private void initData() {
        RxHttp.postForm(Url.baseUrl + Url.PIG_FULLINFO)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s.getCode() == 10000) {
                        if (!TextUtils.isEmpty(s.getData())) {
                            pigEntity = new Gson().fromJson(s.getData(), PigEntity.class);
                            handler.sendEmptyMessage(0);
                        }
                    }
                    if (s.getCode() == 10001) {
                        isCreate = true;
                        status = 0;
                    }
                }, throwable -> {
                    int i = 0;
                });
    }


    @OnClick({R.id.rb_jw, R.id.rb_dy, R.id.rb_bt, R.id.rb_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_jw:
                rb = 0;
                break;
            case R.id.rb_dy:
                rb = 1;
                break;
            case R.id.rb_bt:
                rb = 2;
                break;
            case R.id.rb_my:
                rb = 3;
                break;
        }
    }

    private void closeAll() {
        llDn.setVisibility(View.GONE);
        rgHl.setVisibility(View.GONE);
        llFm.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_fm, R.id.tv_hl, R.id.tv_dn})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.tv_fm:
                if (llFm.getVisibility() == View.VISIBLE) {
                    llFm.setVisibility(View.GONE);
                } else {
                    closeAll();
                    llFm.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_hl:
//                if (status == 0) return;
                if (isCreate){
                    return;
                }
                if (rgHl.getVisibility() == View.VISIBLE) {
                    rgHl.setVisibility(View.GONE);
                } else {
                    closeAll();
                    rgHl.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_dn:
                if (isCreate){
                    return;
                }
//                if (status == 0 || status == 1) return;
                if (llDn.getVisibility() == View.VISIBLE) {
                    llDn.setVisibility(View.GONE);
                } else {
                    closeAll();
                    llDn.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private boolean isFinish = false;

    @OnClick({R.id.btn_sure, R.id.btn_next})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                isFinish = false;
//                if (status == 0) {
//                status0();
//                }
//                if (status == 1) {
//                status1();
//                }
//                if (status == 2) {
                status2();
//                }
                break;
            case R.id.btn_next:
                showDialog();
                break;
        }
    }

    private void showDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CFDetailActivity.this)
                .setTitle("数据上传成功")
                .setPositiveButton("下一个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CFDetailActivity.this, ScanEarTagDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("上一页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CFDetailActivity.this)
                .setTitle("提示")
                .setMessage("是否上传数据?")
                .setPositiveButton("上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isFinish = true;
                        if (status == 0) {
                            status0();
                        }
                        if (status == 1) {
                            status1();
                        }
                        if (status == 2) {
                            status2();
                        }
                    }
                })
                .setNegativeButton("下一个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CFDetailActivity.this, ScanEarTagDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }


    private void toast(String str) {
        Toast.makeText(CFDetailActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private void status0() {
        if (TextUtils.isEmpty(etCzs.getText().toString())) {
            toast("产仔数不能为空");
            return;
        }
        if (TextUtils.isEmpty(etSws.getText().toString())) {
            toast("死亡数不能为空");
            return;
        }
        if (TextUtils.isEmpty(etMny.getText().toString())) {
            toast("木乃伊不能为空");
            return;
        }
        if (TextUtils.isEmpty(etCj.getText().toString())) {
            toast("残疾不能为空");
            return;
        }
        if (TextUtils.isEmpty(etG.getText().toString())) {
            toast("公猪不能为空");
            return;
        }
        if (TextUtils.isEmpty(etWz.getText().toString())) {
            toast("窝重不能为空");
            return;
        }
        List<UpdatePigEntity> list = collectO();
        String result = new Gson().toJson(list);
        if (isCreate) {
            RxHttp.postForm(Url.baseUrl + Url.PIG_CREATE)
                    .addHeader("x-access-token", token)
                    .add("ear_tag", ear_tag)
                    .add("hog_house_id", houseId)
                    .add("status", "childbirth")
                    .add("result", result)
                    .add("operator_time", System.currentTimeMillis())
                    .asObject(BaseData.class)
                    .subscribe(s -> {
                        if (s != null) {
                            if (s.getCode() == 10000) {
                                initStatus();
                                isCreate = false;
                                Looper.prepare();
                                showDialog1();
//                                Toast.makeText(CFDetailActivity.this, "数据提交成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }, throwable -> {
                        Log.e("123", "onInventoryTagEnd: ");
                    });
        } else {
            List<UpdatePigEntity> list1 = new ArrayList<>();
            list1.add(new UpdatePigEntity("childbirth", result));
            update(new Gson().toJson(list1));
        }

    }

    private List<UpdatePigEntity> collectO() {
        List<UpdatePigEntity> list = new ArrayList<>();
        list.add(new UpdatePigEntity("litter_size", etCzs.getText().toString()));
        list.add(new UpdatePigEntity("death_number", etSws.getText().toString()));
        list.add(new UpdatePigEntity("mummy", etMny.getText().toString()));
        list.add(new UpdatePigEntity("disability", etCj.getText().toString()));
        list.add(new UpdatePigEntity("boar", etG.getText().toString()));
        list.add(new UpdatePigEntity("nest_weight", etWz.getText().toString()));
        return list;
    }

    private List<UpdatePigEntity> collect1() {
        List<UpdatePigEntity> list = new ArrayList<>();
        String result = "";
        if (rb == 0) {
            result = "剪尾";
        } else if (rb == 1) {
            result = "断牙";
        } else if (rb == 2) {
            result = "补铁";
        } else if (rb == 3) {
            result = "免疫";
        }
        list.add(new UpdatePigEntity("nursing", result));
        return list;
    }

    private void status1() {
        if (rb == -1) {
            toast("护理必须选择");
            return;
        }
        List<UpdatePigEntity> list = new ArrayList<>();
        list.add(new UpdatePigEntity("childbirth", new Gson().toJson(collectO())));
        list.addAll(collect1());
        update(new Gson().toJson(list));
    }

    private void status2() {
//        if (TextUtils.isEmpty(etHzs.getText().toString())) {
//            toast("活仔数不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(etGzs.getText().toString())) {
//            toast("公猪数不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(etDnWz.getText().toString())) {
//            toast("窝重不能为空");
//            return;
//        }
        if (isCreate){
            status0();
            return;
        }
        List<UpdatePigEntity> list = new ArrayList<>();
        list.add(new UpdatePigEntity("childbirth", new Gson().toJson(collectO())));
        list.addAll(collect1());

        List<UpdatePigEntity> list1 = new ArrayList<>();
        list1.add(new UpdatePigEntity("alive_number", etHzs.getText().toString()));
        list1.add(new UpdatePigEntity("boar_number", etGzs.getText().toString()));
        list1.add(new UpdatePigEntity("nest_weight", etDnWz.getText().toString()));
        list.add(new UpdatePigEntity("weaning", new Gson().toJson(list1)));
        update(new Gson().toJson(list));
    }

    private void update(String result) {
        RxHttp.postForm(Url.baseUrl + Url.PIG_CHANGE_STATUS)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .add("hog_house_id", houseId)
                .add("content", result)
                .add("operator_time", System.currentTimeMillis())
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            handler.sendEmptyMessage(2);
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }

    private void initStatus() {
        RxHttp.postForm(Url.baseUrl + Url.PIG_STATUS)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            PigStatusEntity entity = new Gson().fromJson(s.getData(), PigStatusEntity.class);
                            id = ear_tag;
                            statusName = entity.getStatus_desc();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }
}
