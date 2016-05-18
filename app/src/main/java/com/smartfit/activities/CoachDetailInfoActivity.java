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
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.SelectableRoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
    @Bind(R.id.tv_already_auth)
    TextView tvAlreadyAuth;

    private EventBus eventBus;
    //上传身份证
    private ArrayList<String> photoes = new ArrayList<>();

    private static int REUQUST_HEAD_PHOTO = 0X015;
    private UpdateCoachInfo updateCoachInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail_info);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }


    @Subscribe
    public void onEvent(String status) {/* Do something */
        if (!TextUtils.isEmpty(status)) {
            if (status.equals("couserTypes")) {
                tvTeachedClasses.setText(getString(R.string.alreay_setting));
            } else {
                tvEditBrief.setText(status);
                tvEditBrief.setTextColor(getResources().getColor(R.color.common_header_bg));

            }
        }
    }

    ;

    private void initView() {
        tvTittle.setText(getString(R.string.account_info));
        getCoachInfo();
    }

    /**
     * 获取教练信息
     */
    private void getCoachInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    fillData(userInfoDetail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CoachDetailInfoActivity.this);
        mQueue.add(request);


    }

    UserInfoDetail userInfoDetail;

    private void fillData(UserInfoDetail userInfoDetail) {
        this.userInfoDetail = userInfoDetail;
        ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(), ivHeaderr, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(userInfoDetail.getNickName())) {
            tvName.setText(userInfoDetail.getNickName());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getSex())) {
            if (userInfoDetail.getSex().equals(Constants.SEX_WOMEN)) {
                tvSex.setText("女");
            } else {
                tvSex.setText("男");
            }
        }

        if (!TextUtils.isEmpty(userInfoDetail.getSignature())) {
            tvMotto.setText(userInfoDetail.getSignature());
        }

        if (!TextUtils.isEmpty(userInfoDetail.getHight())) {
            tvHeight.setText(userInfoDetail.getHight() + "CM");
        }

        /**
         * 0未设置；1待审2审核通过3；审核不通过
         */
        if (!TextUtils.isEmpty(userInfoDetail.getIsCCC())) {
            if (userInfoDetail.getIsCCC().equals("0")) {
                tvEditBrief.setText(getString(R.string.not_setting));
            } else if (userInfoDetail.getIsCCC().equals("1")) {
                tvEditBrief.setText("待审核");
            } else if (userInfoDetail.getIsCCC().equals("2")) {
                tvEditBrief.setText("审核通过");
            } else {
                tvEditBrief.setText("审核不通过");
            }
        }

        tvBindPhone.setText(SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "未绑定"));

        if (!TextUtils.isEmpty(userInfoDetail.getWeight())) {
            tvWeight.setText(userInfoDetail.getWeight() + "KG");
        }
        if (!TextUtils.isEmpty(userInfoDetail.getIsCTC())) {
            if (userInfoDetail.getIsCTC().equals("0")) {
                tvTeachedClasses.setText(getString(R.string.not_setting));
            } else {
                tvTeachedClasses.setText(getString(R.string.alreay_setting));
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

        ivHeaderr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoachDetailInfoActivity.this, MultiImageSelectorActivity.class);
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

        tvAlreadyAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 教练状态0：未申请 1上线, 3锁定.4审核中；5审核未通过
                 */
                if (null != userInfoDetail && !TextUtils.isEmpty(userInfoDetail.getIsICF())) {
                    if (userInfoDetail.getIsICF().equals("0")) {
                        openActivity(CoachAuthBaseActivity.class);
                    } else if (userInfoDetail.getIsICF().equals("1")) {
                        openActivity(VertifyFinishedActivity.class);
                    } else if (userInfoDetail.getIsICF().equals("2")) {
                        mSVProgressHUD.showInfoWithStatus("下线");
                    } else if (userInfoDetail.getIsICF().equals("5")) {
                        openActivity(VertifyNotPassActivity.class);
                    } else {
                        openActivity(WaitVertifyActivity.class);
                    }
                }
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

        tvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(6);
            }
        });

        tvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(7);
            }
        });


        tvUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ForgetPasswordActivity.class);
            }
        });


        tvEditBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachBriefActivity.class);
            }
        });

        tvTeachedClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(TeachClassListActivity.class);
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
                saveUserInfo(Constants.SEX_MAN, 3);
            }
        });
        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                saveUserInfo(Constants.SEX_WOMEN, 3);
            }
        });
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
            case 6:
                tvTittle.setText("修改身高");
                break;
            case 7:
                tvTittle.setText("修改体重");
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
        if (index == 5) {
            datas.put("mobileNo", tag);
        }
        if (index == 6) {
            datas.put("hight", tag);
        }
        if (index == 7) {
            datas.put("weight", tag);
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
                        if (tag.equals(Constants.SEX_WOMEN)) {
                            tvSex.setText("女");
                        } else {
                            tvSex.setText("男");
                        }
                        break;
                    case 4:
                        tvMotto.setText(tag);
                        break;
                    case 6:
                        tvHeight.setText(tag);
                        break;
                    case 7:
                        tvWeight.setText(tag);
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
        request.headers = NetUtil.getRequestBody(CoachDetailInfoActivity.this);
        mQueue.add(request);
    }
}
