package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.GroupExpericeItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 搜索课程  页
 */

public class SearchClassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_search_condition)
    TextView tvSearchCondition;

    private View footerView;
    private int page = 1;
    boolean isLoading = false;
    private GroupExpericeItemAdapter adapter;
    private List<String> datas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_class);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        footerView = LayoutInflater.from(this).inflate(R.layout.list_loader_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);
        listView.addFooterView(footerView);
        adapter = new GroupExpericeItemAdapter(this, datas);
        listView.setAdapter(adapter);
    }


    private void addLisener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 加载更多
         */

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen >= totalItemCount - 1 && !isLoading) {
                    isLoading = true;
                    page++;
                    loadData("");
                }
            }
        });


        /***搜索**/
        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showErrorWithStatus("请输入搜索条件", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                    return;
                }
                page = 1;
                loadData(etContent.getEditableText().toString());
            }
        });
    }

    private void loadData(String contions) {
        for (int i = 0; i < 10; i++) {
            datas.add("模拟数据" + i + String.valueOf(page));
        }

        adapter.setData(datas);
        tvCount.setText(String.valueOf(datas.size()));
        tvSearchCondition.setText(String.format(getString(R.string.find_conditon_result), new Object[]{contions}));

        listView.removeFooterView(footerView);
        isLoading = false;
    }


}
