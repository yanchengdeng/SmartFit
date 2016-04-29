package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateDynamic;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论动态
 *
 * @author yanchengdeng
 *         create at 2016/4/29 14:18
 */
public class DynamicMakeDiscussActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_content)
    EditText etContent;


    private String topicId;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_make_discuss);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        topicId = getIntent().getStringExtra(Constants.PASS_STRING);
        tvTittle.setText("动态评论");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
    }

    @OnClick({R.id.iv_back, R.id.iv_function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_function:
                doComment(etContent.getEditableText().toString());
                break;
        }
    }

    private void doComment(String conment) {
        mSVProgressHUD.showWithStatus(getString(R.string.submit_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("topicId", topicId);
//        data.put("stars", String.valueOf(rating));
        data.put("commentContent", conment);
        PostRequest request = new PostRequest(Constants.COMMENT_SAVE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已评论", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new UpdateDynamic());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(getString(R.string.try_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(DynamicMakeDiscussActivity.this);
        mQueue.add(request);

    }
}
