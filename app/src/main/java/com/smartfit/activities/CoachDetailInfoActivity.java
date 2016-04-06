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
import com.smartfit.beans.CoachDetailInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 教练资料详情
 * 个人---认证后的教练
 */
public class CoachDetailInfoActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_headerr)
    SelectableRoundedImageView ivHeaderr;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_motto)
    TextView tvMotto;
    @Bind(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @Bind(R.id.tv_update_pass)
    TextView tvUpdatePass;
    @Bind(R.id.tv_edit_brief)
    TextView tvEditBrief;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_teached_classes)
    TextView tvTeachedClasses;
    @Bind(R.id.tv_weight)
    TextView tvWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail_info);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.account_info));
        getCoachInfo();
    }

    /**
     * 获取教练信息
     */
    private void getCoachInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("coachId", "12");
        PostRequest request = new PostRequest(Constants.COACH_GETCOACHINFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                mSVProgressHUD.dismiss();
                CoachDetailInfo userInfoDetail = JsonUtils.objectFromJson(response, CoachDetailInfo.class);
                if (userInfoDetail != null) {
                    fillData(userInfoDetail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CoachDetailInfoActivity.this);
        mQueue.add(request);

    }

    private void fillData(CoachDetailInfo userInfoDetail) {

        ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(), ivHeaderr, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(userInfoDetail.getNickName())) {
            tvName.setText(userInfoDetail.getNickName());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getSex())) {
            if (userInfoDetail.getSex().equals("0")) {
                tvSex.setText("女");
            } else {
                tvSex.setText("男");
            }
        }

        if (!TextUtils.isEmpty(userInfoDetail.getCoachDesc())) {
            tvEditBrief.setText(userInfoDetail.getCoachDesc());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getHight())) {
            tvHeight.setText(userInfoDetail.getHight());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getWeight())) {
            tvWeight.setText(userInfoDetail.getWeight());
        }
        if (!TextUtils.isEmpty(userInfoDetail.getAuthenCoachClassDesc())) {
             tvTeachedClasses.setText(userInfoDetail.getAuthenCoachClassDesc());
        }
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ForgetActivity.class);
            }
        });


        tvEditBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachBriefActivity.class);
            }
        });
    }
}
