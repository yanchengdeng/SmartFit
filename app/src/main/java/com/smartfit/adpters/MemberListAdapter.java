package com.smartfit.adpters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MemeberInfo;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yanchengdeng  on 2016/3/22.
 */
public class MemberListAdapter extends BaseAdapter {

    private List<MemeberInfo> datas;
    private Context context;

    public MemberListAdapter(Context context, List<MemeberInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_member_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MemeberInfo item = datas.get(position);
        ImageLoader.getInstance().displayImage(item.getUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvName.setText(item.getNickName());
        }

        if (!TextUtils.isEmpty(item.getMobileNo())) {
            viewHolder.tvPhone.setText(item.getMobileNo());
        }

        viewHolder.btnDialet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwo(item.getMobileNo());
            }
        });
        return convertView;
    }

    public void setData(List<MemeberInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    private void NormalDialogStyleTwo(final String mobileNo) {
        final NormalDialog dialog = new NormalDialog(context);
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
                                    mobileNo));
                            ((BaseActivity)context).startActivity(intent);
                        } catch (Exception E) {
                            ((BaseActivity)context).mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_member_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.btn_dialet)
        Button btnDialet;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
