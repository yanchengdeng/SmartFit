package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.CustomClassGridViewItem;
import com.smartfit.adpters.TeachClassAdapter;
import com.smartfit.beans.AttentionBean;
import com.smartfit.beans.ClassFication;
import com.smartfit.beans.CoachCourseType;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 授课项目列表
 *
 * @author yanchengdeng
 *         create at 2016/5/9 16:33
 */
public class TeachClassListActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.no_data)
    TextView noData;
    List<ClassFication> datas = new ArrayList<>();


    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_class_list);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        initView();
        getData();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.teach_classes));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                for (ClassFication item : datas) {
                    if (item.isCheck()) {
                        sb.append(item.getId()).append("|");
                    }
                }
                LogUtil.w("dyc", sb.toString());
                if (TextUtils.isEmpty(sb.toString())) {
                    mSVProgressHUD.showInfoWithStatus("未选择任何课程", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                } else {
                    saveCoachCourseType(sb.toString());
                }
            }
        });
    }

    /**
     * 保存 教练课程
     *
     * @param courseTypes
     */
    private void saveCoachCourseType(String courseTypes) {
        mSVProgressHUD.showWithStatus(getString(R.string.save_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("courseTypesString", courseTypes);
        PostRequest request = new PostRequest(Constants.USER_SAVECOACHCOURSETYPES, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已完成", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post("couserTypes");
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
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(TeachClassListActivity.this);
        mQueue.add(request);


    }

    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        getSaveCours();
    }

    private void getSaveCours() {
        final PostRequest request = new PostRequest(Constants.USER_GETCOACHCOURSETYPES, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                LogUtil.w("dyc", response.get("list").toString());
                String[] tyes = response.get("list").toString().replace("[","").replace("]","").split("[,]");
                getAllCoursType(tyes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(TeachClassListActivity.this);
        mQueue.add(request);
    }

    private void getAllCoursType(final String[] classTypes) {
        PostRequest request = new PostRequest(Constants.CLASSIF_GETCLASSIFICATION, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<ClassFication> classFicationList = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassFication.class);
                if (classFicationList != null && classFicationList.size() > 0) {
                    datas = classFicationList;
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    if (classTypes != null && classTypes.length > 0) {
                        for (ClassFication item : classFicationList) {
                            for (String type : classTypes) {
                                if (item.getId().equals(type)) {
                                    item.setIsCheck(true);
                                }
                            }
                        }
                    }
                    listView.setAdapter(new TeachClassAdapter(TeachClassListActivity.this, classFicationList));
                } else {
                    listView.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(TeachClassListActivity.this);
        mQueue.add(request);
    }

    @OnClick({R.id.iv_back, R.id.iv_function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_function:
                break;
        }
    }
}
