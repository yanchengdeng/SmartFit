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
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.GroupClassDetailActivity;
import com.smartfit.activities.PrivateClassByMessageActivity;
import com.smartfit.adpters.MyClassOrderStatusAdapter;
import com.smartfit.beans.MesageInfo;
import com.smartfit.beans.MyAddClass;
import com.smartfit.beans.MyAddClassList;
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
 * Created by dengyancheng on 16/3/12.
 * <p/>
 * 我结束的课程
 */
public class MyClassesOverFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.no_data)
    TextView noData;

    private MyClassOrderStatusAdapter adapter;
    private List<MyAddClass> datas = new ArrayList<>();

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
        adapter = new MyClassOrderStatusAdapter(getActivity(), datas, false);
        listView.setAdapter(adapter);
        loadData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                openClass(datas.get(position));
            }
        });


    }

    /**
     * 跳转叨叨详情页
     *
     * @param item
     */
    private void openClass(MyAddClass item) {
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
                bundle.putString(Constants.PASS_STRING,item.getId());
                ((BaseActivity) getActivity()).openActivity(PrivateClassByMessageActivity.class,bundle);

            } else if (item.getCourseType().equals("3")) {
                //TODO
            }
        }

    }

    private void loadData() {
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        Map<String, String> datas = new HashMap<>();
        datas.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        datas.put("showType", "1");
        PostRequest request = new PostRequest(Constants.USER_CONTACTCOURSELIST, datas, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                MyAddClassList subClasses = JsonUtils.objectFromJson(response, MyAddClassList.class);
                if (subClasses != null && subClasses.getListData().size() > 0) {
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
