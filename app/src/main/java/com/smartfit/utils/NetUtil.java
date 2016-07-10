package com.smartfit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.smartfit.beans.UserInfoDetail;
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
        map.put("Uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, "0"));
        map.put("acc", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "0"));
        map.put("imei", DeviceUtil.getIMEI(context));
        map.put("sid", SharedPreferencesUtils.getInstance().getString(Constants.SID, "0"));
//        map.put("Longit", SharedPreferencesUtils.getInstance().getString(Constants.CITY_LONGIT, ""));
//        map.put("Lat", SharedPreferencesUtils.getInstance().getString(Constants.CITY_LAT, ""));
//        map.put("CityCode", "");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-agent", map.toString());
        LogUtil.w("dyc", headers.toString());
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
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        String uid = SharedPreferencesUtils.getInstance().getString(Constants.UID, "0");
        if (TextUtils.isEmpty(uid)) {
            return false;
        }

        if (uid.equals("0")) {
            return false;
        }
        return true;
    }

    public static String getPhone() {
        String useString = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(useString)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(useString, UserInfoDetail.class);
            if (userInfoDetail != null) {
                if (!TextUtils.isEmpty(userInfoDetail.getMobile())) {
                    return userInfoDetail.getMobile();

                }
            }
        }

        return "";
    }


    public static UserInfoDetail getUserInfo() {
        String useString = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(useString)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(useString, UserInfoDetail.class);
            if (userInfoDetail != null) {
                return userInfoDetail;
            }
        }
        return null;
    }

    public static boolean getISvip() {
        String isVIP = SharedPreferencesUtils.getInstance().getString(Constants.IS_VIP,"");
        if (isVIP.equals("0")){
            return false;
        }else{
            return true;
        }


    }
}
