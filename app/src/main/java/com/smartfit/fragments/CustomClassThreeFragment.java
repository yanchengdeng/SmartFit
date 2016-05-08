package com.smartfit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.UserCustomClassFourActivity;
import com.smartfit.activities.UserCustomClassThreeActivity;
import com.smartfit.adpters.PrivateEducationAdapter;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 自定课程第三步
 */
public class CustomClassThreeFragment extends BaseFragment {


    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    ListView listView;
    private PrivateEducationAdapter adapter;
    private List<PrivateEducationClass> datas = new ArrayList<PrivateEducationClass>();

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean isLoaded = false;


    private String startTime, endTime, courseClassId, venueId,roomId, venuPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startTime = getArguments().getString("startTime");
            endTime = getArguments().getString("endTime");
            courseClassId = getArguments().getString("courseClassId");
            venueId = getArguments().getString("venueId");
            venuPrice = getArguments().getString("venuePrice");
            roomId = getArguments().getString("roomId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_three, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            startTime = getArguments().getString("startTime");
            endTime = getArguments().getString("endTime");
            courseClassId = getArguments().getString("courseClassId");
            venueId = getArguments().getString("venueId");
            venuPrice = getArguments().getString("venuePrice");
            roomId = getArguments().getString("roomId");
        }
        isPrepared = true;
        lazyLoad();
        initListView();
        return view;
    }

    private void getData() {
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("startTime", startTime);
        data.put("endTime", endTime);
        data.put("venueId", venueId);
        data.put("courseClassId", courseClassId);
        PostRequest request = new PostRequest(Constants.COACH_LISTIDLECOACHESBYVENUEIDANDCOURSETYPECODE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                isLoaded = true;
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                List<PrivateEducationClass> privateEducationClasses = JsonUtils.listFromJson(response.getAsJsonArray("list"), PrivateEducationClass.class);
                if (privateEducationClasses != null && privateEducationClasses.size() > 0) {
                    datas = privateEducationClasses;
                    adapter.setData(privateEducationClasses);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.INVISIBLE);


                } else {
                    listView.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                listView.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.VISIBLE);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {


        adapter = new PrivateEducationAdapter(getActivity(), datas);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());

            }
        });

        ((UserCustomClassThreeActivity) getActivity()).ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PrivateEducationClass> selectPricates = countSelectNum(datas);
                if (selectPricates.size() == 0) {
                    ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("请选择教练", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("已选择" + selectPricates.size() + "个");

                    Bundle bundle = new Bundle();
                    bundle.putString("startTime", startTime);
                    bundle.putString("endTime", endTime);
                    bundle.putString("courseClassId", courseClassId);
                    bundle.putString("venueId", venueId);
                    bundle.putString("venuePrice", venuPrice);
                    bundle.putString("roomId",roomId);
                    float price = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (PrivateEducationClass item : selectPricates) {
                        stringBuilder.append(item.getCoachId()).append("|");
                        if (!TextUtils.isEmpty(item.getPrice())) {
                            if (Float.parseFloat(item.getPrice()) > price) {
                                price = Float.parseFloat(item.getPrice());
                            }
                        }
                    }
                    bundle.putString("coachPrice", String.valueOf(price));
                    bundle.putString("coachId",stringBuilder.toString());
                    ((BaseActivity) getActivity()).openActivity(UserCustomClassFourActivity.class, bundle);
                }
            }
        });


    }

    /**
     * 计算已经选择教练人数
     *
     * @param datas
     */
    private ArrayList<PrivateEducationClass> countSelectNum(List<PrivateEducationClass> datas) {
        ArrayList<PrivateEducationClass> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        getData();
    }
}
