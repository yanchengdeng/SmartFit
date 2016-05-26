package com.smartfit.adpters;/**
 * 作者：dengyancheng on 16/5/8 22:33
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
import com.smartfit.beans.UserCustomClass;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 作者：dengyancheng on 16/5/8 22:33
 * 邮箱：yanchengdeng@gmail.com
 */
public class CustomClassAdapter extends BaseAdapter {
    Context context;
    List<UserCustomClass> datas;

    public CustomClassAdapter(Context context, List<UserCustomClass> datas) {
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
        ViewHolder vieHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_custom_class_item, null);
            vieHolder = new ViewHolder(convertView);
            convertView.setTag(vieHolder);

        } else {
           vieHolder = (ViewHolder) convertView.getTag();

        }

        UserCustomClass item = datas.get(position);
        if (!TextUtils.isEmpty(item.getCourseDetail())) {
            vieHolder.tvClassTittle.setText(item.getCourseDetail());
        }

       if( !TextUtils.isEmpty(item.getCourseClassId())){
            if (item.getCourseClassId().equals("0")){
                vieHolder.tvClassType.setText("团操课程"+" "+item.getCourseName());
            }else if(item.getCourseClassId().equals("1")){
                vieHolder.tvClassType.setText("小班课程"+" "+item.getCourseName());
            }else if(item.getCourseClassId().equals("2")){
                vieHolder.tvClassType.setText("私教课程"+" "+item.getCourseName());
            }else {
                vieHolder.tvClassType.setText("有氧器械课程"+" "+item.getCourseName());
            }
        }

        if (!TextUtils.isEmpty(item.getVenueName())) {
            vieHolder.tvClassAddress.setText(item.getVenueName());
        }
        if (!TextUtils.isEmpty(item.getDistance())) {
            vieHolder.tvClassDistanceNum.setText(item.getDistance()+"m"+"  "+"限"+item.getOtherCount()+"人");
        }

        if (!TextUtils.isEmpty(item.getCoachPrice())) {
            if (Float.parseFloat(item.getCoachPrice())==0) {
                vieHolder.tvPrice.setText("免费");
            }else{
                vieHolder.tvPrice.setText(item.getCoachPrice());
            }
        }
        return convertView;
    }

    public void setData(List<UserCustomClass> userCustomClasses) {
        this.datas = userCustomClasses;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_custom_class_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_class_tittle)
        TextView tvClassTittle;
        @Bind(R.id.tv_class_type)
        TextView tvClassType;
        @Bind(R.id.tv_class_address)
        TextView tvClassAddress;
        @Bind(R.id.tv_class_distance_num)
        TextView tvClassDistanceNum;
        @Bind(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
