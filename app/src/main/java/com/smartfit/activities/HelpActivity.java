package com.smartfit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.smartfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 帮助
 *
 * @author dengyancheng
 */

public class HelpActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_faq_publish_class)
    TextView tvFaqPublishClass;
    @Bind(R.id.tv_faq_connot_topup)
    TextView tvFaqConnotTopup;
    @Bind(R.id.tv_server_phone)
    TextView tvServerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.help));
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.suggest_fadeback));

    }


    private void addLisener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FadeBackActivity.class);
            }
        });

        tvServerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwo();
            }
        });
    }

    private void NormalDialogStyleTwo() {
        final NormalDialog dialog = new NormalDialog(HelpActivity.this);
        dialog.content("确定要拨打客服电话吗？")//
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
                                    "13067380836"));
                            startActivity(intent);
                        } catch (Exception E) {
                            mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }

}
