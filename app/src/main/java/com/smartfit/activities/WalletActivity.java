package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.UpdateWalletInfo;
import com.smartfit.R;
import com.smartfit.adpters.WalletAdapter;
import com.smartfit.beans.AccountRecord;
import com.smartfit.beans.AccountRecordList;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {
    private PullToZoomListViewEx listView;


    private WalletAdapter walletAdapter;

    private List<AccountRecord> accountRecords = new ArrayList<>();

    private EventBus eventBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        listView = (PullToZoomListViewEx) findViewById(R.id.listview);
        initView();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (12.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        listView.setParallax(true);
        ListView lv =listView.getPullRootView();
        listView.setBackgroundColor(getResources().getColor(R.color.gray_light));
        lv.addHeaderView(LayoutInflater.from(this).inflate(R.layout.wallet_listview_header, null));
        lv.setHeaderDividersEnabled(false);
        lv.setFooterDividersEnabled(false);
        lv.setDividerHeight(0);


        getRecordList();
    }


    @Override
    public void onResume() {
        super.onResume();
        String userinfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO,"");
        if (!TextUtils.isEmpty(userinfo)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(userinfo,UserInfoDetail.class);
            if(userInfoDetail!= null){
                ImageView ivheader = (ImageView) listView.getPullRootView().findViewById(R.id.iv_header);
                ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(),ivheader, Options.getHeaderOptions());
                TextView tvName = (TextView) listView.getPullRootView().findViewById(R.id.tv_name);
                if (!TextUtils.isEmpty(userInfoDetail.getNickName())) {
                    tvName.setText(userInfoDetail.getNickName());
                }

                TextView tvBallence = (TextView) listView.getPullRootView().findViewById(R.id.tv_money);
                if (!TextUtils.isEmpty(userInfoDetail.getBalance())) {
                    tvBallence.setText(userInfoDetail.getBalance());
                }
            }
        }
    }

    @Subscribe
    public void onEvent(UpdateWalletInfo event) {
        getRecordList();
    }

    private void getRecordList() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.RECORD_GETRECORDLIST, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                AccountRecordList accountRecordList = JsonUtils.objectFromJson(response,AccountRecordList.class);
                if(accountRecordList!= null &&accountRecordList.getListData().size()>0){
                    walletAdapter = new WalletAdapter(accountRecordList.getListData(),WalletActivity.this);
                    listView.setAdapter(walletAdapter);
                }else{
                    walletAdapter = new WalletAdapter(accountRecords,WalletActivity.this);
                    listView.setAdapter(walletAdapter);
                }
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                walletAdapter = new WalletAdapter(accountRecords,WalletActivity.this);
                listView.setAdapter(walletAdapter);

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WalletActivity.this);
        mQueue.add(request);
    }

    private void initView() {


        listView.getPullRootView().findViewById(R.id.btn_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ReChargeActivity.class);
            }
        });

        listView.getPullRootView().findViewById(R.id.btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WithDrawActivity.class);
            }
        });

        listView.getPullRootView().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
