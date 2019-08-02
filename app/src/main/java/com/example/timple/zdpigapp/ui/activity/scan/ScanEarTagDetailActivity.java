package com.example.timple.zdpigapp.ui.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.ui.activity.record.CFDetailActivity;
import com.example.timple.zdpigapp.ui.activity.record.MZSDetailActivity;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanEarTagDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.rcv_pig_list)
    RecyclerView rcvPigList;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.ll_pro)
    LinearLayout llPro;

    private List<String> readList;
    private int count = 0;
    private String houseId;

    private ModuleConnector connector;
    private RFIDReaderHelper mReader;
    private RXObserver rxObserver = new RXObserver() {
        @Override
        protected void onInventoryTag(final RXInventoryTag tag) {
            super.onInventoryTag(tag);
            if (!readList.contains(tag.strEPC))
                readList.add(tag.strEPC.replace(" ", ""));
        }

        @Override
        protected void onInventoryTagEnd(final RXInventoryTag.RXInventoryTagEnd tagEnd) {
//            mReader.realTimeInventory((byte) 0xff, (byte) 0x01);
            count++;
            if (tagEnd.mTotalRead == readList.size() && count == 1) {

                Intent intent = null;
                if (houseId.equals("10")) {
                    intent = new Intent(ScanEarTagDetailActivity.this, MZSDetailActivity.class);
                } else if (houseId.equals("11")) {
                    intent = new Intent(ScanEarTagDetailActivity.this, CFDetailActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("ear_tag", readList.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_ear_tag_detail_activity);
        ButterKnife.bind(this);

        houseId = SPUtils.getInstance().getString("houseId");
        readList = new ArrayList<>();

        llPro.setVisibility(View.VISIBLE);
        rcvPigList.setVisibility(View.GONE);
        init();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                if (event.getKeyCode() == KeyEvent.KEYCODE_F4) {
                    if (connector.isConnected()) {
                        ModuleManager.newInstance().setUHFStatus(true);
                        try {
                            if (mReader == null) {
                                mReader = RFIDReaderHelper.getDefaultHelper();
                                mReader.registerObserver(rxObserver);

                                Thread.currentThread().sleep(500);

                                mReader.realTimeInventory((byte) 0xff, (byte) 0x01);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case KeyEvent.ACTION_UP:
                ModuleManager.newInstance().setUHFStatus(false);
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @OnClick({R.id.btn_sure, R.id.btn_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }

    private void init() {
        connector = new ReaderConnector();
        connector.connectCom("dev/ttyS4", 115200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModuleManager.newInstance().setUHFStatus(false);
        if (mReader != null) {
            mReader.unRegisterObserver(rxObserver);
        }
        if (connector != null) {
            connector.disConnect();
        }
        ModuleManager.newInstance().setUHFStatus(false);
        ModuleManager.newInstance().release();
    }
}
