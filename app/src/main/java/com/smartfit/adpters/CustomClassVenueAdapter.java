package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.CustomClassVenue;
import com.smartfit.beans.CustomClassVenueItem;
import com.smartfit.utils.Options;
import com.smartfit.views.MyListView;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： Administrator 创建于： 2016/4/25 9:54.
 * 邮箱：yanchengdeng@gmail.com
 */
public class CustomClassVenueAdapter extends BaseAdapter {
    private Context context;
    private List<CustomClassVenue> customClassVenues;

    public CustomClassVenueAdapter(Context context, List<CustomClassVenue> customClassVenues) {
        this.context = context;
        this.customClassVenues = customClassVenues;
    }

    @Override
    public int getCount() {
        return customClassVenues.size();
    }

    @Override
    public Object getItem(int position) {
        return customClassVenues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_custom_venue_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


     final CustomClassVenue item =    customClassVenues.get(position);
        if (!TextUtils.isEmpty(item.getVenueName())){
            viewHolder.tvVenueName.setText(item.getVenueName());
        }
        if (!TextUtils.isEmpty(item.getLat()) &&!TextUtils.isEmpty(item.getLongit()) && !TextUtils.isEmpty(item.getCount())) {
            //TODO  经纬度换算
            viewHolder.tvInfo.setText("距离：" + 0 + "共" + item.getCount() + "个教室");
        }

        ImageLoader.getInstance().displayImage(item.getVenueUrl(), viewHolder.ivIcon, Options.getListOptions());

        if (item.getClassroomList()!= null &&item.getClassroomList().size()>0){
            viewHolder.listView.setAdapter(new CustomVenuPriceItem(context,item.getClassroomList()));
            viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for(CustomClassVenue customClassVenue:customClassVenues){
                        for (CustomClassVenueItem item:customClassVenue.getClassroomList()){
                            item.setIsCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select_price);
                    checkBox.setChecked(!checkBox.isChecked());
                    item.getClassroomList().get(position).setIsCheck(checkBox.isChecked());
                    ((CustomVenuPriceItem) viewHolder.listView.getAdapter()).notifyDataSetChanged();
                }
            });
        }



        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.rl_icon_ui)
        RelativeLayout rlIconUi;
        @Bind(R.id.tv_venue_name)
        TextView tvVenueName;
        @Bind(R.id.tv_info)
        TextView tvInfo;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.listView)
        MyListView listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
