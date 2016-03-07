package com.smartfit;

import android.app.Application;

import com.smartfit.commons.AppException;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by dengyancheng on 16/2/23.
 */
public class SmartAppliction extends Application{

    private static SmartAppliction app;

    public SmartAppliction() {
        app = this;
    }

    public static synchronized SmartAppliction getInstance() {
        if (app == null) {
            app = new SmartAppliction();
        }
        return app;
    }

    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        registerUncaughtExceptionHandler();
    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }


}
