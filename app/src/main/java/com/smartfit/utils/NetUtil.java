package com.smartfit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.smartfit.commons.Constants;

import org.apache.http.params.HttpParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Policy;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2015/6/1.
 */
public class NetUtil {

    public static   String  getRequestBody(Map<String, String> data, Context context) {

        Map<String, String> map = new HashMap<>();
        map.put("terminal", "1");
        map.put("versionCode", String.valueOf(DeviceUtil.getVersionCode(context)));
        map.put("versionName", DeviceUtil.getVersionName(context));
        map.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        map.put("acc", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, ""));
        map.put("imei", DeviceUtil.getIMEI(context));
        map.put("sid", SharedPreferencesUtils.getInstance().getString(Constants.SID, ""));

        for (Map.Entry<String, String> entry : data.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map.toString();
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
}
