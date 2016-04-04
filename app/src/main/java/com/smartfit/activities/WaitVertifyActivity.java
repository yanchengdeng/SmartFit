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
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

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


    private void addLisener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
