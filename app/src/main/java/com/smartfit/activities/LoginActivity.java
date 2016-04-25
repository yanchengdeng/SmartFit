package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.LoginSuccess;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_name)
    ImageView ivName;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.iv_pass)
    ImageView ivPass;
    @Bind(R.id.et_pass)
    EditText etPass;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.ck_remeber)
    CheckBox ckRemeber;
    @Bind(R.id.tv_foget)
    TextView tvFoget;
    @Bind(R.id.iv_login_wechat)
    ImageView ivLoginWechat;
    @Bind(R.id.iv_login_sina)
    ImageView ivLoginSina;
    @Bind(R.id.iv_login_qq)
    ImageView ivLoginQq;
    @Bind(R.id.btn_login_phone)
    Button btnLoginPhone;
    @Bind(R.id.iv_header)
    ImageView ivHeader;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        initView();
        addLisener();
    }


    @Override
    public void onResume() {
        super.onResume();
        String userInfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfo)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(userInfo, UserInfoDetail.class);
            ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(), ivHeader, Options.getListOptions());
        }
    }

    private void initView() {
        String account = SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "");
        String pwd = SharedPreferencesUtils.getInstance().getString(Constants.PASSWORD, "");
        if (!TextUtils.isEmpty(account)) {
            etName.setText(account);
        }

        if (!TextUtils.isEmpty(pwd)) {
            etPass.setText(pwd);
        }
    }


    private void addLisener() {

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(etName.getEditableText().toString(), etPass.getEditableText().toString());
            }
        });


        tvFoget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ForgetActivity.class);
                finish();
            }
        });

        btnLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegisterActivity.class);
            }
        });


    }

    /**
     * 登录操作
     *
     * @param accont
     * @param password
     */
    private void doLogin(final String accont, final String password) {


        if (TextUtils.isEmpty(accont)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.account_cannot_empty), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }


        if (TextUtils.isEmpty(password)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.passowr_cannot_empt), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }


        mSVProgressHUD.showWithStatus(getString(R.string.login_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("mobileNo", accont);
        data.put("password", MD5.getMessageDigest(password.getBytes()));
        PostRequest request = new PostRequest(Constants.LOGIN_IN_METHOD, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(final JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.login_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ckRemeber.isChecked()) {
                            SharedPreferencesUtils.getInstance().putString(Constants.ACCOUNT, accont);
                            SharedPreferencesUtils.getInstance().putString(Constants.PASSWORD, password);
                        } else {
                            SharedPreferencesUtils.getInstance().putString(Constants.ACCOUNT, "");
                            SharedPreferencesUtils.getInstance().putString(Constants.PASSWORD, "");
                        }
                        UserInfoDetail customeInfo = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                        if (customeInfo != null) {
                            SharedPreferencesUtils.getInstance().putString(Constants.SID, customeInfo.getSid());
                            SharedPreferencesUtils.getInstance().putString(Constants.UID, customeInfo.getUid());
                            SharedPreferencesUtils.getInstance().putString(Constants.IS_ICF, customeInfo.getIsICF());
                            SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO, JsonUtils.toJson(customeInfo));
                            if (!TextUtils.isEmpty(customeInfo.getCoachId())) {
                                SharedPreferencesUtils.getInstance().putString(Constants.COACH_ID, customeInfo.getCoachId());
                            }
                        }

                        eventBus.post(new LoginSuccess());
                        String isICF = SharedPreferencesUtils.getInstance().getString(Constants.IS_ICF, "0");
                      /*  if (isICF.equals("1")) {
                            openActivity(CustomeCoachActivity.class);
                        } else {
                            openActivity(CustomeMainActivity.class);
                        }*/
                        finish();
                    }
                }, 1500);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getLocalizedMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);

            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(LoginActivity.this);
        mQueue.add(request);
    }

}


