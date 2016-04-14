package com.smartfit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.adpters.MyClassOrderStatusAdapter;
import com.smartfit.beans.MyAddClass;

import java.util.ArrayList;
import java.util.List;

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




    }

    private void loadData() {
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    datas.add(new MyAddClass());
                }
                listView.setVisibility(View.VISIBLE);
                adapter.setData(datas);
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
