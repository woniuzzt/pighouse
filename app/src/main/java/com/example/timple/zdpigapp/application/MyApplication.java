package com.example.timple.zdpigapp.application;

import android.app.Application;

import com.example.timple.zdpigapp.utils.Utils;
import com.lzy.okgo.OkGo;

import rxhttp.HttpSender;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpSender.init(null, true);
        Utils.init(getApplicationContext());
        OkGo.getInstance().init(this);
    }
}
