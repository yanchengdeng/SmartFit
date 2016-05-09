package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.AddDynaicItem;
import com.smartfit.R;
import com.smartfit.adpters.GridViewPublishPhotoAdapter;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/***
 * 发布状态
 */
public class PublishCustomeDynamicActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_mood_content)
    EditText etMoodContent;
    @Bind(R.id.gv_select_photos)
    GridView gvSelectPhotos;
    @Bind(R.id.iv_select_photos)
    ImageView ivSelectPhotos;
    @Bind(R.id.iv_same_wx)
    ImageView ivSameWx;


    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;

    private List<String> urls = new ArrayList<>();

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_custome_dynamic);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        tvTittle.setText("动态");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        addLisener();
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if (TextUtils.isEmpty(etMoodContent.getEditableText().toString())) {
                                                  mSVProgressHUD.showInfoWithStatus("请填写心情内容哦~");
                                                  return;
                                              }

                                              StringBuffer stringBuffer = new StringBuffer();
                                              if (urls.size() > 0) {

                                                  for (String item : urls) {
                                                      stringBuffer.append(item);
                                                  }
                                              }

                                              addDynamic(etMoodContent.getEditableText().toString(), stringBuffer.toString());


                                          }
                                      }

        );

        ivSelectPhotos.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  goPhotoThum();
                                              }
                                          }

        );

        gvSelectPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                              {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                      if (null != mSelectPath && mSelectPath.size() > 0) {
                                                          goPhotoThum();
                                                      }
                                                  }
                                              }

        );
    }

    private void addDynamic(String content, String urlsContent) {
        mSVProgressHUD.showWithStatus(getString(R.string.uploading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("content", content);
        if (!TextUtils.isEmpty(urlsContent.toString())) {
            maps.put("imgUrl", urlsContent);
        }
        PostRequest request = new PostRequest(Constants.DYNAMIC_ADDDYNAMIC, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("上传成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new AddDynaicItem());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PublishCustomeDynamicActivity.this);
        mQueue.add(request);

    }


    /**
     * 进入相册ji
     */
    private void goPhotoThum() {
        Intent intent = new Intent(PublishCustomeDynamicActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
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
                if (null != mSelectPath && mSelectPath.size() > 0) {
                    gvSelectPhotos.setAdapter(new GridViewPublishPhotoAdapter(PublishCustomeDynamicActivity.this, mSelectPath));
                    urls.clear();
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
