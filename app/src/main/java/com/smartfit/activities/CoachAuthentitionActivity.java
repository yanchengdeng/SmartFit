package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.UserInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 教练认证信息
 */
public class CoachAuthentitionActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_icon)
    SelectableRoundedImageView ivIcon;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_base_info)
    TextView tvBaseInfo;
    @Bind(R.id.tv_auth_info)
    TextView tvAuthInfo;
    @Bind(R.id.tv_auth_info_more)
    TextView tvAuthInfoMore;
    @Bind(R.id.tv_classes)
    TextView tvClasses;
    @Bind(R.id.tv_age)
    TextView tvAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_authentition);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText("认证资料");
        getCoachInfo();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /**
     * 获取教练信息
     */
    private void getCoachInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));

        if (SharedPreferencesUtils.getInstance().getBoolean(Constants.OPEN_COACH_AUTH, false)) {
            maps.put("isCoach", "1");
        } else {
            maps.put("isCoach", "0");
        }
        PostRequest request = new PostRequest(Constants.MAIN_PAGE_INFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfo userInfo = JsonUtils.objectFromJson(response, UserInfo.class);
                if (null != userInfo) {
                    fillData(userInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CoachAuthentitionActivity.this);
        mQueue.add(request);
    }

    private void fillData(UserInfo userInfo) {
        ImageLoader.getInstance().displayImage(userInfo.getUserPicUrl(), ivIcon, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(userInfo.getNickName())) {
            tvName.setText(userInfo.getNickName()+" 教练");
        }
        if (!TextUtils.isEmpty(userInfo.getSex())) {
            if (userInfo.getSex().equals("0")) {
                tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_woman), null);
            } else {
                tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_man), null);

            }
        }

        if (!TextUtils.isEmpty(userInfo.getAge())) {
tvAge.setText(userInfo.getAge());
        }

        if (!TextUtils.isEmpty(userInfo.getHight()) && !TextUtils.isEmpty(userInfo.getWeight())) {
            tvBaseInfo.setText("身高："+userInfo.getHight()+" CM 体重："+userInfo.getWeight()+" KG");
        }

        if (!TextUtils.isEmpty(userInfo.getCertificates())) {
            tvAuthInfo.setText(userInfo.getCertificates());
        }
        if (!TextUtils.isEmpty(userInfo.getCoachClassDesc())) {
            tvAuthInfoMore.setText(userInfo.getCoachClassDesc());
        }

        if (!TextUtils.isEmpty(userInfo.getAuthenCoachClassDesc())) {
            tvClasses.setText(userInfo.getAuthenCoachClassDesc());
        }
    }

}
