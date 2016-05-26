package com.smartfit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.utils.Options;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 联系教练
 */

public class ContactCoachActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.btn_contact)
    Button btnContact;


    private String icon, name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_coach);
        ButterKnife.bind(this);
        tvTittle.setText("联系教练");
        /**
         *   bundle.putString("icon",classInfoDetail.getUserPicUrl());
         bundle.putString("name",classInfoDetail.getCoachRealName());
         bundle.putString("phone",classInfoDetail.getMobileNo());
         */
        icon = getIntent().getStringExtra("icon");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        ImageLoader.getInstance().displayImage(icon, ivHeader, Options.getListOptions());
        if (!TextUtils.isEmpty(name)) {
            tvName.setText(name + "  教练");
        }

        if (!TextUtils.isEmpty(phone)) {
            tvPhone.setText(phone);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone)) {
                    mSVProgressHUD.showInfoWithStatus("未提供号码", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                }
                NormalDialogStyleTwo();
            }
        });
    }

    private void NormalDialogStyleTwo() {
        final NormalDialog dialog = new NormalDialog(ContactCoachActivity.this);
        dialog.content(String.format("您确定要拨打%s电话吗？", new Object[]{name}))//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                        //.showAnim(mBasIn)//
                        //.dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                                    phone));
                            startActivity(intent);
                        } catch (Exception E) {
                            mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }


}
