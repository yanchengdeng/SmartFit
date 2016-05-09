package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.SubmitAuthInfo;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/27.
 */
public class MoreCertiaicateVertifyAdapter extends BaseAdapter {
    private List<SubmitAuthInfo> certificates;
    private Context context;

    public MoreCertiaicateVertifyAdapter(Context context, List<SubmitAuthInfo> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    @Override
    public int getCount() {
        return certificates.size();
    }

    @Override
    public Object getItem(int position) {
        return certificates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_more_ceritiface, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


         SubmitAuthInfo item = certificates.get(position);


        viewHolder.tvNameTips.setText("获得的证书");
        viewHolder.tvCertificate.setText("上传证书照片");
        if (item.getStatus().equals("1")) {
            viewHolder.cbPhoto.setImageResource(R.mipmap.icon_close);
            viewHolder.cbName.setImageResource(R.mipmap.icon_close);
        } else if (item.getStatus().equals("2")) {
            viewHolder.cbPhoto.setImageResource(R.mipmap.icon_choose);
            viewHolder.cbName.setImageResource(R.mipmap.icon_choose);
        } else {
            viewHolder.cbPhoto.setImageResource(R.mipmap.icon_choose);
            viewHolder.cbName.setImageResource(R.mipmap.icon_choose);
        }


        viewHolder.ivAddPhoto.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.getCertificateName())) {
            viewHolder.tvName.setText(item.getCertificateName());
        }

        ImageLoader.getInstance().displayImage(item.getCertificateImg(),viewHolder.ivCertificate, Options.getListOptions());



        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_more_ceritiface.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_name_tips)
        TextView tvNameTips;
        @Bind(R.id.tv_name)
        EditText tvName;
        @Bind(R.id.cb_name)
        ImageView cbName;
        @Bind(R.id.tv_certificate)
        TextView tvCertificate;
        @Bind(R.id.iv_certificate)
        ImageView ivCertificate;
        @Bind(R.id.iv_add_photo)
        ImageView ivAddPhoto;
        @Bind(R.id.cb_photo)
        ImageView cbPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
