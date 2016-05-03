package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.CustomClassGridViewItem;
import com.smartfit.beans.ClassFication;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 教练发布课程第一步
 *
 * @author yanchengdeng
 *         create at 2016/5/3 11:52
 */
public class UserCoachPublishClassOneActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.gv_class)
    GridView gvClass;
    @Bind(R.id.no_data)
    TextView noData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coach_publish_class_one);
        ButterKnife.bind(this);
        initView();
        getData();
        addLisener();

    }

    private void initView() {
        tvTittle.setText("自定课程1/3");
    }


    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        HashMap<String, String> maps = new HashMap<>();
        maps.put("classType", "1");
        PostRequest request = new PostRequest(Constants.CLASSIF_GETCLASSIFICATION, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<ClassFication> classFicationList = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassFication.class);
                if (classFicationList != null && classFicationList.size() > 0) {
                    gvClass.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    gvClass.setAdapter(new CustomClassGridViewItem(UserCoachPublishClassOneActivity.this, classFicationList,true));


                } else {
                    gvClass.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(UserCoachPublishClassOneActivity.this);
        mQueue.add(request);

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
