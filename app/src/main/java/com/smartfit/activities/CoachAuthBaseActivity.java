package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.GridViewAuthAdapter;
import com.smartfit.adpters.MoreCertiaicateAdapter;
import com.smartfit.beans.Certificate;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyGridView;
import com.smartfit.views.MyListView;

import org.apache.http.params.HttpProtocolParams;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 教练资格认证   基本信息认真
 */
public class CoachAuthBaseActivity extends BaseActivity {


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
    @Bind(R.id.gv_ids_photos)
    MyGridView gvIdsPhotos;
    @Bind(R.id.iv_add_card)
    ImageView ivAddCard;
    @Bind(R.id.cb_card_photo)
    ImageView cbCardPhoto;
    @Bind(R.id.tv_work_quality)
    TextView tvWorkQuality;
    @Bind(R.id.gv_work_photos)
    MyGridView gvWorkPhotos;
    @Bind(R.id.iv_work_photo)
    ImageView ivWorkPhoto;
    @Bind(R.id.cb_work_photo)
    ImageView cbWorkPhoto;
    @Bind(R.id.list_auth_photos)
    MyListView listAuthPhotos;
    @Bind(R.id.tv_add_more_books)
    TextView tvAddMoreBooks;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    //上传身份证
    private ArrayList<String> cards = new ArrayList<>();
    //上传工作证
    private ArrayList<String> works = new ArrayList<>();

    //更多工作证书
    private List<Certificate> certificateList = new ArrayList<>();

    private static int REQUSET_ID_CARDS = 0x011;
    private static int REUQUST_WORK_AUTH = 0X012;
    private static int REQUEST_CERTIFICATE_MORE = 0x013;

    private int positon = -1;

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

    private void UpdateCertificate(int position, ArrayList<String> mSelectPath) {
        Certificate certificate = certificateList.get(position);
        positon = position;
        if (certificate != null) {
            goPhotoThum(mSelectPath, REQUEST_CERTIFICATE_MORE, 1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_auth_base);
        ButterKnife.bind(this);
        initView();
        initMoreCentifacate();
        addLisener();
    }

    private void initMoreCentifacate() {

        certificateList.add(getNewCertificate());
        listAuthPhotos.setAdapter(new MoreCertiaicateAdapter(this, certificateList, handler));

    }

    private Certificate getNewCertificate() {
        Certificate certificate = new Certificate();
        certificate.setText_tips("获得的证书" + (certificateList.size() + 1));
        certificate.setImage_tips("上传证书照片" + (certificateList.size() + 1));
        return certificate;
    }

    private void initView() {
        tvTittle.setText(getString(R.string.coach_auth));

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

        ivAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum(cards, REQUSET_ID_CARDS, 2);
            }
        });

        gvIdsPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cards != null && cards.size() > 0) {
                    goPhotoThum(cards, REQUSET_ID_CARDS, 2);
                }
            }
        });


        ivWorkPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum(works, REUQUST_WORK_AUTH, 1);
            }
        });

        gvWorkPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (works != null && works.size() > 0) {
                    goPhotoThum(works, REUQUST_WORK_AUTH, 1);
                }
            }
        });

        //添加更多证书
        tvAddMoreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificateList.add(getNewCertificate());
                ((MoreCertiaicateAdapter) listAuthPhotos.getAdapter()).notifyDataSetChanged();
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
                        if (cards.size() == 0) {
                            mSVProgressHUD.showInfoWithStatus("请添加身份证照片", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        } else {
                            if (works.size() == 0) {
                                mSVProgressHUD.showInfoWithStatus("请添加正式照", SVProgressHUD.SVProgressHUDMaskType.Clear);
                            } else {
                                doSubmit(tvName.getEditableText().toString(), tvCard.getEditableText().toString(), cards, works);
                            }
                        }
                    }
                }
            }
        });
    }

    private void doSubmit(String name, String card, ArrayList<String> cards, ArrayList<String> works) {
        mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
//        params.addBodyParameter("CoachId", "8");
//        params.addBodyParameter("CoachRealName", name);
//        params.addBodyParameter("IdentityCode", card);
        // 使用multipart表单上传文件
        params.setMultipart(true);
        try {
            for (String item : cards) {
                params.addBodyParameter("imageFile", new File(item));
            }
            for (String item : works) {
                params.addBodyParameter("imageFile", new File(item));
            }
        } catch (Exception ex) {
            LogUtil.w("dyc",ex.getMessage());
        }
        LogUtil.w("dyc",params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.w("dyc",result);
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.subimit_success, SVProgressHUD.SVProgressHUDMaskType.Clear));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(ex.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
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
     * 进入相册ji
     *
     * @param mSelectPath
     * @param requestCode
     */
    private void goPhotoThum(ArrayList<String> mSelectPath, int requestCode, int photos) {
        Intent intent = new Intent(CoachAuthBaseActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, photos);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
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
                    gvIdsPhotos.setAdapter(new GridViewAuthAdapter(CoachAuthBaseActivity.this, cards));
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                }
            }
        } else if (requestCode == REUQUST_WORK_AUTH) {
            if (resultCode == RESULT_OK) {
                works = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (null != works && works.size() > 0) {
                    gvWorkPhotos.setAdapter(new GridViewAuthAdapter(CoachAuthBaseActivity.this, works));
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);
                }
            }
        } else if (requestCode == REQUEST_CERTIFICATE_MORE) {
            ArrayList<String> photos = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (null != photos && photos.size() > 0) {
                certificateList.get(positon).setPhotos(photos);
                ((MoreCertiaicateAdapter) listAuthPhotos.getAdapter()).notifyDataSetChanged();
            }
        }
    }
}
