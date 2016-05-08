package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.InviteFriendsAdapter;
import com.smartfit.beans.AttentionBean;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 邀请好友
 */
public class InviteFriendsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_invite_wx_friends)
    TextView tvInviteWxFriends;
    @Bind(R.id.tv_health_friends_tips)
    TextView tvHealthFriendsTips;
    @Bind(R.id.ll_health_friends)
    MyListView llHealthFriends;
    @Bind(R.id.fans_tips)
    TextView fansTips;
    @Bind(R.id.ll_fans)
    MyListView llFans;
    @Bind(R.id.no_data)
    TextView noData;

    private boolean friendsOK, fansOk;
    private String couserID;

    private List<AttentionBean> friendsData = new ArrayList<>();
    private List<AttentionBean> fansData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        initView();
        initData();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.invite_friends));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        couserID = getIntent().getStringExtra(Constants.PASS_STRING);

    }


    private void initData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.USER_FRIENDLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    llHealthFriends.setAdapter(new InviteFriendsAdapter(InviteFriendsActivity.this, beans));
                    friendsOK = true;
                    friendsData = beans;
                } else {
                    tvHealthFriendsTips.setVisibility(View.GONE);
                    friendsOK = false;
                }
                getFans();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
                friendsOK = false;
                getFans();
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(InviteFriendsActivity.this);
        mQueue.add(request);
    }

    private void getFans() {
        Map<String, String> data = new HashMap<>();
        data.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        data.put("nickname", "");
        PostRequest request = new PostRequest(Constants.USER_CONCERNLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    llFans.setAdapter(new InviteFriendsAdapter(InviteFriendsActivity.this, beans));
                    fansOk = true;
                    fansData = beans;
                } else {
                    fansTips.setVisibility(View.GONE);
                    fansOk = false;
                }
                showNodata();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
                fansOk = false;
                showNodata();
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(InviteFriendsActivity.this);
        mQueue.add(request);
    }

    private void showNodata() {
        if (!friendsOK && !fansOk) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }

    }

    private void addLisener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        llHealthFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                friendsData.get(position).setIsCheck(checkBox.isChecked());
                List<AttentionBean> selectPricates = countSelectNum(friendsData);
            }
        });


        llFans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                fansData.get(position).setIsCheck(checkBox.isChecked());
                List<AttentionBean> selectPricates = countSelectNum(fansData);
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<AttentionBean> selectBeans = countSelectNum(friendsData);
                selectBeans.addAll(countSelectNum(fansData));

                StringBuffer sb = new StringBuffer();
                if (selectBeans.size() > 0) {
                    for (AttentionBean item : selectBeans) {
                        sb.append(item.getUid()).append("|");
                    }
                    doInviteFriends(sb.toString());
                } else {
                    mSVProgressHUD.showInfoWithStatus("您还未选择", SVProgressHUD.SVProgressHUDMaskType.Clear);

                }
            }
        });

    }

    private void doInviteFriends(String uidstring) {

        Map<String, String> data = new HashMap<>();
        data.put("courseId", couserID);
        data.put("uidstring", uidstring);
        PostRequest request = new PostRequest(Constants.CLASSIF_INVITEFRIENDS, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("邀请已发出", SVProgressHUD.SVProgressHUDMaskType.Clear);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());

            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(InviteFriendsActivity.this);
        mQueue.add(request);
    }

    /**
     * 计算已经选择教练人数
     *
     * @param datas
     */
    private ArrayList<AttentionBean> countSelectNum(List<AttentionBean> datas) {
        ArrayList<AttentionBean> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }
}
