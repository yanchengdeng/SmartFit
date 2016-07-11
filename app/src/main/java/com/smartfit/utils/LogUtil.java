package com.smartfit.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/2/23.
 * 打印日志   方便 在发布时 关闭
 */
public class LogUtil {

    private static boolean LOGV = false;
    private static boolean LOGD = false;
    private static boolean LOGI = false;
    private static boolean LOGW = false;
    private static boolean LOGE = false;
    public static void v(String tag, String mess) {
        if (LOGV) { Log.v(tag, mess); }
    }
    public static void d(String tag, String mess) {
        if (LOGD) { Log.d(tag, mess); }
    }
    public static void i(String tag, String mess) {
        if (LOGI) { Log.i(tag, mess); }
    }
    public static void w(String tag, String mess) {
        if (LOGW) { Log.w(tag, mess); }
    }
    public static void e(String tag, String mess) {
        if (LOGE) {
            Log.e(tag, mess);
        }
    }
}
