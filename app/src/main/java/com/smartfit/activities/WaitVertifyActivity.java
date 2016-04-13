package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.adpters.MoreCertiaicateVertifyAdapter;
import com.smartfit.beans.SubmitAuthInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author dengyancheng
 *         等待审核
 */
public class WaitVertifyActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_vertify);
        ButterKnife.bind(this);
        initView();
        addLisener();

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
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WaitVertifyActivity.this);
        mQueue.add(request);
    }

    /**
     * 填充资料信息
     *
     * @param submitAuthInfoList
     */
    private void fillData(List<SubmitAuthInfo> submitAuthInfoList) {

        List<SubmitAuthInfo>  workAuths = new ArrayList<>();
        for (SubmitAuthInfo item : submitAuthInfoList) {
            if (item.getType().equals("1")) {
                //身份证
                tvCard.setText(item.getCertificateName());
                tvName.setText(item.getCoachRealName());
                ImageLoader.getInstance().displayImage(item.getCertificateImg(), imageCard, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                } else if (item.getStatus().equals("2")) {
                    cbCardPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbCardPhoto.setImageResource(R.mipmap.icon_close);
                }
            }

            if (item.getType().equals("2")) {
                //正式照片
                ImageLoader.getInstance().displayImage(item.getCertificateImg(), imageWork, Options.getListOptions());
                //1待审；2通过；3不通过
                if (item.getStatus().equals("1")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);
                } else if (item.getStatus().equals("2")) {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_choose);
                } else {
                    cbWorkPhoto.setImageResource(R.mipmap.icon_close);

                }
            }

            if (item.getType().equals("3")){
                workAuths.add(item);
            }
        }

        if (workAuths.size()>0){
            listAuthPhotos.setAdapter(new MoreCertiaicateVertifyAdapter(WaitVertifyActivity.this,workAuths));
        }
    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
