package com.smartfit.adpters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.Certificate;
import com.smartfit.utils.Options;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/27.
 */
public class MoreCertiaicatePassAdapter extends BaseAdapter {
    private List<Certificate> certificates;
    private Context context;
    private Handler handler;

    public MoreCertiaicatePassAdapter(Context context, List<Certificate> certificates, Handler handler) {
        this.context = context;
        this.certificates = certificates;
        this.handler = handler;
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


        final Certificate item = certificates.get(position);

        viewHolder.tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    viewHolder.cbName.setImageResource(R.mipmap.icon_close);
                } else {
                    Message msg = new Message();
                    msg.what = position;
                    msg.obj = s.toString();
                    handler.sendMessage(msg);

                    viewHolder.cbName.setImageResource(R.mipmap.icon_choose);
                }

            }
        });

        if (!TextUtils.isEmpty(item.getText_tips())) {
            viewHolder.tvNameTips.setText(item.getText_tips());
        }

        if (!TextUtils.isEmpty(item.getImage_tips())) {
            viewHolder.tvCertificate.setText(item.getImage_tips());
        }

        if (!TextUtils.isEmpty(item.getName())) {
            viewHolder.tvName.setText(item.getName());
        } else {
            viewHolder.tvName.setHint("请填写证书名称");
            viewHolder.tvName.setText("");
        }


        //1待审；2通过；3不通过
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("1")) {
                viewHolder.cbPhoto.setImageResource(R.mipmap.icon_close);
                viewHolder.cbName.setImageResource(R.mipmap.icon_close);
            } else if (item.getStatus().equals("2")) {
                viewHolder.cbPhoto.setImageResource(R.mipmap.icon_choose);
                viewHolder.cbName.setImageResource(R.mipmap.icon_choose);
            } else {
                viewHolder.cbPhoto.setImageResource(R.mipmap.icon_close);
                viewHolder.cbName.setImageResource(R.mipmap.icon_close);
            }
        }

        ImageLoader.getInstance().displayImage(item.getPhoto(), viewHolder.ivCertificate, Options.getListOptions());

        viewHolder.ivCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> photos = new ArrayList<String>();
                Message msg = new Message();
                msg.what = position;
                msg.obj = photos;
                handler.sendMessage(msg);
            }
        });

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
        @Bind(R.id.cb_photo)
        ImageView cbPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
