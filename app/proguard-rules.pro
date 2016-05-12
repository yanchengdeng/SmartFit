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


-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

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

-keep  class com.smartfit.beans.** { *;}


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
-keep  class android.support.**{ *;}
-keep  class com.android.volley.**{ *;}
-keep  class com.flyco.**{ *;}
-keep  class com.google.gson.**{ *;}
-keep  class com.bigkoo.svprogresshub.**{ *;}
-keep  class com.readystatesoftware.systembartint.**{ *;}
-keep class com.nostra13.universalimageloader.**{ *;}
-keep class org.apache.http.**{ *;}

