package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.smartfit.adpters.PrivateEducationCoachAdapter;
import com.smartfit.beans.PrivateCalssType;
import com.smartfit.beans.SelectCoachInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 选择教练
 *
 * @author dengyancheng
 */
public class SelectedCoachActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_type_name)
    TextView tvTypeName;
    @Bind(R.id.tv_couch_num)
    TextView tvCouchNum;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.no_data)
    TextView noData;

    /**
     * bundle.putString("venueId", venueId);
     * bundle.putParcelable(Constants.PASS_OBJECT, privateCalssTypes.get(position));
     * bundle.putString("dayTime", selectDate);
     *
     * @param savedInstanceState
     */


    private String venueId;
    private String dayTime;
    private PrivateCalssType privateCalssType;

  private   List<SelectCoachInfo> selectCoachInfoList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_coach);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("选择教练");
        venueId = getIntent().getStringExtra("venueId");
        dayTime = getIntent().getStringExtra("dayTime");
        privateCalssType = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        if (!TextUtils.isEmpty(privateCalssType.getClassificationName())) {
            tvTypeName.setText(privateCalssType.getClassificationName());
        }

        if (!TextUtils.isEmpty(privateCalssType.getCoachNum())) {
            tvCouchNum.setText(String.format("%s位教练", privateCalssType.getCoachNum()));
        }

        getCoachList();

    }

    /**
     * 获取可选教练列表
     */
    private void getCoachList() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("venueId", venueId);
        data.put("dayTime", String.valueOf(DateUtils.getTheDateMillions(dayTime)));
        data.put("classificationId", privateCalssType.getClassificationId());
        PostRequest request = new PostRequest(Constants.CLASSIF_LISTCOACHESBYVENUEIDANDCOURSETYPECODE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                 selectCoachInfoList = JsonUtils.listFromJson(response.getAsJsonArray("list"), SelectCoachInfo.class);
                if (selectCoachInfoList != null && selectCoachInfoList.size() > 0) {
                    listView.setAdapter(new PrivateEducationCoachAdapter(SelectedCoachActivity.this,selectCoachInfoList));
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else{
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                noData.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = getIntent().getExtras();
                bundle.putParcelable("coach",selectCoachInfoList.get(position));
                openActivity(SettingOrderCoachTimeActivity.class,bundle);
            }
        });


    }


}
