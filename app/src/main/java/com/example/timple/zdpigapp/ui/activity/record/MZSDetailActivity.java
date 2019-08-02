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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

public class MZSDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_cq)
    TextView tvCq;
    @BindView(R.id.tv_pz)
    TextView tvPz;
    @BindView(R.id.tv_rj)
    TextView tvRj;
    @BindView(R.id.tv_jk)
    TextView tvJk;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.rl_msg)
    RelativeLayout rlMsg;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_cq)
    RadioGroup rgCq;
    @BindView(R.id.ll_pz)
    LinearLayout llPz;
    @BindView(R.id.rb_zc)
    RadioButton rbZc;
    @BindView(R.id.rb_fq)
    RadioButton rbFq;
    @BindView(R.id.rb_lc)
    RadioButton rbLc;
    @BindView(R.id.rg_rj)
    RadioGroup rgRj;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.et_jybh)
    EditText etJybh;

    private PigStatusEntity pigStatusEntity;
    private String token;
    private String ear_tag;
    private String houseId;
    private PigEntity pigEntity;
    private List<UpdatePigEntity> updateList;

    private int chaQing = -1;
    private String peiZhong = "";
    private int renJian = -1;

    private int status = 0;
    private String id;
    private String statusName;
    private int jk = -1;

    private boolean isCreate = false;   //是否创建猪

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                tvId.setText("当前ID:" + id);
                tvStatus.setText("状态:" + statusName);
                if (pigEntity.getDetail() != null && pigEntity.getDetail().size() != 0) {
                    tvJk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tvPz.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tvRj.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    List<DetailEntity> list = pigEntity.getDetail();
                    Log.e("123", "handleMessage: " + new Gson().toJson(list));
                    for (DetailEntity entity : list) {
                        switch (entity.getStatus()) {
                            case "estrus":  //查情
//                                initStatus();
                                status = 0;
                                if (entity.getResult().equals("是")) {
                                    rbYes.setChecked(true);
                                    chaQing = 0;
                                } else {
                                    rbNo.setChecked(false);
                                    chaQing = 1;
                                }
                                break;
                            case "pregnancy_detection": //妊检
                                status = 2;
                                if (entity.getResult().equals("正常")) {
                                    rbZc.setChecked(true);
                                    renJian = 0;
                                } else if (entity.getResult().equals("返情")) {
                                    rbFq.setChecked(true);
                                    renJian = 1;
                                } else {
                                    rbLc.setChecked(true);
                                    renJian = 2;
                                }
                                break;
                            case "breed": //配种
                                status = 1;
                                etJybh.setText(entity.getResult());
                                break;
                            case "healthy": //健康
                                status = 3;
                                if (entity.getResult().equals("健康")) {
                                    tvJk.setBackgroundColor(getResources().getColor(R.color.black));
                                    jk = 1;
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

    private void toast(String str) {
        Toast.makeText(MZSDetailActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mzs_detail);
        ButterKnife.bind(this);


        tvToolbar.setText("母猪舍");
        tvStatus.setText("状态:");

        token = SPUtils.getInstance().getString("token");
        ear_tag = getIntent().getExtras().getString("ear_tag");
//        ear_tag = "s" + ear_tag;
        tvId.setText("当前ID:" + ear_tag);
        houseId = SPUtils.getInstance().getString("houseId");
        updateList = new ArrayList<>();

        closeAll();
        initClick();
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
                            isCreate = false;
                            pigEntity = new Gson().fromJson(s.getData(), PigEntity.class);
                            initStatus();

                        }
                    }
                    if (s.getCode() == 10001) {
                        status = 0;
                        isCreate = true;
                    }
                }, throwable -> {
                    int i = 0;
                });
    }

    /**
     * 获取猪的信息
     */
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
//                            handler.sendEmptyMessage(1);
                            handler.sendEmptyMessage(0);
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }

    private void initClick() {
        rgCq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_yes:
                        chaQing = 0;
                        break;
                    case R.id.rb_no:
                        chaQing = 1;
                        break;
                }
            }
        });

        rgRj.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_zc:
                        renJian = 0;
                        for (int i = 0; i < updateList.size(); i++) {
                            if (updateList.get(i).getStatus().equals("pregnancy_detection")) {
                                updateList.get(i).setResult("正常");
                                return;
                            }
                            if (i == updateList.size() - 1) {
                                updateList.add(new UpdatePigEntity("pregnancy_detection", "正常"));
                            }
                        }
                        break;
                    case R.id.rb_fq:
                        renJian = 1;
                        for (int i = 0; i < updateList.size(); i++) {
                            if (updateList.get(i).getStatus().equals("pregnancy_detection")) {
                                updateList.get(i).setResult("返情");
                                return;
                            }
                            if (i == updateList.size() - 1) {
                                updateList.add(new UpdatePigEntity("pregnancy_detection", "返情"));
                            }
                        }
                        break;
                    case R.id.rb_lc:
                        renJian = 2;
                        for (int i = 0; i < updateList.size(); i++) {
                            if (updateList.get(i).getStatus().equals("pregnancy_detection")) {
                                updateList.get(i).setResult("流产");
                                return;
                            }
                            if (i == updateList.size() - 1) {
                                updateList.add(new UpdatePigEntity("pregnancy_detection", "流产"));
                            }
                        }
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tv_cq, R.id.tv_pz, R.id.tv_rj, R.id.tv_jk, R.id.btn_sure, R.id.btn_next})
    public void onViewClicked(View view) {
        closeAll();
        switch (view.getId()) {
            case R.id.tv_cq:
                rgCq.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_pz:
                if (isCreate) return;
                llPz.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_rj:
                if (isCreate) return;
                rgRj.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_jk:
                if (isCreate) return;
                if (jk == -1) {
                    jk = 1;
                    tvJk.setBackgroundColor(getResources().getColor(R.color.black));
                } else {
                    jk = -1;
                    tvJk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                break;
            case R.id.btn_sure:
                update(false);
                break;
            case R.id.btn_next:
                showDialog();
                break;
        }
    }

    private void closeAll() {
        rgCq.setVisibility(View.GONE);
        rgRj.setVisibility(View.GONE);
        llPz.setVisibility(View.GONE);
    }

    private void showDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MZSDetailActivity.this)
                .setTitle("数据上传成功")
                .setPositiveButton("下一个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MZSDetailActivity.this, ScanEarTagDetailActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MZSDetailActivity.this)
                .setTitle("提示")
                .setMessage("是否上传数据?")
                .setPositiveButton("上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(true);
                    }
                })
                .setNegativeButton("下一个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MZSDetailActivity.this, ScanEarTagDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }

    private void update(boolean isFinish) {
        if (updateList != null) updateList.clear();
//        switch (status) {
//            case 0:
//                if (chaQing == -1) {
//                    Toast.makeText(MZSDetailActivity.this, "查情必须选择", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (isCreate) {
//                    pigCreate();
//                    return;
//                } else {
//                    updateList.add(new UpdatePigEntity("estrus", chaQing == 0 ? "是" : "否"));
//                }
//                break;
//            case 1:
//                if (TextUtils.isEmpty(etJybh.getText().toString())) {
//                    toast("精液编号必须填写");
//                    return;
//                }
//                updateList.add(new UpdatePigEntity("estrus", chaQing == 0 ? "是" : "否"));
//                updateList.add(new UpdatePigEntity("breed", etJybh.getText().toString().trim()));
//                break;
//            case 2:
//                if (renJian == -1) {
//                    toast("妊检必须选择");
//                    return;
//                }
//                updateList.addAll(collot());
//                break;
//            case 3:
//                updateList.addAll(collot());
//                String result = jk == 1 ? "健康" : "";
//                updateList.add(new UpdatePigEntity("healthy", result));
//                break;
//        }
        if (isCreate) {
            if (chaQing == -1) {
                Toast.makeText(MZSDetailActivity.this, "查情必须选择", Toast.LENGTH_SHORT).show();
                return;
            }
            pigCreate();
            return;
        }
        updateList.addAll(collot());
        String result = jk == 1 ? "健康" : "";
        updateList.add(new UpdatePigEntity("healthy", result));

        String content = new Gson().toJson(updateList);
//        long timeStampSec = System.currentTimeMillis() / 1000;
//        String timestamp = String.format("%010d", timeStampSec);
        RxHttp.postForm(Url.baseUrl + Url.PIG_CHANGE_STATUS)
                .addHeader("x-access-token", token)
                .add("hog_house_id", houseId)
                .add("ear_tag", ear_tag)
                .add("operator_time", System.currentTimeMillis())
                .add("content", content)
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            handler.sendEmptyMessage(2);
                        }
                    }
                }, throwable -> {
                    int i = 0;
                });
    }

    private List<UpdatePigEntity> collot() {
        List<UpdatePigEntity> list = new ArrayList<>();
        list.add(new UpdatePigEntity("estrus", chaQing == 0 ? "是" : "否"));
        list.add(new UpdatePigEntity("breed", etJybh.getText().toString().trim()));
        String result = "";
        switch (renJian) {
            case 0:
                result = "正常";
                break;
            case 1:
                result = "返情";
                break;
            case 2:
                result = "流产";
                break;
        }
        list.add(new UpdatePigEntity("pregnancy_detection", result));
        return list;
    }

    private void pigCreate() {
//        List<UpdatePigEntity> list = new ArrayList<>();
//        list.add(new UpdatePigEntity("estrus", chaQing == 0 ? "是" : "否"));
//        String result = new Gson().toJson(list);
        RxHttp.postForm(Url.baseUrl + Url.PIG_CREATE)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .add("hog_house_id", houseId)
                .add("status", "estrus")
                .add("result", chaQing == 0 ? "是" : "否")
                .add("operator_time", System.currentTimeMillis())
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
//                            initStatus();
                            isCreate = false;
                            Looper.prepare();
                            showDialog1();
//                            Toast.makeText(MZSDetailActivity.this, "数据提交成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if (s.getCode() == 10001) {
                            Looper.prepare();
                            Toast.makeText(MZSDetailActivity.this, s.getMsg(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
//                            initStatus();
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }
}
