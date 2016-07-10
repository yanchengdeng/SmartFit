package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.BindCard;
import com.smartfit.MessageEvent.ShareTicketSuccess;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 绑定实体卡
 */
public class BindCardActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_card_code)
    EditText etCardCode;
    @Bind(R.id.ll_code_ui)
    LinearLayout llCodeUi;
    @Bind(R.id.tv_code_tips)
    TextView tvCodeTips;
    @Bind(R.id.btn_bind)
    Button btnBind;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
        addLisener();
    }


    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof ShareTicketSuccess){
            finish();
        }
    }
    private void initView() {
        tvTittle.setText(getString(R.string.bind_card));

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindCard(etCardCode.getEditableText().toString());
            }
        });

    }

    private void bindCard(String code) {

        if (TextUtils.isEmpty(code)){
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (code.length()!=13){
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_lenght_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        Map<String,String > map = new HashMap<>();
        map.put("cardSNNumber",code);
        mSVProgressHUD.showWithStatus(getString(R.string.bind_card_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.EVENT_CARDBINDEVENT, map,new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("绑定成功，请在券包内查看记录", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new BindCard());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(BindCardActivity.this);
        mQueue.add(request);
    }


}
