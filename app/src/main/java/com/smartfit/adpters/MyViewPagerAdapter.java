package com.smartfit.adpters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/***
 * @author yanchengdeng
 *         活动广告页适配器
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;
    private Context mContext;

    public MyViewPagerAdapter(Context mContext, List<View> mListViews) {
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
        this.mContext = mContext;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(mListViews.get(position));
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        ViewGroup group = (ViewGroup) mListViews.get(position % mListViews.size()).getParent();
        //判断是否存在图片，如果存在先移除，后添加
        if (group != null) {
            group.removeView(mListViews.get(position));
        }
        ((ViewPager) container).addView(mListViews.get(position % mListViews.size()));
        View  view = mListViews.get(position % mListViews.size());
        return view;
    }
}


