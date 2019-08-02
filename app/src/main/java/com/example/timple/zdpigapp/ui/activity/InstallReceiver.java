package com.example.timple.zdpigapp.ui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class InstallReceiver extends BroadcastReceiver {
 
    // 安装下载接收器
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            installApk(context);
        }
    }
 
    // 安装Apk
    private void installApk(Context context) {
 
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/app_debug.apk";
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
}