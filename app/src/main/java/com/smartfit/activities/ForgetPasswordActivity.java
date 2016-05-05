package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改  密码
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_old_pass)
    ImageView ivOldPass;
    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.iv_new_password)
    ImageView ivNewPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.iv_new_password_repeat)
    ImageView ivNewPasswordRepeat;
    @Bind(R.id.et_new_password_repeat)
    EditText etNewPasswordRepeat;
    @Bind(R.id.btn_reset)
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.update_password));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_reset)
    public void onClick() {
        checkParams(etOldPassword.getEditableText().toString(), etNewPassword.getEditableText().toString(), etNewPasswordRepeat.getEditableText().toString());
    }

    private void checkParams(String oldPass, String newPass, String newPassRepeat) {
        if (TextUtils.isEmpty(oldPass)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.please_input_old_password), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else if (TextUtils.isEmpty(newPass)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.please_input_new_password), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else if (newPass.length() < 6) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.please_input_new_password), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else if (TextUtils.isEmpty(newPassRepeat)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.please_input_password_again), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else if (newPassRepeat.length() < 6) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.please_input_new_password), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else if (!newPass.equals(newPassRepeat)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.twice_new_pass_not_eaqual));
        } else {
            doUpdatePassword(oldPass, newPass);
        }
    }

    /**
     * 更新密码
     *
     * @param oldPass
     * @param newPass
     */
    private void doUpdatePassword(String oldPass, String newPass) {
        mSVProgressHUD.showWithStatus(getString(R.string.reset_ing, SVProgressHUD.SVProgressHUDMaskType.Clear));
        Map<String, String> data = new HashMap<>();
        data.put("mobileNo", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, ""));
        data.put("password", MD5.getMessageDigest(oldPass.getBytes()));
        data.put("newPassword", MD5.getMessageDigest(newPass.getBytes()));
        PostRequest request = new PostRequest(Constants.RESET_PASSOWRD, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showInfoWithStatus("重置成功");
                showResetSuccessDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(ForgetPasswordActivity.this);
        mQueue.add(request);
    }

    private void showResetSuccessDialog() {
        final NormalDialog dialog = new NormalDialog(ForgetPasswordActivity.this);
        dialog.content("密码已更新！")//
                .btnNum(1)
                .btnText("确定")//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }
}
