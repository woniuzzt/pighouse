package com.example.timple.zdpigapp.ui.activity.turn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.PigStatusEntity;
import com.example.timple.zdpigapp.ui.activity.scan.ScanEarTagDetailActivity;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxhttp.wrapper.param.RxHttp;

/**
 * 转群-》后备群
 */
public class HBQTurnToDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_cq)
    RadioGroup rgCq;
    @BindView(R.id.et_ssjs)
    EditText etSsjs;
    @BindView(R.id.et_sslw_first)
    EditText etSslwFirst;
    @BindView(R.id.et_sslw_second)
    EditText etSslwSecond;


    private String token;
    private String ear_tag;
    private String statusName;
    private int updateStutas = -1;
    private String hogHouseName;
    private String filed;
    private String hogId;
    private String houseId;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://获取状态更新UI
                    tvId.setText("当前ID:" + hogId);
                    tvStatus.setText("状态:" + statusName);
                    break;
                case 1://转群成功
                    showDialog();
                    break;
            }
        }
    };
    private String tvJs;
    private String tvLw;
    private String tvLw2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbqturn_to_detail);
        ButterKnife.bind(this);

        tvToolbar.setText("转群 → 后备群");

        token = SPUtils.getInstance().getString("token");
        ear_tag = getIntent().getExtras().getString("ear_tag");
        houseId = SPUtils.getInstance().getString("houseId");


        getPigInfo();
    }

    /**
     * 获取猪的信息
     */
    private void getPigInfo() {

        RxHttp.postForm(Url.baseUrl + Url.PIG_STATUS)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            PigStatusEntity entity = new Gson().fromJson(s.getData(), PigStatusEntity.class);
                            statusName = entity.getStatus_desc();
                            hogId = entity.getEar_tag();
                            hogHouseName = entity.getHog_house_name();
                            filed = entity.getFiled();
                            handler.sendEmptyMessage(0);
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });

    }

    @OnClick({R.id.rb_yes, R.id.rb_no, R.id.back, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_yes:
                updateStutas = 0;
                break;
            case R.id.rb_no:
                updateStutas = 1;
                break;
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                turnGroup();
                break;
        }
    }

    /**
     * 确认转群
     */
    private void turnGroup() {
        tvJs = etSsjs.getText().toString().trim();
        tvLw = etSslwFirst.getText().toString().trim();
        tvLw2 = etSslwSecond.getText().toString().trim();
        if (updateStutas == -1){
            toast("请选择是否更新状态");
            return;
        }
        if (TextUtils.isEmpty(tvJs)){
            toast("请输入所属圈舍");
            return;
        }

        if (TextUtils.isEmpty(tvLw)|| TextUtils.isEmpty(tvLw2)){
            toast("请输入所属栏位");
            return;
        }
        RxHttp.postForm(Url.baseUrl + Url.TURN_GROUP)
                .addHeader("x-access-token", token)
                .add("ear_tag", ear_tag)
                .add("current_hog_house",houseId)
                .add("sub_hog_house", tvJs)
                .add("is_update_status", updateStutas+"")
                .add("current_field",tvLw+"-"+tvLw2)// TODO: 2019/8/4
                .add("operator_time", System.currentTimeMillis())
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                }, throwable -> {
                    Log.e("123", "onInventoryTagEnd: ");
                });
    }

    private void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("转群成功！")
                .setMessage(hogHouseName +">>>母猪舍"+"\n"+"(舍"+houseId+"栏"+filed+">>>舍"+tvJs+"栏"+tvLw+"-"+tvLw2+")")
                .setPositiveButton("下一个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HBQTurnToDetailActivity.this, ScanEarTagDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("结束", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }
}
