package com.smartfit.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.smartfit.R;

/**
 * Created by yanchengdeng on 2015/8/13.
 * 图片加载类型   正常   圆角  等
 */
public class Options {

    /**
     * 新闻列表中用到的图片加载配置
     */
    public static DisplayImageOptions getListOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.mipmap.bg_pic)
                        // // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.mipmap.bg_pic)
                        // // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.mipmap.bg_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())//防止闪烁
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }


    public static DisplayImageOptions getHeaderOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.mipmap.icon_avatar)
                        // // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.mipmap.icon_avatar)
                        // // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.mipmap.icon_avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())//防止闪烁
                .displayer(new RoundedBitmapDisplayer(90))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }







}
