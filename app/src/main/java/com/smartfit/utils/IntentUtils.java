package com.smartfit.utils;

import android.content.Context;
import android.content.Intent;

import com.smartfit.activities.BaseActivity;


/**
 * Created by 邓言诚 on 2015/8/5.
 * 界面跳转工具类
 */
public class IntentUtils {

    /***
     * 开启新界面
     *
     * @param context     Context 对象
     * @param intent      意向
     * @param requestCode 请求码 0代表未有返回码
     */
    public static void startActivity(Context context, Intent intent, int requestCode) {
        if (requestCode == 0) {
            context.startActivity(intent);
        }else{
            ((BaseActivity) context).startActivityForResult(intent, requestCode);
        }
    }
}
