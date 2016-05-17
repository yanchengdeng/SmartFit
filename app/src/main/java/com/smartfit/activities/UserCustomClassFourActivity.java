package com.smartfit.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.R;
import com.smartfit.beans.CustomClassReleaseInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 自订课程第四步
 *
 * @author yanchengdeng
 *         create at 2016/4/25 14:30
 */
public class UserCustomClassFourActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_class_name)
    EditText etClassName;
    @Bind(R.id.et_class_content)
    EditText etClassContent;
    @Bind(R.id.tv_friends_tips)
    TextView tvFriendsTips;
    @Bind(R.id.tv_friends_tip_content)
    TextView tvFriendsTipContent;
    @Bind(R.id.tv_friends_num)
    TextView tvFriendsNum;
    @Bind(R.id.rl_friends_count)
    RelativeLayout rlFriendsCount;
    @Bind(R.id.tv_open_tips)
    TextView tvOpenTips;
    @Bind(R.id.tv_open_tip_content)
    TextView tvOpenTipContent;
    @Bind(R.id.tv_open_num)
    TextView tvOpenNum;
    @Bind(R.id.rl_open_count)
    RelativeLayout rlOpenCount;
    @Bind(R.id.tv_venue_fee)
    TextView tvVenueFee;
    @Bind(R.id.tv_coach_fee)
    TextView tvCoachFee;
    @Bind(R.id.tv_count_fee)
    TextView tvCountFee;
    @Bind(R.id.btn_pay)
    Button btnPay;

    //好友
    private int friends = 1;
    //开放人群数
    private int opener = 1;
    private String startTime, endTime, courseClassId, venueId, venuPrice, coachPrice, coachId,roomId;
    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_custom_class_four);
        ButterKnife.bind(this);eventBus =EventBus.getDefault();
        eventBus.register(this);
        initView();
        addLisener();
    }
    @Subscribe
    public void onEvent(UpdateCustomClassInfo event) {/* Do something */
        finish();
    }


    private void initView() {
        tvTittle.setText("自订课程(4/4)");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        courseClassId = getIntent().getStringExtra("courseClassId");
        venueId = getIntent().getStringExtra("venueId");
        venuPrice = getIntent().getStringExtra("venuePrice");
        coachPrice = getIntent().getStringExtra("coachPrice");
        coachId = getIntent().getStringExtra("coachId");
        roomId = getIntent().getStringExtra("roomId");
        countPrice();
    }

    /**
     * 计算费用
     */
    private void countPrice() {
        tvVenueFee.setText(venuPrice + "元");
        tvCoachFee.setText(coachPrice + "元*" + friends);
        float countMoney = Float.parseFloat(venuPrice) + Float.parseFloat(coachPrice) * friends;
        tvCountFee.setText(countMoney + "元");

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlFriendsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(1);//好友
            }
        });

        rlOpenCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialogDefault(2);//开放
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParms(etClassName.getEditableText().toString(), etClassContent.getEditableText().toString());
            }
        });

    }


    private void MaterialDialogDefault(final int flag) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        final EditText etContent = (EditText) dialog.getWindow().findViewById(R.id.et_update_content);
        etContent.setInputType(InputType.TYPE_CLASS_PHONE);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (flag == 1) {
            tvTittle.setText("输入伙伴人数");
        } else {
            tvTittle.setText("输入开放人数");
        }

        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("未填写信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                } else {
                    if (TextUtils.isDigitsOnly(etContent.getEditableText().toString())) {
                        if (flag == 1) {
                            friends = Integer.parseInt(etContent.getEditableText().toString());
                            countPrice();
                            tvFriendsNum.setText(friends + "人");
                        } else {
                            opener = Integer.parseInt(etContent.getEditableText().toString());
                            tvOpenNum.setText(opener + "人");
                        }
                    } else {
                        mSVProgressHUD.showInfoWithStatus("只能输入数字", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }
            }
        });
    }

    private void checkParms(String name, String content) {
        if (TextUtils.isEmpty(name)) {
            mSVProgressHUD.showInfoWithStatus("请填写课程名");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            mSVProgressHUD.showInfoWithStatus("请填写课程描述");
            return;
        }


        Map<String, String> maps = new HashMap<>();
        maps.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        maps.put("courseType", "1");
        maps.put("courseName", name);
        maps.put("courseDetail", content);
        maps.put("courseClassId", courseClassId);
        maps.put("classroomId", roomId);
        maps.put("startTime", startTime);
        maps.put("endTime", endTime);
        maps.put("partnerCount", String.valueOf(friends));
        maps.put("otherCount", String.valueOf(opener));
        maps.put("coachIdStr", coachId);

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.COURSE_MEMBERRELEASECOURSE, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                mSVProgressHUD.dismiss();

                CustomClassReleaseInfo customClassReleaseInfo = JsonUtils.objectFromJson(response,CustomClassReleaseInfo.class);
                if(customClassReleaseInfo != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 6);
                    bundle.putString(Constants.COURSE_ID, customClassReleaseInfo.getId());
                    bundle.putString(Constants.COURSE_MONEY, String.format("%.2f",Float.parseFloat(customClassReleaseInfo.getTotalPrice())));
                    openActivity(PayActivity.class,bundle);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(UserCustomClassFourActivity.this);
        mQueue.add(request);
    }
}
