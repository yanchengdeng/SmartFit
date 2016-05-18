package com.smartfit.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.UploadAuthSuccess;
import com.smartfit.R;
import com.smartfit.adpters.MoreCertiaicateVertifyAdapter;
import com.smartfit.beans.SubmitAuthInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 审核通过后
 *
 * @author yanchengdeng
 *         create at 2016/5/18 14:36
 */
public class VertifyFinishedActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_name_tips)
    TextView tvNameTips;
    @Bind(R.id.tv_name)
    EditText tvName;
    @Bind(R.id.cb_name)
    ImageView cbName;
    @Bind(R.id.tv_card_tips)
    TextView tvCardTips;
    @Bind(R.id.tv_card)
    EditText tvCard;
    @Bind(R.id.cb_card)
    ImageView cbCard;
    @Bind(R.id.tv_card_photo_tips)
    TextView tvCardPhotoTips;
    @Bind(R.id.image_card)
    ImageView imageCard;
    @Bind(R.id.cb_card_photo)
    ImageView cbCardPhoto;
    @Bind(R.id.tv_work_quality)
    TextView tvWorkQuality;
    @Bind(R.id.image_work)
    ImageView imageWork;
    @Bind(R.id.cb_work_photo)
    ImageView cbWorkPhoto;
    @Bind(R.id.list_auth_photos)
    MyListView listAuthPhotos;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    @Bind(R.id.btn_del)
    Button btnDel;


    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_finished);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
    }


    @Subscribe
    public void onEvent(Object event) {/* Do something */
        if (event instanceof UploadAuthSuccess) {
            finish();
        }
    }

    private void initView() {
        tvTittle.setText(getString(R.string.coach_auth));
        getVertifyInfo();
    }

    /**
     * 获取教练审核信息
     */
    private void getVertifyInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.COACH_LISTCERTIFICATE, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                List<SubmitAuthInfo> submitAuthInfoList = JsonUtils.listFromJson(response.getAsJsonArray("list"), SubmitAuthInfo.class);
                if (submitAuthInfoList != null && submitAuthInfoList.size() > 0)
                    fillData(submitAuthInfoList);

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(VertifyFinishedActivity.this);
        mQueue.add(request);
    }

    /**
     * 填充资料信息
     *
     * @param submitAuthInfoList
     */
    List<SubmitAuthInfo> submitAuthInfoList;

    private void fillData(List<SubmitAuthInfo> submitAuthInfoList) {
        this.submitAuthInfoList = submitAuthInfoList;
        List<SubmitAuthInfo> workAuths = new ArrayList<>();
        for (SubmitAuthInfo item : submitAuthInfoList) {
            if (item.getType().equals("1")) {
                //身份证
                tvCard.setText(item.getCertificateName());
                tvName.setText(item.getCoachRealName());
                ImageLoader.getInstance().displayImage(item.getCertificateImg(), imageCard, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                } else if (item.getStatus().equals("2")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                }
            }

            if (item.getType().equals("2")) {
                //正式照片
                ImageLoader.getInstance().displayImage(item.getCertificateImg(), imageWork, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                } else if (item.getStatus().equals("2")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);

                }
            }

            if (item.getType().equals("3")) {
                workAuths.add(item);
            }
        }

        if (workAuths.size() > 0) {
            listAuthPhotos.setAdapter(new MoreCertiaicateVertifyAdapter(VertifyFinishedActivity.this, workAuths));
        }
    }


    @OnClick({R.id.iv_back, R.id.btn_submmit, R.id.btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submmit:
                if (null != submitAuthInfoList && submitAuthInfoList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PASS_STRING, submitAuthInfoList.get(0).getCoachRealName());
                    openActivity(AddVertifyInfoActivity.class, bundle);
                }
                break;
            case R.id.btn_del:
                if (null != submitAuthInfoList && submitAuthInfoList.size() > 0) {
                    MaterialDialogDefault();
                }
                break;
        }
    }


    private void MaterialDialogDefault() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        tvTittle.setText(getString(R.string.plasas_input_your_login_pass));
        final EditText etContent = (EditText) dialog.getWindow().findViewById(R.id.et_update_content);
        etContent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
                    mSVProgressHUD.showInfoWithStatus("登录密码不能为空", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                } else {
                    String password = SharedPreferencesUtils.getInstance().getString(Constants.PASSWORD, "");
                    if (etContent.getEditableText().toString().equals(password)) {
                        delAutoInfo();
                    } else {
                        mSVProgressHUD.showInfoWithStatus("登录密码不正确", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }
            }
        });
    }

    private void delAutoInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.delete_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.COACH_DELCERTIFICATE, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已删除", SVProgressHUD.SVProgressHUDMaskType.Clear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openActivity(CoachAuthBaseActivity.class);
                        finish();
                    }
                }, 1500);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(VertifyFinishedActivity.this);
        mQueue.add(request);
    }
}
