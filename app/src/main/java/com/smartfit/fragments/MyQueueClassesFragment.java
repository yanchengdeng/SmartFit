package com.smartfit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.ArerobicDetailActivity;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.GroupClassDetailActivity;
import com.smartfit.activities.PrivateClassByMessageActivity;
import com.smartfit.adpters.MyClassOrderStatusAdapter;
import com.smartfit.adpters.MyRankClassStatusAdapter;
import com.smartfit.beans.MyAddClass;
import com.smartfit.beans.MyAddClassList;
import com.smartfit.beans.MyRankClass;
import com.smartfit.beans.MyRankClassList;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我排队的课程
 */
public class MyQueueClassesFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.no_data)
    TextView noData;

    private MyRankClassStatusAdapter adapter;
    private List<MyRankClass> myAddClassArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_add_classes, null);
        ButterKnife.bind(this, view);
        intData();
        return view;
    }

    private void intData() {
        adapter = new MyRankClassStatusAdapter(getActivity(), myAddClassArrayList);
        listView.setAdapter(adapter);
        loadData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                openClass(myAddClassArrayList.get(position));
            }
        });
    }

    /**
     * 跳转叨叨详情页
     *
     * @param item
     */
    private void openClass(MyRankClass item) {
        if (!TextUtils.isEmpty(item.getCourseType())) {
            //0  团操 1  小班   2  私教  3  有氧
            if (item.getCourseType().equals("0")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                bundle.putString(Constants.COURSE_TYPE, "0");
                ((BaseActivity) getActivity()).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getCourseType().equals("1")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                bundle.putString(Constants.COURSE_TYPE, "1");
                ((BaseActivity) getActivity()).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getCourseType().equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                ((BaseActivity) getActivity()).openActivity(PrivateClassByMessageActivity.class, bundle);

            } else if (item.getCourseType().equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                ((BaseActivity) getActivity()).openActivity(ArerobicDetailActivity.class, bundle);
            }
        }

    }

    private void loadData() {

        final Map<String, String> datas = new HashMap<>();
        datas.put("pageNo", "1");
        datas.put("pageSize", "100");
        PostRequest request = new PostRequest(Constants.USER_MYBOOKLIST, datas, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                MyRankClassList subClasses = JsonUtils.objectFromJson(response, MyRankClassList.class);
                if (subClasses != null && subClasses.getListData().size() > 0) {
                    myAddClassArrayList.addAll(subClasses.getListData());
                    adapter.setData(subClasses.getListData());
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
