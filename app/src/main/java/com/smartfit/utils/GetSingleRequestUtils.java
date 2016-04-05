package com.smartfit.utils;

import android.app.Activity;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yanchengdeng on 2016/4/1.
 * 获取单例网络请求
 */
public class GetSingleRequestUtils {
    private static GetSingleRequestUtils mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private GetSingleRequestUtils(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized GetSingleRequestUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GetSingleRequestUtils(context);
        }
        return mInstance;
    }

    public static synchronized GetSingleRequestUtils getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new GetSingleRequestUtils(activity.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
}
