package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.hyphenate.util.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.UpdateDynamic;
import com.smartfit.R;
import com.smartfit.adpters.DiscussItemAdapter;
import com.smartfit.beans.ClassCommend;
import com.smartfit.beans.ClassCommendList;
import com.smartfit.beans.DynamicInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;
import com.smartfit.views.SelectableRoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 动态详情
 */
public class DynamicDetailActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    
    private DynamicInfo dynamicInfo;

    SelectableRoundedImageView ivIcon;
    TextView tvName;
    TextView tvDynamicTittle;
    ImageView ivPhoto;
    TextView tvDynamicDate;
    TextView tvMessage;
    TextView tvPraise;
    private EventBus eventBus;

    private LinearLayout.LayoutParams params;

    private List<ClassCommend> datas = new ArrayList<>();

    private DiscussItemAdapter adapter;

    private boolean isLoadMore = false;

    private  int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    @Subscribe
    public void onEvent(UpdateDynamic event) {

        dynamicInfo.setCommentCount(String.valueOf(Integer.parseInt(dynamicInfo.getCommentCount())+1));
        tvMessage.setText(dynamicInfo.getCommentCount());
        isLoadMore = false;
        datas.clear();
        loadCommend();
    /* Do something */
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLoadMore)
                loadCommend();
            }
        });


        tvPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportGood(dynamicInfo.getId());
            }
        });

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, dynamicInfo.getTopicId());
                openActivity(DynamicMakeDiscussActivity.class,bundle);
            }
        });
    }

    /***
     * 点赞
     * @param id
     */
    private void supportGood(String id) {
        Map<String, String> maps = new HashMap<>();
        maps.put("dynamicId", id);
        PostRequest request = new PostRequest(Constants.DYNAMIC_GOOD, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                dynamicInfo.setGood(String.valueOf(Integer.parseInt(dynamicInfo.getGood()) + 1));
                tvPraise.setText(dynamicInfo.getGood());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(DynamicDetailActivity.this);
        mQueue.add(request);
    }

    private void initView() {
        params = new LinearLayout.LayoutParams(DeviceUtil.getWidth(this) / 2, DeviceUtil.getWidth(this) / 2);
        params.gravity = Gravity.LEFT;
        params.leftMargin = DensityUtil.dip2px(this, 16);
        dynamicInfo = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        tvTittle.setText("动态详情");
        View header  = LayoutInflater.from(this).inflate(R.layout.adapter_dynamic_view, null);
        tvName = (TextView) header.findViewById(R.id.tv_name);
        ivIcon = (SelectableRoundedImageView) header.findViewById(R.id.iv_icon);
        tvDynamicTittle = (TextView) header.findViewById(R.id.tv_dynamic_tittle);
        ivPhoto = (ImageView) header.findViewById(R.id.iv_photo);
        tvMessage = (TextView) header.findViewById(R.id.tv_message);
        tvPraise = (TextView)header.findViewById(R.id.tv_praise);
        tvDynamicDate = (TextView) header.findViewById(R.id.tv_dynamic_date);
        if (dynamicInfo != null){
            if (!TextUtils.isEmpty(dynamicInfo.getNickName())) {
                tvName.setText(dynamicInfo.getNickName());
            }

            ivPhoto.setLayoutParams(params);
            if (TextUtils.isEmpty(dynamicInfo.getImgUrl())) {
                ivPhoto.setVisibility(View.GONE);
            } else {
                ImageLoader.getInstance().displayImage(dynamicInfo.getImgUrl(), ivPhoto, Options.getListOptions());
                ivPhoto.setVisibility(View.VISIBLE);
            }
            ImageLoader.getInstance().displayImage(dynamicInfo.getUserPicUrl(), ivIcon, Options.getListOptions());

            if (!TextUtils.isEmpty(dynamicInfo.getContent())) {
                tvDynamicTittle.setText(dynamicInfo.getContent());
            }
            if (!TextUtils.isEmpty(dynamicInfo.getCommentCount())) {
                tvMessage.setText(dynamicInfo.getCommentCount());
            }

            if (!TextUtils.isEmpty(dynamicInfo.getGood())) {
                tvPraise.setText(dynamicInfo.getGood());
            }

            if (!TextUtils.isEmpty(dynamicInfo.getAddTime())) {
                tvDynamicDate.setText(DateUtils.getData(dynamicInfo.getAddTime()));
            }

        }

        adapter = new DiscussItemAdapter(datas,this);
        listView.setAdapter(adapter);
        listView.addHeaderView(header);

        loadCommend();
        
    }

    private void loadCommend() {

        if (page == 1)
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        final Map<String, String> data = new HashMap<>();
        data.put("topicId", dynamicInfo.getTopicId());
        data.put("pageNO", String.valueOf(page));
        data.put("pageSize", "100");
        PostRequest request = new PostRequest(Constants.CLASS_COMMEND, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                listView.onLoadMoreComplete();
                ClassCommendList classCommendList = JsonUtils.objectFromJson(response, ClassCommendList.class);
                if (null != classCommendList && classCommendList.getListData().size() > 0) {
                    datas.addAll(classCommendList.getListData());
                    listView.setVisibility(View.VISIBLE);
                    adapter.setData(datas);
                    if (classCommendList.getListData().size()==100){
                        page++;
                    }else{
                        listView.onLoadMoreComplete();
                        isLoadMore = true;

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                listView.onLoadMoreComplete();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(DynamicDetailActivity.this);
        mQueue.add(request);
    }

}
