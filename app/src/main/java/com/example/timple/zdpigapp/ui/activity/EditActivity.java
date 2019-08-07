package com.example.timple.zdpigapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.entity.BreedInfo;
import com.example.timple.zdpigapp.entity.FattenInfo;
import com.example.timple.zdpigapp.entity.PigBaseInfo;

import java.io.Serializable;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int type = getIntent().getIntExtra("type", -1);

        switch (type) {
            case 0:
                PigBaseInfo baseInfo = (PigBaseInfo) getIntent().getSerializableExtra("info");

                break;
            case 1:
                List<FattenInfo> fattenInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");
                break;
            case 2:
                List<BreedInfo> yFInfos = (List<BreedInfo>) getIntent().getSerializableExtra("info");
                break;
            case 3:
                List<FattenInfo> cZInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");

                break;
            case 4:
                List<FattenInfo> yZInfos = (List<FattenInfo>) getIntent().getSerializableExtra("info");
                break;

        }
    }
}
