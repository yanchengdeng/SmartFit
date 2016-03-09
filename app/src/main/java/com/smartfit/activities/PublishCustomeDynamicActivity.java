package com.smartfit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.GridViewPublishPhotoAdapter;
import com.smartfit.utils.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/***
 * 发布状态
 */
public class PublishCustomeDynamicActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_mood_content)
    EditText etMoodContent;
    @Bind(R.id.gv_select_photos)
    GridView gvSelectPhotos;
    @Bind(R.id.iv_select_photos)
    ImageView ivSelectPhotos;
    @Bind(R.id.iv_same_wx)
    ImageView ivSameWx;


    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_custome_dynamic);
        ButterKnife.bind(this);
        tvTittle.setText("动态");
        ivFunction.setBackgroundResource(R.mipmap.icon_right);
        addLisener();
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
                mSVProgressHUD.showSuccessWithStatus("发布成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });

        ivSelectPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoThum();
            }
        });

        gvSelectPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != mSelectPath && mSelectPath.size() > 0) {
                    goPhotoThum();
                }
            }
        });
    }


    /**
     * 进入相册ji
     */
    private void goPhotoThum() {
        Intent intent = new Intent(PublishCustomeDynamicActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                LogUtil.d("dyc", mSelectPath.toString());
                if(null != mSelectPath  && mSelectPath.size()>0){
                    gvSelectPhotos.setAdapter(new GridViewPublishPhotoAdapter(PublishCustomeDynamicActivity.this,mSelectPath));
                }
            }
        }
    }
}
