package com.example.timple.zdpigapp.ui.activity.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timple.zdpigapp.R;
import com.example.timple.zdpigapp.Url;
import com.example.timple.zdpigapp.entity.BaseData;
import com.example.timple.zdpigapp.entity.UpdateVersionEntity;
import com.example.timple.zdpigapp.ui.activity.RecordActivity;
import com.example.timple.zdpigapp.ui.activity.turn.TurnGroupActivity;
import com.example.timple.zdpigapp.utils.BaseActivity;
import com.example.timple.zdpigapp.utils.SPUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rxhttp.wrapper.param.RxHttp;


public class MainActivity extends BaseActivity {
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;
    @BindView(R.id.btn_turn_group)
    Button btnTurnGroup;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.tv_work_reminder)
    TextView tvWorkReminder;
    @BindView(R.id.tv_work_record)
    TextView tvWorkRecord;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int MY_PERMISSIONS_REQUEST_DOWNLOAD = 1;

    private DownLoader downLoader;

    private List<String> mPermissionList;

    private boolean isForceUpdate = false;//是否强制更新

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvWorkReminder.setText("工作提醒\n1.XXX\n2.XXX\n3.XXX\n4.XXX");
        tvWorkRecord.setText("工作记录\n1.XXX\n2.XXX\n3.XXX\n4.XXX");

        // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
        mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        downLoader = new DownLoader() {
            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadSuccess(String path) {
                installApk(MainActivity.this, path);
            }
        };
        RxHttp.postForm(Url.baseUrl + Url.ACCOUNT_TOKEN)
                .addHeader("x-access-token", SPUtils.getInstance().getString("token"))
                .asObject(BaseData.class)
                .subscribe(s -> {
                    if (s != null) {
                        if (s.getCode() == 10000) {
                            checkUpdate();
                        }
                        if (s.getCode() == 10307) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }, throwable -> {
                });
    }

    private void checkUpdate() {
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            RxHttp.postForm(Url.baseUrl + Url.UPDATE_VERSION)
                    .addHeader("x-access-token", SPUtils.getInstance().getString("token"))
                    .asObject(BaseData.class)
                    .subscribe(s -> {
                        if (s != null) {
                            if (s.getCode() == 10000) {
                                UpdateVersionEntity entity = new Gson().fromJson(s.getData(), UpdateVersionEntity.class);
                                int versionCode = Integer.valueOf(entity.getId());
                                int thisVersionCode = getVersionCode(MainActivity.this);
                                if (versionCode > thisVersionCode) {
//                                    downloadFile(entity.getDownload_url());
                                    Looper.prepare();
                                    if (entity.getIs_force_update() == 0) {
                                        showDialog(entity.getDownload_url());
                                    } else {
                                        showDialog1(entity.getDownload_url());
//                                        downloadApk(entity.getDownload_url());
                                    }
                                    Looper.loop();
                                }
                            }
                        }
                    }, throwable -> {
                    });
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST_DOWNLOAD);
        }
    }

    private void showDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("检测到有新版本,是否更新?")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Toast.makeText(MainActivity.this, "正在更新", Toast.LENGTH_SHORT).show();
                            downloadApk(url);
                            if (dialog != null) dialog.dismiss();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void showDialog1(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("检测到有新版本,请立即更新?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            downloadApk(url);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setCancelable(false);
        builder.show();
    }

    private void downloadApk(String apkUrl) throws PackageManager.NameNotFoundException {

        Uri uri = Uri.parse(apkUrl);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(request.NETWORK_MOBILE | request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(true);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkUrl));
        request.setMimeType(mimeString);
        //在通知栏中显示
        request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("正在更新正大智慧猪场...");
        request.setVisibleInDownloadsUi(true);
        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir("/download", "app_debug.apk");
        // 将下载请求放入队列
        downloadManager.enqueue(request);
    }

    public static void installApk(Context context, String downloadApk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    public void downloadFile(String url) {
        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD", "startTime=" + startTime);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD", "download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, url.substring(url.lastIndexOf("/") + 1));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        downLoader.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    downLoader.onDownloadSuccess(savePath + "app-debug.apk");
                    Log.i("DOWNLOAD", "download success");
                    Log.i("DOWNLOAD", "totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
//                    listener.onDownloadFailed();
                    Log.i("DOWNLOAD", "download failed");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private interface DownLoader {
        void onDownloading(int progress);

        void onDownloadSuccess(String path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_DOWNLOAD) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        showToast("权限未申请");
                    } else {
                        RxHttp.postForm(Url.baseUrl + Url.UPDATE_VERSION)
                                .addHeader("x-access-token", SPUtils.getInstance().getString("token"))
                                .asObject(BaseData.class)
                                .subscribe(s -> {
                                    if (s != null) {
                                        if (s.getCode() == 10000) {
                                            UpdateVersionEntity entity = new Gson().fromJson(s.getData(), UpdateVersionEntity.class);
                                            int versionCode = Integer.valueOf(entity.getId());
                                            int thisVersionCode = getVersionCode(MainActivity.this);
                                            if (versionCode > thisVersionCode) {

                                            }
                                        }
                                    }
                                }, throwable -> {
                                });
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    Intent intent;
    @OnClick({R.id.btn_scan, R.id.btn_record, R.id.btn_refresh, R.id.btn_turn_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                Toast.makeText(MainActivity.this, "该功能暂未开放,敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_record:
                intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_refresh:
                Toast.makeText(MainActivity.this, "该功能暂未开放,敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_turn_group:
                intent = new Intent(MainActivity.this, TurnGroupActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static int getVersionCode(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }
}
