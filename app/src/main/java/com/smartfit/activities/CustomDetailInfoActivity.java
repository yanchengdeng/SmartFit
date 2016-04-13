package com.smartfit.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.UpdateCoachInfo;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.SelectableRoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

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
    private UpdateCoachInfo updateCoachInfo;

    private String vertifyStatus = "0";

    private EventBus eventBus;
    //上传身份证
    private ArrayList<String> photoes = new ArrayList<>();

    private static int REUQUST_HEAD_PHOTO = 0X016;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_detail_info);
        eventBus = EventBus.getDefault();
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

    private void MaterialDialogDefault(final int flag) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        final EditText etContent = (EditText) dialog.getWindow().findViewById(R.id.et_update_content);
        switch (flag) {
            case 1:
                tvTittle.setText("修改昵称");
                break;
            case 3:
                tvTittle.setText("修改性别");
                break;
            case 4:
                tvTittle.setText("修改签名");
                break;

        }

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
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("未填写信息");
                    return;
                } else {
                    saveUserInfo(etContent.getEditableText().toString(), flag);
                }
            }
        });
    }

    /**
     * 上传用户信息
     *
     * @param tag
     * @param index 规定：1：昵称
     *              2.头像
     *              3.性别
     *              4.签名
     *              5.手机号
     *              6.身高
     *              7.体重
     */
    private void saveUserInfo(final String tag, final int index) {
        updateCoachInfo = new UpdateCoachInfo();
        mSVProgressHUD.showWithStatus("提交中..", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> datas = new HashMap<>();
        if (index == 1) {
            datas.put("nickName", tag);
            updateCoachInfo.setNickName(tag);
        }
        if (index == 2) {
            datas.put("userPicUrl", tag);
            updateCoachInfo.setUserPicUrl(tag);
        }
        if (index == 3) {
            datas.put("sex", tag);
            updateCoachInfo.setSex(tag);
        }
        if (index == 4) {
            datas.put("signature", tag);
            updateCoachInfo.setSignature(tag);
        }


        PostRequest request = new PostRequest(Constants.USER_SAVEUSERINFO, datas, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus("已更新", SVProgressHUD.SVProgressHUDMaskType.Clear);
                mSVProgressHUD.dismiss();
                eventBus.post(updateCoachInfo);
                switch (index) {
                    case 1:
                        tvName.setText(tag);
                        break;
                    case 3:
                        if (tag.equals("0")) {
                            tvSex.setText("女");
                        } else {
                            tvSex.setText("男");
                        }
                        break;
                    case 4:
                        tvMotto.setText(tag);
                        break;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomDetailInfoActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivHeaderr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomDetailInfoActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                // 默认选择
                if (photoes != null && photoes.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, photoes);
                }
                startActivityForResult(intent, REUQUST_HEAD_PHOTO);
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(1);
            }
        });


        tvSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MateralDialogSex();
            }
        });

        tvMotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(4);
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
//                    openActivity(WaitVertifyActivity.class);
                    openActivity(CoachAuthBaseActivity.class);
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

    private void MateralDialogSex() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_edit_sex);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveUserInfo("1", 3);
            }
        });
        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                saveUserInfo("0", 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REUQUST_HEAD_PHOTO) {
            if (resultCode == RESULT_OK) {
                photoes = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (null != photoes && photoes.size() > 0) {
                    getCardUrl(photoes);
                }
            }
        }
    }

    private void getCardUrl(ArrayList<String> photoes) {
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
        params.setMultipart(true);
        try {
            for (String item : photoes) {
                params.addBodyParameter("imageFile", new File(item));
            }
        } catch (Exception ex) {
        }
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {

                String url = null;
                try {
                    url = result.getString("data");
                    ImageLoader.getInstance().displayImage(url, ivHeaderr, Options.getListOptions());
                    saveUserInfo(url, 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.w("dyc", "" + ex.getMessage() + "..." + ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

}
