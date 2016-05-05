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
import butterknife.OnClick;

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

    @Bind(R.id.tv_server_phone)
    TextView tvServerPhone;
    @Bind(R.id.tv_faq_1)
    TextView tvFaq1;
    @Bind(R.id.tv_faq_2)
    TextView tvFaq2;
    @Bind(R.id.tv_faq_3)
    TextView tvFaq3;
    @Bind(R.id.tv_faq_4)
    TextView tvFaq4;
    @Bind(R.id.tv_faq_5)
    TextView tvFaq5;
    @Bind(R.id.tv_faq_6)
    TextView tvFaq6;
    @Bind(R.id.tv_faq_7)
    TextView tvFaq7;
    @Bind(R.id.tv_faq_8)
    TextView tvFaq8;
    @Bind(R.id.tv_faq_9)
    TextView tvFaq9;
    @Bind(R.id.tv_faq_10)
    TextView tvFaq10;
    @Bind(R.id.tv_faq_11)
    TextView tvFaq11;
    @Bind(R.id.tv_faq_12)
    TextView tvFaq12;
    @Bind(R.id.tv_faq_13)
    TextView tvFaq13;
    @Bind(R.id.tv_faq_14)
    TextView tvFaq14;
    @Bind(R.id.tv_faq_15)
    TextView tvFaq15;
    @Bind(R.id.tv_faq_16)
    TextView tvFaq16;
    @Bind(R.id.tv_faq_17)
    TextView tvFaq17;
    @Bind(R.id.tv_faq_18)
    TextView tvFaq18;
    @Bind(R.id.tv_faq_19)
    TextView tvFaq19;
    @Bind(R.id.tv_faq_20)
    TextView tvFaq20;

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
        String[] tittles = getResources().getStringArray(R.array.fqa_tittle);
        tvFaq1.setText(tittles[0]);
        tvFaq2.setText(tittles[1]);
        tvFaq3.setText(tittles[2]);
        tvFaq4.setText(tittles[3]);
        tvFaq5.setText(tittles[4]);
        tvFaq6.setText(tittles[5]);
        tvFaq7.setText(tittles[6]);
        tvFaq8.setText(tittles[7]);
        tvFaq9.setText(tittles[8]);
        tvFaq10.setText(tittles[9]);
        tvFaq11.setText(tittles[10]);
        tvFaq12.setText(tittles[11]);
        tvFaq13.setText(tittles[12]);
        tvFaq14.setText(tittles[13]);
        tvFaq15.setText(tittles[14]);
        tvFaq16.setText(tittles[15]);
        tvFaq17.setText(tittles[16]);
        tvFaq18.setText(tittles[17]);
        tvFaq19.setText(tittles[18]);
        tvFaq20.setText(tittles[19]);


    }


    private void addLisener() {
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
                                    "4006053337"));
                            startActivity(intent);
                        } catch (Exception E) {
                            mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }

    @OnClick({R.id.tv_faq_1, R.id.tv_faq_2, R.id.tv_faq_3, R.id.tv_faq_4, R.id.tv_faq_5, R.id.tv_faq_6, R.id.tv_faq_7, R.id.tv_faq_8, R.id.tv_faq_9, R.id.tv_faq_10, R.id.tv_faq_11, R.id.tv_faq_12, R.id.tv_faq_13, R.id.tv_faq_14, R.id.tv_faq_15, R.id.tv_faq_16, R.id.tv_faq_17, R.id.tv_faq_18, R.id.tv_faq_19, R.id.tv_faq_20,R.id.tv_faq_21})
    public void onClick(View view) {
        Bundle bundle = new Bundle();

        switch (view.getId()) {
            case R.id.tv_faq_1:
                bundle.putInt("key", 0);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_2:
                bundle.putInt("key", 1);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_3:
                bundle.putInt("key", 2);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_4:
                bundle.putInt("key", 3);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_5:
                bundle.putInt("key", 4);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_6:
                bundle.putInt("key", 5);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_7:
                bundle.putInt("key", 6);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_8:
                bundle.putInt("key", 7);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_9:
                bundle.putInt("key", 8);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_10:
                bundle.putInt("key", 9);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_11:
                bundle.putInt("key", 10);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_12:
                bundle.putInt("key", 11);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_13:
                bundle.putInt("key", 12);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_14:
                bundle.putInt("key", 13);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_15:
                bundle.putInt("key", 14);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_16:
                bundle.putInt("key", 15);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_17:
                bundle.putInt("key", 16);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_18:
                bundle.putInt("key", 17);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_19:
                bundle.putInt("key", 18);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_20:
                bundle.putInt("key", 19);
                openActivity(FaqContentActivity.class, bundle);
                break;
            case R.id.tv_faq_21:
                bundle.putInt("key",20);
                openActivity(FaqContentActivity.class, bundle);
                break;
        }
    }
}
