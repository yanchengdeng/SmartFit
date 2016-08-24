package com.smartfit.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.VersionInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_new_function)
    TextView tvNewFunction;
    @Bind(R.id.tv_check_udpate)
    TextView tvCheckUdpate;
    @Bind(R.id.tv_user_deal)
    TextView tvUserDeal;

    private String downLoadURL = "/download/smartfit.apk";
    DownLoadCompleteReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        tvTittle.setText(getString(R.string.about_us));
        tvVersion.setText("V" + DeviceUtil.getVersionName(this));
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        receiver = new DownLoadCompleteReceiver();
        registerReceiver(receiver, filter);
    }

    @OnClick({R.id.iv_back, R.id.tv_new_function, R.id.tv_check_udpate, R.id.tv_user_deal})
    public void onClick(View view) {
        Intent intent = new Intent(AboutUsActivity.this, UserDealActivity.class);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_new_function:
                intent.putExtra(Constants.PASS_STRING, Constants.VERSIONMANAGER_GETINTRODUCE);
                intent.putExtra("name", "新功能介绍");
                startActivity(intent);
                break;
            case R.id.tv_check_udpate:
                getLastVersion();
                break;
            case R.id.tv_user_deal:
                intent.putExtra(Constants.PASS_STRING, Constants.VERSIONMANAGER_GETPROTOCOL);
                intent.putExtra("name", "使用协议");
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取最新版本信息
     */
    private void getLastVersion() {
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.VERSIONMANAGER_GETLATESTUPDATE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                VersionInfo versionInfo = JsonUtils.objectFromJson(response.toString(), VersionInfo.class);
                if (versionInfo != null) {
                    if (versionInfo.getVersionCode().equals(DeviceUtil.getVersionCode(AboutUsActivity.this))) {
                        mSVProgressHUD.showSuccessWithStatus("已是最新版本", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    } else {
                        showUpdateDialog(versionInfo);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.headers = NetUtil.getRequestBody(AboutUsActivity.this);
        GetSingleRequestUtils.getInstance(AboutUsActivity.this).getRequestQueue().add(request);
    }

    /**
     * 版本升级对话框
     *
     * @param versionInfo
     */
    private void showUpdateDialog(final VersionInfo versionInfo) {

        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_app_update);

        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        tvTittle.setText(String.format("版本：%s | 大小：%sM | 更新日期：%s", new Object[]{versionInfo.getVersionName(), versionInfo.getPackageSize(), DateUtils.getData(versionInfo.getUpdateTime(), "yyyy-MM-dd")}));

        TextView tvContent = (TextView) dialog.getWindow().findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(versionInfo.getContent())) {
            tvContent.setText(versionInfo.getContent());
        }

        if (versionInfo.getForceUpdate().equals("1")) {
            dialog.getWindow().findViewById(R.id.cancel_action).setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(versionInfo.getUrl())) {
                    doDownLoadApp(versionInfo.getUrl());
                }
            }
        });
    }

    /**
     * 下载最新版本app
     *
     * @param filepath
     */
    private void doDownLoadApp(String filepath) {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//下载请求
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(filepath));
//设置允许使用的网络类型
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//发出通知，既后台下载
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        down.setMimeType("application/vnd.android.package-archive");
        //下载完成文件存放的位置
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "smartfit.apk");
        down.setTitle("正在下载");
//将下载请求放入队列中,开始下载
        manager.enqueue(down);
    }


    /**
     * 作者： 邓言诚 创建于： 2016/7/18 11:42.
     */
    class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + downLoadURL)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

            }
        }
    }


}
