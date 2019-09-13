### ProGuard 混淆

如果项目用到了混淆，需要添加下面代码到proguard-rules.pro

```properties
-keep class com.grouter.GRouterInitializer
-keep @com.grouter.RouterComponent class *
-keep @com.grouter.RouterDelegate class *
-keep @com.grouter.RouterInterceptor class *
-keep @com.grouter.RouterTask class *
-keep @com.grouter.RouterActivity class *
-keepclasseswithmembers class * {
    @com.grouter.RouterField <fields>;
}
-keepclasseswithmembers class * {
    @com.grouter.RouterDelegateMethod <methods>;
}
-keepclasseswithmembers class * {
    @com.grouter.RouterDelegateConstructor <init>(...);
}
-keepclasseswithmembers class * {
    @com.grouter.RouterComponentConstructor <init>(...);
}

```

