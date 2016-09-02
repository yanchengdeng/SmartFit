package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.VertifyCardSuccessAdapter;
import com.smartfit.beans.EntityCardInfo;
import com.smartfit.beans.EntityType;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择实体卡 去支付
 */
public class SelectCardToBuyActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.tv_card_code)
    TextView tvCardCode;
    @Bind(R.id.et_card_code)
    EditText etCardCode;
    @Bind(R.id.tv_vertify_card_code)
    TextView tvVertifyCardCode;
    @Bind(R.id.rl_card_code_ui)
    RelativeLayout rlCardCodeUi;
    @Bind(R.id.tv_card_code_pay_tips)
    TextView tvCardCodePayTips;

    private String couserType = "0";// 实体卡支付类型    0 包月 ；  1 小班  ；  2 私教

    private ArrayList<EntityCardInfo> vertifyCards = new ArrayList<>();

    private VertifyCardSuccessAdapter adapter;

    private int selectMaxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card_to_buy);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("使用实体卡");
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.sure));
        adapter = new VertifyCardSuccessAdapter(this, vertifyCards);
        listview.setAdapter(adapter);
        selectMaxNum = getIntent().getIntExtra(Constants.PASS_STRING, 0);
        couserType = getIntent().getStringExtra(Constants.COURSE_TYPE);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //验证实体卡
        tvVertifyCardCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    vertyfiyCode(etCardCode.getEditableText().toString());
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vertifyCards.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < vertifyCards.size(); i++) {
                        if (i == vertifyCards.size() - 1) {
                            stringBuffer.append(vertifyCards.get(i));
                        } else {
                            stringBuffer.append(vertifyCards.get(i)).append("|");
                        }
                    }
                    Intent intent = new Intent(SelectCardToBuyActivity.this, ConfirmPayActivity.class);
                    intent.putParcelableArrayListExtra(Constants.PASS_OBJECT, vertifyCards);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mSVProgressHUD.showInfoWithStatus("没有验证的实体卡", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }

            }
        });

    }

    /**
     * 绑定实体卡
     *
     * @param code
     */
    private void vertyfiyCode(final String code) {
        if (TextUtils.isEmpty(code)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (code.length() != 13) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_lenght_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("cardSNNumber", code);
        map.put("cardType", couserType);
        mSVProgressHUD.showWithStatus(getString(R.string.vertify_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.EVENT_CARDCHECK, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                EntityType entityType = JsonUtils.objectFromJson(response, EntityType.class);
                EntityCardInfo entityCardInfo = new EntityCardInfo(code, entityType.getData());
               if (!checkCardsMouth(vertifyCards,entityCardInfo)) {
                   tvCardCodePayTips.setText(getString(R.string.card_pay_tips));
               }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(SelectCardToBuyActivity.this);
        mQueue.add(request);
    }

    /**
     * 计算 所添加的实体卡是否 大于需要购买的 月数
     * @param vertifyCards
     * @param entityCardInfo
     * @return  true: 大于
     */
    private boolean checkCardsMouth(ArrayList<EntityCardInfo> vertifyCards, EntityCardInfo entityCardInfo) {
        if (vertifyCards!=null&& vertifyCards.size()>0){
            vertifyCards.add(entityCardInfo);
            int maxMouth = 0 ;
            for (EntityCardInfo item:vertifyCards){
                if (item.getType().equals("14")){
                    maxMouth = maxMouth + 1;
                }else if(item.getType().equals("15")){
                    maxMouth = maxMouth + 3;
                }else if (item.getType().equals("16")){
                    maxMouth = maxMouth + 12;
                }
            }
            if (maxMouth>selectMaxNum){
                tvCardCodePayTips.setText(String.format("您的实体卡支持购买%s个月包月服务，目前只选择%s个月，请返回重新选择。",new Object[]{maxMouth,selectMaxNum}));
                return true;
            }else{
                vertifyCards.add(entityCardInfo);
                etCardCode.setText("");
                etCardCode.setHint(getString(R.string.card_tips));
                adapter.setData(vertifyCards);
                return false;
            }
        }else{
            int maxMouth = 0;
            if (entityCardInfo.getType().equals("14")){
                maxMouth = maxMouth + 1;
            }else if (entityCardInfo.getType().equals("15")){
                maxMouth = maxMouth + 3;
            }else if(entityCardInfo.getType().equals("16")){
                maxMouth = maxMouth + 12;
            }
            if (maxMouth>selectMaxNum){
                tvCardCodePayTips.setText(String.format("您的实体卡支持购买%s个月包月服务，目前只选择%s个月，请返回重新选择。",new Object[]{maxMouth,selectMaxNum}));
                return true;
            }else{
                vertifyCards.add(entityCardInfo);
                etCardCode.setText("");
                etCardCode.setHint(getString(R.string.card_tips));
                adapter.setData(vertifyCards);
                return false;
            }
        }
    }
}
