package com.smartfit;

import android.app.Application;

import com.smartfit.commons.AppException;

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
