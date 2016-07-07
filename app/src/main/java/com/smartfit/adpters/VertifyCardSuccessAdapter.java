package com.smartfit.adpters;/**
 * 作者：dengyancheng on 16/7/7 22:39
 * 邮箱：yanchengdeng@gmail.com
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：dengyancheng on 16/7/7 22:39
 * 邮箱：yanchengdeng@gmail.com
 */
public class VertifyCardSuccessAdapter extends BaseAdapter {

    private Context context;
    private List<String> vertifyCards;

    public VertifyCardSuccessAdapter(Context context, List<String> vertifyCards) {
        this.context = context;
        this.vertifyCards = vertifyCards;

    }

    @Override
    public int getCount() {
        return vertifyCards.size();
    }

    @Override
    public Object getItem(int position) {
        return vertifyCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_vertify_card_success_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(vertifyCards.get(position))) {
            viewHolder.etCardCode.setText(vertifyCards.get(position));
        }
        return convertView;
    }

    public void setData(List<String> vertifyCards) {
        this.vertifyCards = vertifyCards;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_vertify_card_success_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_card_code)
        TextView tvCardCode;
        @Bind(R.id.et_card_code)
        TextView etCardCode;
        @Bind(R.id.tv_vertify_card_code)
        ImageView tvVertifyCardCode;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
