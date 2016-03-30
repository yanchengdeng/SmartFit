package com.smartfit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.smartfit.commons.Constants;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2015/6/1.
 */
public class NetUtil {

    public static Map<String, String> getRequestBody(Context context) {

        Map<String, String> map = new HashMap<>();
        map.put("terminal", "1");
        map.put("versionCode", String.valueOf(DeviceUtil.getVersionCode(context)));
        map.put("versionName", DeviceUtil.getVersionName(context));
        map.put("Uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        map.put("acc", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, ""));
        map.put("imei", DeviceUtil.getIMEI(context));
        map.put("sid", SharedPreferencesUtils.getInstance().getString(Constants.SID, ""));
//        map.put("Longit", SharedPreferencesUtils.getInstance().getString(Constants.CITY_LONGIT, ""));
//        map.put("Lat", SharedPreferencesUtils.getInstance().getString(Constants.CITY_LAT, ""));
//        map.put("CityCode", "");
        LogUtil.w("dyc", map.toString());
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-agent", map.toString());
        return headers;
    }


    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }


    /***
     * 是否登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        String uid = SharedPreferencesUtils.getInstance().getString(Constants.UID, "");
        if (TextUtils.isEmpty(uid)) {
            return false;
        }
        return true;
    }
}
