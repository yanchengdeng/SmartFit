package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.adpters.MoreCertiaicateAdapter;
import com.smartfit.beans.Certificate;
import com.smartfit.beans.CoachCertificate;
import com.smartfit.beans.CoachCertificateItem;
import com.smartfit.beans.SubmitAuthInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;

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
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 审核未通过
 */
public class VertifyNotPassActivity extends BaseActivity {


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
    @Bind(R.id.iv_ids_photos)
    ImageView ivIdsPhotos;
    @Bind(R.id.cb_card_photo)
    ImageView cbCardPhoto;
    @Bind(R.id.tv_work_quality)
    TextView tvWorkQuality;
    @Bind(R.id.iv_work_photos)
    ImageView ivWorkPhotos;
    @Bind(R.id.cb_work_photo)
    ImageView cbWorkPhoto;
    @Bind(R.id.list_auth_photos)
    MyListView listAuthPhotos;
    @Bind(R.id.tv_add_more_books)
    TextView tvAddMoreBooks;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    @Bind(R.id.ll_ui_root)
    LinearLayout llUiRoot;
    //上传身份证
    private ArrayList<String> cards = new ArrayList<>();
    //上传工作证
    private ArrayList<String> works = new ArrayList<>();

    //更多工作证书
    private List<Certificate> certificateList = new ArrayList<>();

    private static int REQUSET_ID_CARDS = 0x011;
    private static int REUQUST_WORK_AUTH = 0X012;
    private static int REQUEST_CERTIFICATE_MORE = 0x013;
    MoreCertiaicateAdapter moreCertiaicateAdapter;

    private CoachCertificate coachCertificate;

    private int positon = -1;


    private int countRequset = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof String) {
                Certificate item = certificateList.get(msg.what);
                if (item != null) {
                    certificateList.get(msg.what).setName((String) msg.obj);
                }
            } else {
                UpdateCertificate(msg.what, (ArrayList<String>) msg.obj);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_not_pass);
        ButterKnife.bind(this);
        coachCertificate = new CoachCertificate();
        initView();
        initMoreCentifacate();
        addLisener();

    }


    private void initMoreCentifacate() {

        certificateList.add(getNewCertificate());
        moreCertiaicateAdapter = new MoreCertiaicateAdapter(this, certificateList, handler);
        listAuthPhotos.setAdapter(moreCertiaicateAdapter);

    }

    private void initView() {
        tvTittle.setText(getString(R.string.add_vertify_info));
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
        request.headers = NetUtil.getRequestBody(VertifyNotPassActivity.this);
        mQueue.add(request);
    }

    private void fillData(List<SubmitAuthInfo> submitAuthInfoList) {
        List<SubmitAuthInfo> workAuths = new ArrayList<>();
        for (SubmitAuthInfo item : submitAuthInfoList) {
            if (item.getType().equals("1")) {
                //身份证
                tvCard.setText(item.getCertificateName());
                tvName.setText(item.getCoachRealName());

                ImageLoader.getInstance().displayImage(item.getCertificateImg(), ivIdsPhotos, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                    cbName.setImageResource(R.mipmap.icon_close);
                    cbCard.setImageResource(R.mipmap.icon_close);
                } else if (item.getStatus().equals("2")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                    cbName.setImageResource(R.mipmap.icon_choose);
                    cbCard.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                    cbName.setImageResource(R.mipmap.icon_close);
                    cbCard.setImageResource(R.mipmap.icon_close);
                }
            }

            if (item.getType().equals("2")) {
                //正式照片
                ImageLoader.getInstance().displayImage(item.getCertificateImg(), ivWorkPhotos, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);
                } else if (item.getStatus().equals("2")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);
                }
            }

            if (item.getType().equals("3")) {
                workAuths.add(item);
            }
        }

        if (workAuths.size() > 0) {
            for (int i = 0; i < workAuths.size(); i++) {
                Certificate certificate = new Certificate();
                certificate.setPhoto(workAuths.get(i).getCertificateImg());
                certificate.setName(workAuths.get(i).getCertificateName());
                certificate.setStatus(workAuths.get(i).getStatus());
                certificate.setText_tips("获得的证书" + (certificateList.size() + 1));
                certificate.setImage_tips("上传证书照片" + (certificateList.size() + 1));
                certificateList.add(certificate);
            }
            moreCertiaicateAdapter.setData(certificateList);
        }
    }


    private void UpdateCertificate(int position, ArrayList<String> mSelectPath) {
        Certificate certificate = certificateList.get(position);
        positon = position;
        if (certificate != null) {
            goPhotoThum(mSelectPath, REQUEST_CERTIFICATE_MORE, 1);
        }
    }

    /**
     * 进入相册ji
     *
     * @param mSelectPath
     * @param requestCode
     */
    private void goPhotoThum(ArrayList<String> mSelectPath, int requestCode, int photos) {
        Intent intent = new Intent(VertifyNotPassActivity.this, MultiImageSelectorActivity.class);
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
        if (requestCode == REQUSET_ID_CARDS) {
            if (resultCode == RESULT_OK) {
                cards = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (null != cards && cards.size() > 0) {

                    getCardUrl(cards);
                } else {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                }
            }
        } else if (requestCode == REUQUST_WORK_AUTH) {
            if (resultCode == RESULT_OK) {
                works = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (null != works && works.size() > 0) {

                    getWorkUrl(works);
                } else {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);
                }
            }
        } else if (requestCode == REQUEST_CERTIFICATE_MORE) {
            ArrayList<String> photos = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (null != photos && photos.size() > 0) {
                getAuthUrl(photos.get(0));
            }
        }
    }

    /**
     * 获取该证书的图片url
     *
     * @param certificateList
     */
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


    /**
     * 获取工作图片路径
     *
     * @param works
     */
    private void getWorkUrl(ArrayList<String> works) {
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
        params.setMultipart(true);
        try {
            for (String item : works) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtil.w("dyc", "工作证" + result + "..." + url);
                if (!TextUtils.isEmpty(url)) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                    ImageLoader.getInstance().displayImage(url, ivWorkPhotos, Options.getListOptions());
                    coachCertificate.setCoachCertificateWord(new CoachCertificateItem(tvCard.getEditableText().toString(), url, "2"));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
     * 获取省份证图片路径
     *
     * @param cards
     */
    private void getCardUrl(ArrayList<String> cards) {
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
        params.setMultipart(true);
        try {
            for (String item : cards) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(url)) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                    ImageLoader.getInstance().displayImage(url, ivIdsPhotos, Options.getListOptions());
                    coachCertificate.setCoachCertificateCard(new CoachCertificateItem(tvCard.getEditableText().toString(), url, "1"));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private Certificate getNewCertificate() {
        Certificate certificate = new Certificate();
        certificate.setText_tips("获得的证书" + (certificateList.size() + 1));
        certificate.setImage_tips("上传证书照片" + (certificateList.size() + 1));
        return certificate;
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //姓名
        tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    cbName.setImageResource(R.mipmap.icon_close);
                } else {
                    cbName.setImageResource(R.mipmap.icon_choose);
                }
            }
        });


        //身份证
        tvCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    cbCard.setImageResource(R.mipmap.icon_close);
                } else {
                    cbCard.setImageResource(R.mipmap.icon_choose);
                }
            }
        });

        ivIdsPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum(cards, REQUSET_ID_CARDS, 1);
            }
        });


        ivWorkPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum(works, REUQUST_WORK_AUTH, 1);
            }
        });

        //添加更多证书
        tvAddMoreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificateList.add(getNewCertificate());
                moreCertiaicateAdapter.notifyDataSetChanged();
            }
        });

        btnSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvName.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("请输入姓名", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    if (TextUtils.isEmpty(tvCard.getEditableText().toString())) {
                        mSVProgressHUD.showInfoWithStatus("请输入身份证号", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    } else {

                        mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.Clear);
                        if (null != coachCertificate.getCoachCertificateCard()) {
                            countRequset++;
                            doSubmit(tvName.getEditableText().toString(), tvCard.getEditableText().toString(), coachCertificate.getCoachCertificateCard());
                        }

                        if (null != coachCertificate.getCoachCertificateWord()) {
                            countRequset++;
                            doSubmit(tvName.getEditableText().toString(), tvCard.getEditableText().toString(), coachCertificate.getCoachCertificateWord());
                        }

                        List<Certificate> subcertificateList = new ArrayList<Certificate>();
                        for (Certificate item : certificateList) {
                            if (!TextUtils.isEmpty(item.getPhoto()) && !TextUtils.isEmpty(item.getName())) {
                                subcertificateList.add(item);
                            }
                        }

                        if (subcertificateList.size() > 0) {
                            countRequset = countRequset + subcertificateList.size();
                            for (Certificate item : subcertificateList) {
                                CoachCertificateItem coachCertificateItem = new CoachCertificateItem(item.getName(), item.getPhoto(), "3");
                                doSubmit(tvName.getEditableText().toString(), coachCertificateItem.getCertificateName(), coachCertificateItem);
                            }
                        }

                    }
                }
            }
        });

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
                mSVProgressHUD.dismiss();
                countRequset--;
                updateSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countRequset--;
                updateSuccess();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(VertifyNotPassActivity.this);
        mQueue.add(request);
    }

    private void updateSuccess() {
        if (countRequset == 0) {
            mSVProgressHUD.showSuccessWithStatus("上传成功", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
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
