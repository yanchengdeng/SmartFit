package com.smartfit.adpters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.CoachInfoActivity;
import com.smartfit.activities.MainBusinessActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 私教  数据列表
 */
public class PrivateEducationAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    LinearLayout.LayoutParams params;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;

    public PrivateEducationAdapter(Context context
            , List<String> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();

    }

    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<datas.size();i++) {
            getIsSelected().put(i,false);
        }
    }


    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        PrivateEducationAdapter.isSelected = isSelected;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_private_education_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ratingBar.setLayoutParams(params);

        if(!getIsSelected().isEmpty()){
            Log.w("dyc",getIsSelected()+"=======");
            viewHolder.chSelect.setChecked(getIsSelected().get(position));
        }

//        viewHolder.chSelect.setChecked(getIsSelected().get(position));
        viewHolder.chSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainBusinessActivity)context).openActivity(CoachInfoActivity.class);
            }
        });
        return convertView;
    }

    public void setData(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_private_education_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_coach)
        TextView tvCoach;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.ch_select)
       public CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
