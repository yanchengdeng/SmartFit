package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.VersionInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户协议
 */
public class UserDealActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_content)
    TextView tvContent;
    private String url;
    private String adName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deal);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(Constants.PASS_STRING);
        adName = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(adName)) {
            tvTittle.setText(adName);
        }

        getContent();
    }

    private void getContent() {

        Map<String, String> data = new HashMap<>();

        PostRequest request = new PostRequest(url, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                tvContent.setText(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.headers = NetUtil.getRequestBody(UserDealActivity.this);
        GetSingleRequestUtils.getInstance(UserDealActivity.this).getRequestQueue().add(request);
    }

}
