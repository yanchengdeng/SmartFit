package com.smartfit.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.adpters.PrivateEducationAdapter;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.views.MyListView;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 开试预约私教课程
 */
public class OrderPrivateEducationClassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.iv_space_icon)
    SelectableRoundedImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_count_down)
    TextView tvCountDown;


    private PrivateEducationAdapter adapter;
    private List<PrivateEducationClass> datas = new ArrayList<PrivateEducationClass>();

    private CountDownTimer countDownTimer;

    private long coundTime = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_education_class);
        ButterKnife.bind(this);
        countDownTimer = new CountDownTimer(coundTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                openActivity(OrderPrivateEducationSuccessActivity.class);
            }
        };
        countDownTimer.start();
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.private_education));
        adapter = new PrivateEducationAdapter(this, datas);
        adapter.setDismissCheck(false);
        listView.setAdapter(adapter);
        loadData();
    }



    private void loadData() {
        for (int i = 0; i < 3; i++) {
            PrivateEducationClass item = new PrivateEducationClass();
            item.setName("王小二教练" + i + String.valueOf(i));
            datas.add(item);
        }
        adapter.setData(datas);
    }


}
