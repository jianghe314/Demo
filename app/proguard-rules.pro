# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSDK/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#阿里
-keep class com.kangzhan.student.zfbapi.**{*;}

-keep class com.alipay.fastjson{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
####
-keep class com.kangzhan.student.mUI.**{*;}


#极光推送混淆
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#实体
-keep class com.kangzhan.student.newsFragment.**{*;}
-keep class com.kangzhan.student.entity.**{*;}
-keep class com.kangzhan.student.mUI.**{*;}
-keep class com.kangzhan.student.RecommendBean.**{*;}
-keep class com.kangzhan.student.School.Bean.**{*;}
-keep class com.kangzhan.student.Student.bean.**{*;}
-keep class com.kangzhan.student.Teacher.bean.**{*;}
-keep class com.kangzhan.student.CompayManage.Bean.**{*;}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#第三方依赖
-keep class com.android.support.**{*;}

#七鱼
-dontwarn com.qiyukf.**
-keep class com.qiyukf.** {*;}



