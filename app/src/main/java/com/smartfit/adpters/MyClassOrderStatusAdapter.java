package com.smartfit.adpters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MyAddClass;
import com.smartfit.utils.Options;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 课程状态
 */
public class MyClassOrderStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddClass> datas;
    private boolean isHandleShow = true;

    public MyClassOrderStatusAdapter(Context context, List<MyAddClass> datas, boolean isHandleShow) {
        this.context = context;
        this.datas = datas;
        this.isHandleShow = isHandleShow;

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_my_classes_status_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyAddClass item = datas.get(position);

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
//        viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime() + "~" + DateUtils.getDataTime(item.getEndTime())));

        ImageLoader.getInstance().displayImage(item.getStartUserPicUrl(), viewHolder.ivIcon, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(item.getStartUserName())) {
            viewHolder.tvName.setText(item.getStartUserName());
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
        if (!TextUtils.isEmpty(item.getCourseDetail())) {
            viewHolder.tvClassInfo.setText(item.getCourseDetail());
        }

        if (!TextUtils.isEmpty(item.getCoursePrice())) {
            viewHolder.tvMoney.setText(item.getCoursePrice());
        }


        viewHolder.tvContactCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwo(item.getCoachPhone());
            }
        });


        viewHolder.tvCancleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (isHandleShow) {
            viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(context.getString(R.string.going));
        } else {
            viewHolder.llHandleFunciton.setVisibility(View.GONE);
            viewHolder.tvStatus.setText(context.getString(R.string.already_over));
        }


        return convertView;
    }

    public void setData(List<MyAddClass> datas) {
        this.datas = datas;
    }

    private void NormalDialogStyleTwo(final String phone) {
        if (TextUtils.isEmpty(phone)){
            ((BaseActivity)context).mSVProgressHUD.showInfoWithStatus("空号", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        final NormalDialog dialog = new NormalDialog(context);
        dialog.content("确定要拨打教练电话吗？")//
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
                            ((BaseActivity)context).startActivity(intent);
                        } catch (Exception E) {
                            ((BaseActivity)context).mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_my_classes_status_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_contact_coach)
        TextView tvContactCoach;
        @Bind(R.id.tv_cancle_class)
        TextView tvCancleClass;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
