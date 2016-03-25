package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.adpters.CityAdapter;
import com.smartfit.beans.CityBean;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 城市列表
 */
public class CityListActivity extends BaseActivity {

    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.listView)
    ListView listView;
    private List<String[]> datas = new ArrayList<String[]>();

    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
        initView();
        addLisener();
    }

    private void initView() {
        adapter = new CityAdapter(this, datas);
        listView.setAdapter(adapter);
        goSearch("sf");
    }


    private void addLisener() {

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSearchContent.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("未输入关键字", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                } else {
                    goSearch(etSearchContent.getEditableText().toString());
                }
            }
        });

    }

    private void goSearch(String key) {

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.GET_CITY_LIST, NetUtil.getRequestBody(data, mContext), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.register_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
                List<CityBean> cityBeans = JsonUtils.listFromJson(response.getAsJsonArray("list"), CityBean.class);
                if (cityBeans != null && cityBeans.size() > 0) {

                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        mQueue.add(request);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] data = new String[]{"北京", "湖北"};
                for (int i = 0; i < 10; i++) {
                    datas.add(data);
                }
                adapter.setData(datas);
                mSVProgressHUD.dismiss();
            }
        }, 2000);

    }


}
