package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.CityListActivity;
import com.smartfit.beans.CityBeanGroup;
import com.smartfit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Administrator on 2016/3/15.
 */
public class CityAdapter extends BaseAdapter {


    private List<CityBeanGroup> datas = new ArrayList<>();


    private Context context;

    public CityAdapter(Context context, List<CityBeanGroup> datas) {
        this.datas = datas;

        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_city_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CityBeanGroup item = datas.get(position);
        if(!TextUtils.isEmpty(item.getIndex())){
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < item.getTags().size(); i++) {
                tags.add(item.getTags().get(i).getDictionaryName());
            }

            viewHolder.tvLetter.setText(item.getIndex().toUpperCase());
            viewHolder.tagGroup.setTags(tags.toArray(new String[]{}));

            viewHolder.tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                @Override
                public void onTagClick(String s) {
                    Util.setCityInfo(s);
                    ((CityListActivity)context).setResult(11);
                    ((CityListActivity)context).finish();
                }
            });
        }


        return convertView;
    }


    public void setData(List<CityBeanGroup> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_city_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_letter)
        TextView tvLetter;
        @Bind(R.id.tag_group)
        TagGroup tagGroup;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
