# AndroidRouter
[![](https://jitpack.io/v/joyrun/AndroidRouter.svg)](https://jitpack.io/#joyrun/AndroidRouter)

通过注解方式来实现URL打开Activity功能，并支持在WebView和外部浏览器使用，支持多级Activity跳转，支持Bundle、Uri参数注入并转换参数类型。
### 特点
1. 支持注解方式、手动方式注册Activity。
2. 支持注入Bundle、Uri的参数并转换格式。
3. 支持多级跳转。
4. 支持外部浏览器打开。
5. 支持HTTP协议。

### 简单例子
```java
@RouterActivity("second")
public class SecondActivity extends Activity {
    @RouterField("uid")
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Routers.inject(this);
        Log.e("uid", String.valueOf(uid));
    }
}
```
```java
Routers.init("joyrun");//设置Scheme
Routers.startActivity(context, "joyrun://second?uid=233");
```

### 多级跳转

```java
@RouterActivity("third")
public class ThirdActivity extends BaseActivity {
    @RouterField("uid")
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Routers.inject(this);
        Log.e("uid", String.valueOf(uid));
    }
}
```
```java
// 先打开SecondActivity，再打开ThirdActivity
Routers.startActivity(context, "joyrun://second/third?uid=233");
```
### Bundle、Uri参数注入（支持单独使用）
Routers.inject(this)方法可以`单独使用`，可以实现注入Bundle、Uri的参数，由于Uri的参数是String类型，所以该框架还支持把String格式的类型转换为目标类型。目前该方法支持double、float、int、boolean、String数据类型。
```java
// Bundle
Intent intent = new Intent(this, SecondActivity.class);
intent.putExtra("uid", "233");
startActivity(intent);
// Uri
Intent intent = new Intent(this, SecondActivity.class);
intent.setData(Uri.parse("joyrun://second?uid=233"));
startActivity(intent);
// Bundle、Uri
Intent intent = new Intent(this, SecondActivity.class);
intent.setData(Uri.parse("joyrun://second?uid=233"));
intent.putExtra("name", "Wiki");
startActivity(intent);
```
```java
@RouterActivity("second")
public class SecondActivity extends Activity {
    @RouterField("uid")
    private int uid;
    @RouterField("name")
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Routers.inject(this);
        Log.e("uid", String.valueOf(uid));
        Log.e("name", String.valueOf(name));
    }
}
```
### 从外部浏览器、其它APP打开
只要在AndroidManifest.xml注册了RouterCenterActivity，即可变成经典的Uri打开，可以支持外部浏览器、其它APP打开内部的Activity。
```xml
<activity android:name="com.thejoyrun.router.RouterCenterActivity">
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <action android:name="android.intent.action.VIEW" />
        <data android:scheme="joyrun" />
    </intent-filter>
</activity>
```
```java
// Java代码调用
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("joyrun://second?uid=233&name=Wiki")));
```
```html
// HTML方式，系统浏览器（不支持微信）
<a href="joyrun://second?uid=233&name=Wiki">打开JoyrunApp的SecondActivity</a>
```

### 支持HTTP协议
```xml
<activity android:name="com.thejoyrun.router.RouterCenterActivity">
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <action android:name="android.intent.action.VIEW" />
        <data android:scheme="joyrun" />
    </intent-filter>
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <action android:name="android.intent.action.VIEW" />
        <data android:host="www.thejoyrun.com" android:scheme="http" />
    </intent-filter>
</activity>
```
如果支持HTTP协议，那么URL的结构就要做些修改
```
// joyrun://second?uid=233
// =>
http://www.thejoyrun.com/second?uid=233
```

## 使用方式
### 配置根目录的build.gradle 
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
	dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}
```
### 配置app module的build.gradle 
```
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    compile 'com.github.joyrun.AndroidRouter:router:0.2.0'
    apt 'com.github.joyrun.AndroidRouter:router-compiler:0.2.0'
}
```
### 初始化
建议在Application进行初始化
```java
// 必填，填写独特的scheme，避免和其它APP重复
Routers.init("joyrun");
// 可选，如果需要支持HTTP协议就需要填写
Routers.setHttpHost("www.thejoyrun.com");
// 可选，手工注册Activity
Routers.register(new ActivityRouteTableInitializer() {
    @Override
    public void initRouterTable(Map<String, Class<? extends Activity>> router) {
        router.put("second2", SecondActivity.class);
        router.put("other://www.thejoyrun.com/second", SecondActivity.class);
    }
});
// 可选，针对自己的业务做调整
Routers.setFilter(new Filter() {
    public String doFilter(String url) {
    	//return url.replace("joyrun://www.thejoyrun.com/","joyrun://");
        return url;
    }
});
```

