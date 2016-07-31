# AndroidRouter
通过注解方式来实现URL打开Activity功能，并支持在WebView和外部浏览器使用，支持多级Activity跳转，支持Bundle、Uri参数注入并转换参数类型。
### 特点
1. 支持注解方式、手动方式注册Activity。
2. 支持注入Bundle、Uri的参数并转换格式。
3. 支持多级跳转。
4. 支持外部浏览器打开。
5. 支持HTTP协议。

### 简单例子
```
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
```
Routers.init("joyrun");//设置Scheme
Routers.startActivity(context, "joyrun://second?uid=233");
```
