package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.smartfit.MessageEvent.AllegeClassOver;
import com.smartfit.R;
import com.smartfit.adpters.GridViewPublishPhotoAdapter;
import com.smartfit.beans.MyAbsentClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyGridView;

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
 * 申述课程
 *
 * @author yanchengdeng
 *         create at 2016/8/26 15:49
 */
public class AllegeAbsentClassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.tv_coach_name)
    TextView tvCoachName;
    @Bind(R.id.tv_absent_time)
    TextView tvAbsentTime;
    @Bind(R.id.tv_absent_reason)
    TextView tvAbsentReason;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.gv_select_photos)
    MyGridView gvSelectPhotos;
    @Bind(R.id.iv_select_photos)
    ImageView ivSelectPhotos;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;


    private MyAbsentClass myAbsentClass;

    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 3;

    private List<String> urls = new ArrayList<>();

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allege_absent_class);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        initView();
        addLisener();
    }

    private void initView() {
        myAbsentClass = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        tvTittle.setText("旷课申请");
        if (!TextUtils.isEmpty(myAbsentClass.getCourseName())) {
            tvClassName.setText(String.format("课程：%s", myAbsentClass.getCourseName()));
        }

        if (!TextUtils.isEmpty(myAbsentClass.getCoachNickName())) {
            tvCoachName.setText(String.format("教练：%s", myAbsentClass.getCoachNickName()));
        }

        if (!TextUtils.isEmpty(myAbsentClass.getStartTime()) && !TextUtils.isEmpty(myAbsentClass.getEndTime())) {
            tvAbsentTime.setText(String.format("日期：%s - %s", new Object[]{DateUtils.getData(myAbsentClass.getStartTime(), "yyyy-MM-dd HH:mm"), DateUtils.getData(myAbsentClass.getEndTime(), "HH:mm")}));
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_submmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submmit:
                doFadeBack(etContent.getEditableText().toString());
                break;
        }
    }

    /**
     * 申述
     *
     * @param content
     */
    private void doFadeBack(String content) {
        if (TextUtils.isEmpty(content)) {
            mSVProgressHUD.showInfoWithStatus("请填写申述内容", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", myAbsentClass.getId());
        maps.put("reason", content);

        StringBuffer stringBuffer = new StringBuffer();
        if (urls.size() > 0) {
            for (int i = 0; i < urls.size(); i++) {
                if (urls.size() - 1 == i) {
                    stringBuffer.append(urls.get(i));
                } else {
                    stringBuffer.append(urls.get(i)).append("|");
                }
            }
        }

        if (!TextUtils.isEmpty(stringBuffer.toString())) {
            maps.put("imgUrl", stringBuffer.toString());
        }

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.USER_APPEALPUNISH, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("申述成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new AllegeClassOver(myAbsentClass.getId()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
        request.headers = NetUtil.getRequestBody(AllegeAbsentClassActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
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


    }

    /**
     * 进入相册ji
     */
    private void goPhotoThum() {
        Intent intent = new Intent(AllegeAbsentClassActivity.this, MultiImageSelectorActivity.class);
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
                    gvSelectPhotos.setAdapter(new GridViewPublishPhotoAdapter(AllegeAbsentClassActivity.this, mSelectPath));
                    for (String item : mSelectPath) {
                        getCardUrl(item);
                    }
                }
            }
        }
    }


    /**
     * 获取省份证图片路径
     *
     * @param cards
     */
    private void getCardUrl(String cards) {
        RequestParams params = new RequestParams(Constants.Net.URL + Constants.UPLOAD_PHOTOS);
        params.setMultipart(true);
        try {
            params.addBodyParameter("imageFile", new File(cards));
        } catch (Exception ex) {
        }
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {

                String url = null;
                try {
                    url = result.getString("data");
                    urls.add(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(url)) {
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
