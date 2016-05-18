package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UploadAuthSuccess;
import com.smartfit.R;
import com.smartfit.adpters.MoreCertiaicateAdapter;
import com.smartfit.beans.Certificate;
import com.smartfit.beans.CoachCertificateItem;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 添加认证信息
 */

public class AddVertifyInfoActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.list_auth_photos)
    MyListView listAuthPhotos;
    @Bind(R.id.tv_add_more_books)
    TextView tvAddMoreBooks;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    //更多工作证书
    private List<Certificate> certificateList = new ArrayList<>();
    private static int REQUEST_CERTIFICATE_MORE = 0x013;
    MoreCertiaicateAdapter moreCertiaicateAdapter;

    private int totalRequest = 0;

    private int positon = -1;

    private String realCoachName;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            UpdateCertificate(msg.what, (ArrayList<String>) msg.obj);
            return false;
        }
    });

    private void UpdateCertificate(int position, ArrayList<String> mSelectPath) {
        Certificate certificate = certificateList.get(position);
        positon = position;
        if (certificate != null) {
            goPhotoThum(mSelectPath, REQUEST_CERTIFICATE_MORE, 1);
        }
    }


    private EventBus eventBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vertify_info);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.add_more_auth_books));
        realCoachName = getIntent().getStringExtra(Constants.PASS_STRING);
        initMoreCentifacate();
    }

    private void initMoreCentifacate() {

        certificateList.add(getNewCertificate());
        moreCertiaicateAdapter = new MoreCertiaicateAdapter(this, certificateList, handler);
        listAuthPhotos.setAdapter(moreCertiaicateAdapter);

        //添加更多证书
        tvAddMoreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                certificateList.add(getNewCertificate());
                moreCertiaicateAdapter.setData(certificateList);
            }
        });

    }

    private Certificate getNewCertificate() {
        Certificate certificate = new Certificate();
        certificate.setText_tips("获得的证书" + (certificateList.size() + 1));
        certificate.setImage_tips("上传证书照片" + (certificateList.size() + 1));
        return certificate;
    }

    /**
     * 进入相册ji
     *
     * @param mSelectPath
     * @param requestCode
     */
    private void goPhotoThum(ArrayList<String> mSelectPath, int requestCode, int photos) {
        Intent intent = new Intent(AddVertifyInfoActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, photos);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
//            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CERTIFICATE_MORE) {
            ArrayList<String> photos = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (null != photos && photos.size() > 0) {
                getAuthUrl(photos.get(0));
            }
        }
    }

    /**
     * 获取该证书的图片url
     */
    private void getAuthUrl(final String certificateListUrl) {
        mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
        params.setMultipart(true);
        params.addBodyParameter("imageFile", new File(certificateListUrl));
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                mSVProgressHUD.dismiss();
                String url = null;
                try {
                    url = result.getString("data");
                    if (!TextUtils.isEmpty(url)) {
                        certificateList.get(positon).setPhoto(url);
                        moreCertiaicateAdapter.setData(certificateList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.w("dyc", "" + ex);
                mSVProgressHUD.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mSVProgressHUD.dismiss();
            }

            @Override
            public void onFinished() {
                mSVProgressHUD.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_submmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.btn_submmit:
                mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.Clear);
                List<Certificate> subcertificateList = new ArrayList<Certificate>();
                for (Certificate item : certificateList) {
                    if (!TextUtils.isEmpty(item.getPhoto()) && !TextUtils.isEmpty(item.getName())) {
                        subcertificateList.add(item);
                    }
                }

                if (subcertificateList.size() > 0) {
                    totalRequest = totalRequest + subcertificateList.size();
                    for (Certificate item : subcertificateList) {
                        CoachCertificateItem coachCertificateItem = new CoachCertificateItem(item.getName(), item.getPhoto(), "3");
                        doSubmit(realCoachName, coachCertificateItem.getCertificateName(), coachCertificateItem);
                    }
                } else {
                    mSVProgressHUD.showInfoWithStatus("请填写信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                break;
        }
    }

    private void doSubmit(String name, String card, CoachCertificateItem coachCertificateCard) {
        Map<String, String> map = new HashMap<>();
        map.put("coachRealName", name);
        map.put("certificateName", card);
        map.put("certificateImg", coachCertificateCard.getCertificateImg());
        map.put("type", coachCertificateCard.getType());

        PostRequest request = new PostRequest(Constants.COACH_ADD_CERTIFICATE, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                totalRequest--;
                mSVProgressHUD.dismiss();
                updateSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                totalRequest--;

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AddVertifyInfoActivity.this);
        mQueue.add(request);


    }

    private void updateSuccess() {
        if (totalRequest == 0) {
            mSVProgressHUD.showSuccessWithStatus("上传成功", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
            eventBus.post(new UploadAuthSuccess());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openActivity(WaitVertifyActivity.class);
                    finish();
                }
            }, 1000);
        }
    }
}
