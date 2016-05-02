package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.smartfit.adpters.GridViewPublishPhotoAdapter;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 意见反馈
 */
public class FadeBackActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.gv_select_photos)
    MyGridView gvSelectPhotos;
    @Bind(R.id.iv_select_photos)
    ImageView ivSelectPhotos;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    @Bind(R.id.et_content)
    EditText etContent;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade_back);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.suggest_fadeback));

    }

    private void addLisener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //添加图片
        ivSelectPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum();
            }
        });


        gvSelectPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != mSelectPath && mSelectPath.size() > 0) {
                    goPhotoThum();
                }
            }
        });

        //提交
        btnSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFadeBack(etContent.getEditableText().toString());

            }
        });

    }

    /**
     * 反馈
     * @param content
     */
    private void doFadeBack(String content) {
        if (TextUtils.isEmpty(content)) {
            mSVProgressHUD.showInfoWithStatus("请填写反馈内容", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        Map<String,String> maps = new HashMap<>();
        maps.put("feedBackContent",content);

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.SYS_SAVEFEEDBACK, maps,new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("反馈成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(FadeBackActivity.this);
        mQueue.add(request);
    }

    /**
     * 进入相册ji
     */
    private void goPhotoThum() {
        Intent intent = new Intent(FadeBackActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                LogUtil.d("dyc", mSelectPath.toString());
                if (null != mSelectPath && mSelectPath.size() > 0) {
                    gvSelectPhotos.setAdapter(new GridViewPublishPhotoAdapter(FadeBackActivity.this, mSelectPath));
                }
            }
        }
    }


}
