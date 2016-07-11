package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.ShareFriendsTicketAdapter;
import com.smartfit.beans.ShareFriendsInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author dengyancheng
 *         分享券---选择好友
 */
public class SelectShareFriendsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.ll_fans)
    ListView llFans;
    @Bind(R.id.no_data)
    TextView noData;
    List<ShareFriendsInfo> shareFriendsInfos = new ArrayList<>();

    private ShareFriendsTicketAdapter adapter;

    private int selectPosion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_share_friends);
        ButterKnife.bind(this);
        initView();
        getData();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("选择分享好友");
        tvFunction.setText("下一步");
        adapter = new ShareFriendsTicketAdapter(this, shareFriendsInfos);
        llFans.setAdapter(adapter);

    }


    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.USER_SHAREFRIENDLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                shareFriendsInfos = JsonUtils.listFromJson(response.getAsJsonArray("list"), ShareFriendsInfo.class);
                if (shareFriendsInfos != null && shareFriendsInfos.size() > 0) {
                    tvFunction.setVisibility(View.VISIBLE);
                    adapter.setData(shareFriendsInfos);
                    llFans.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    llFans.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
                llFans.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(SelectShareFriendsActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llFans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (ShareFriendsInfo item : shareFriendsInfos) {
                    item.setIsCheck(false);
                }
                selectPosion = position;
                shareFriendsInfos.get(position).setIsCheck(true);
                adapter.notifyDataSetChanged();
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosion > -1) {
                    Intent intent = new Intent(SelectShareFriendsActivity.this,WXEntryActivity.class);
                    intent.putExtra("uid",shareFriendsInfos.get(selectPosion).getUid());
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    mSVProgressHUD.showInfoWithStatus("未选中好友", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });

    }


}
