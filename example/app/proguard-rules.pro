# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Wiki/Documents/Tools/android-sdk-macosx/tools/proguard/proguard-android.txt
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

# 避免GRouterInitializer类名被混淆
-keep class com.grouter.GRouterInitializer
# 避免带有@RouterField的字段名称被混淆
-keepclasseswithmembers class * {
    @com.grouter.RouterField <fields>;
}
# 避免带有@RouterDelegateMethod的方法被混淆
-keepclasseswithmembers class * {
    @com.grouter.RouterDelegateMethod <methods>;
}
# 避免带有@RouterDelegateConstructor的构造方法被混淆
-keepclasseswithmembers class * {
    @com.grouter.RouterDelegateConstructor <init>(...);
}
# 避免带有@RouterComponentConstructor的构造方法被混淆
-keepclasseswithmembers class * {
    @com.grouter.RouterComponentConstructor <init>(...);
}
-keep @com.grouter.RouterComponent class *
-keep @com.grouter.RouterDelegate class *
-keep @com.grouter.RouterInterceptor class *
-keep @com.grouter.RouterTask class *
-keep @com.grouter.RouterActivity class *





#-keep
#-keepnames
#-keepattributes
#-keepclasseswithmembernames
#-keepclasseswithmembers
#-keepclassmembers
#-keeppackagenames
#-keepparameternames
#-whyareyoukeeping