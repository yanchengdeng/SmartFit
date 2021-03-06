package com.smartfit;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.smartfit.commons.AppException;
import com.umeng.socialize.PlatformConfig;

import org.xutils.x;

/**
 * Created by dengyancheng on 16/2/23.
 */
public class SmartAppliction extends Application {

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
        initImageLoader();
        x.Ext.init(this);
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(getApplicationContext(), options);
//        registerUncaughtExceptionHandler();

    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }


    private void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
        L.writeLogs(false);
    }


}
