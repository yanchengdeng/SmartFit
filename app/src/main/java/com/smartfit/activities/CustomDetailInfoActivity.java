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
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.SelectableRoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户资料详情
 */
public class CustomDetailInfoActivity extends BaseActivity {

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
    @Bind(R.id.tv_coach_auth_status)
    TextView tvCoachAuthStatus;


    private String vertifyStatus = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_detail_info);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.account_info));
        getUserInfo();


    }

    private void getUserInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    fillData(userInfoDetail);
                }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomDetailInfoActivity.this);
        mQueue.add(request);
    }

    /***
     * 填充用户信息
     *
     * @param userInfoDetail
     */
    private void fillData(UserInfoDetail userInfoDetail) {
        ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(), ivHeaderr, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(userInfoDetail.getNickName())) {
            tvName.setText(userInfoDetail.getNickName());
        }


        if (!TextUtils.isEmpty(userInfoDetail.getSex()) && userInfoDetail.getSex().equals("0")) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }

        if (!TextUtils.isEmpty(userInfoDetail.getSignature())) {
            tvMotto.setText(userInfoDetail.getSignature());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getMobile())) {
            tvBindPhone.setText(userInfoDetail.getMobile());
        }
        String coachStatus = userInfoDetail.getIsICF();
        if (!TextUtils.isEmpty(coachStatus)) {
            vertifyStatus = coachStatus;
            if (coachStatus.equals("0")) {
                tvCoachAuthStatus.setText("尚未认证");
            } else if (coachStatus.equals("1")) {
                tvCoachAuthStatus.setText("上线");
            } else if (coachStatus.equals("2")) {
                tvCoachAuthStatus.setText("下线");
            } else if (coachStatus.equals("5")) {
                tvCoachAuthStatus.setText("审核未通过");
            } else if (coachStatus.equals("4")) {
                tvCoachAuthStatus.setText("审核中");
            }
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


        tvCoachAuthStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (vertifyStatus.equals("0")) {
                    openActivity(CoachAuthBaseActivity.class);
                } else if (vertifyStatus.equals("1")) {
                    openActivity(WaitVertifyActivity.class);
                    //TODO
//                    openActivity(CoachAuthentitionActivity.class);
                } else if (vertifyStatus.equals("2")) {
                    mSVProgressHUD.showInfoWithStatus("下线");
                } else if (vertifyStatus.equals("5")) {
                    openActivity(CoachAuthBaseActivity.class);
                } else {
                    openActivity(WaitVertifyActivity.class);
                }
            }
        });

    }
}
