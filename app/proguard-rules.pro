# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/dengyancheng/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Signature


-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {  # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}



#实体类
-keep  class com.smartfit.beans.** { *;}


#忽略libs
-libraryjars libs/alipaySDK-20150610.jar
-libraryjars libs/AMap_2DMap_V2.8.1_20160202.jar
-libraryjars libs/AMap_Location_V2.4.0_20160308.jar
-libraryjars libs/GetuiExt-2.0.3.jar
-libraryjars libs/GetuiSDK2.8.1.0.jar
-libraryjars libs/httpmime-4.1.3.jar
-libraryjars libs/jpinyin-1.0.jar
-libraryjars libs/SocialSDK_Sina.jar
-libraryjars libs/SocialSDK_WeiXin_1.jar
-libraryjars libs/SocialSDK_WeiXin_2.jar
-libraryjars libs/umeng_social_sdk.jar
-libraryjars libs/weiboSDKCore_3.1.2.jar

#忽略.so 文件
-libraryjars src/main/jniLibs/x86_64/libweibosdkcore.so
-libraryjars src/main/jniLibs/x86/libweibosdkcore.so
-libraryjars src/main/jniLibs/x86/libgetuiext.so
-libraryjars src/main/jniLibs/mips64/libweibosdkcore.so
-libraryjars src/main/jniLibs/mips/libweibosdkcore.so
-libraryjars src/main/jniLibs/armeabi-v7a/libgetuiext.so
-libraryjars src/main/jniLibs/armeabi-v7a/libweibosdkcore.so
-libraryjars src/main/jniLibs/armeabi/libweibosdkcore.so
-libraryjars src/main/jniLibs/arm64-v8a/libweibosdkcore.so


#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


-keep class org.apache.http.** { *;}
-keepclassmembers class org.apache.http.** { *;}
-dontwarn org.apache.**

-keep class com.nineoldandroids.**{ *;}
-keepclassmembers class com.nineoldandroids.**{ *;}
-dontwarn com.nineoldandroids.**


#地图
-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}

#个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

#lib 包
-keep class android.support.annotation.**{ *;}
-dontwarn  android.support.annotation.**
-keep  class android.support.**{ *;}
-dontwarn  android.support.**
-keep  class com.android.volley.**{ *;}
-dontwarn com.android.volley.**
-keep  class com.flyco.**{ *;}
-dontwarn  com.flyco.**
-keep  class com.google.gson.**{ *;}
-dontwarn com.google.gson.**
-keep  class com.bigkoo.svprogresshub.**{ *;}
-dontwarn com.bigkoo.svprogresshub.**
-keep  class com.readystatesoftware.systembartint.**{ *;}
-dontwarn com.readystatesoftware.systembartint.**
-keep class com.nostra13.universalimageloader.**{ *;}
-dontwarn com.nostra13.universalimageloader.**
-keep class org.apache.http.**{ *;}
-dontwarn org.apache.http.**
-keep class org.greenrobot.eventbus.**{*;}
-dontwarn org.greenrobot.eventbus.**
-keep class me.gujun.android.taggroup.**{*;}
-dontwarn me.gujun.android.taggroup.**
-keep class com.ogaclejapan.smarttablayout.**{*;}
-dontwarn com.ogaclejapan.smarttablayout.**
-keep class com.nineoldandroids.**{*;}
-dontwarn com.nineoldandroids.**
-keep class com.squareup.picasso.**{*;}
-dontwarn com.squareup.picasso.**
-keep class com.ecloud.pulltozoomview.**{*;}
-dontwarn com.ecloud.pulltozoomview.**
-keep class com.jude.rollviewpager.**{*;}
-dontwarn com.jude.rollviewpager.**
-keep class com.readystatesoftware.systembartint.**{*;}
-dontwarn com.readystatesoftware.systembartint.**
-keep class com.ogaclejapan.smarttablayout.utils.**{*;}
-dontwarn com.ogaclejapan.smarttablayout.utils.**
-keep class android.backport.webp.**{*;}
-dontwarn android.backport.webp.**
-keep class org.xutils.**{*;}
-dontwarn  org.xutils.**


