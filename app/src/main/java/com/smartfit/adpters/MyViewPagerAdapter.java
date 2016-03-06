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
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(((AdList)(mListViews.get(position % mListViews.size())).getTag()).getType().equals("1")){
//                    //活动
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants._ID, ((AdList)(mListViews.get(position % mListViews.size())).getTag()).getAd_id());
//                    ((MainActivity)mContext). openActivity(EventDetailActivity.class, bundle);
//                }else if(((AdList)(mListViews.get(position % mListViews.size())).getTag()).getType().equals("2")){
//                    //商家
//                    Bundle bundle = new Bundle();
//                    Shop shop = new Shop();
//                    shop.setShop_id(((AdList)(mListViews.get(position % mListViews.size())).getTag()).getAd_id());
//                    shop.setShop_name(((AdList)(mListViews.get(position % mListViews.size())).getTag()).getTitle());
//                    bundle.putSerializable(Constants.PASS_ONE_OBJECT, shop);
//                    ((BaseActivity) mContext).openActivity(ShopDetailActivity.class, bundle);
//                }else{
//                    //网页
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.ACTIVITY_TITTLE,((AdList)(mListViews.get(position % mListViews.size())).getTag()).getTitle());
//                    bundle.putString(Constants.ACTIVITY_ACTION,((AdList)(mListViews.get(position % mListViews.size())).getTag()).getLink());
//                    ((MainActivity)mContext).openActivity(ActionActivity.class,bundle);
//                }
//            }
//        });

        return view;
    }
}


