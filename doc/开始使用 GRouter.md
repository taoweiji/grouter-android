[![](https://jitpack.io/v/taoweiji/grouter-android.svg)](https://jitpack.io/#taoweiji/grouter-android)

## GRouter - 为Android组件化开发而生的路由框架

组件化开发架构中，`页面跳转`、`组件间调用`是至关重要的两大问题，目前Github上已经有多个开源框架，基本都是通过拼接参数来实现页面跳转、获取服务实例。

GRouter 兼容拼接参数方式同时，区别于其他组件化方案推出了`安全构造器`方案，把工程内的Activity和服务类的构造器生成在GActivityCenter、GComponentCenter、GTaskCenter、GDelegateCenter类中，方便我们安全地调用，避免了拼接参数容易写错，或者目标类被修改带来的运行错误。

Activity 页面跳转

```kotlin
// 不推荐
GRouter.getInstance().startActivity(context, "grouter://activity/user?uid=1")
// 不推荐
GRouter.getInstance().activityBuilder("user").put("uid",1).start(context)
// 推荐
GActivityCenter.UserActivity().uid(123).start(context)
```

下沉接口式 - 组件间服务调用

```kotlin
// 不推荐
val userService = GRouter.getInstance().getComponent("userService") as UserService
// 不推荐
val userService = GRouter.getInstance().getComponent("userService",UserService::class.java)
// 推荐
val userService = GComponentCenter.UserServiceImpl()
```

非下沉式 - 组件间单任务调用

```kotlin
// 不推荐
val response = GRouter.getInstance().taskBuilder("grouter://task/getUser?uid=1").execute()
// 不推荐
val response = GRouter.getInstance().taskBuilder("getUser").put("uid",1).execute()
// 推荐
val response = GTaskCenter.GetUserTask().uid(1).execute()
// 获取返回值
val user = response.value(User::class.java)
```

非下沉式 - 反射代理服务

```kotlin
val accountServiceDelegate = GDelegateCenter.AccountService(context)
```

### 特点

1. 支持[AndroidX]()、支持 Java 和 Kotlin。
2. 支持[Flutter]()、[Hybrid H5]()混合项目，可以通过URL调用原生模块获取服务数据、跳转Activity。
3. 支持多Module项目、[多工程项目]()；多工程项目支持多scheme。
4. Activity 跳转支持设置默认转场动画，支持设置单次转场动画，支持指定 Flag，支持[多级跳转]()。
5. Activity、Fragment、Task支持[参数注入]()，无需手动解析参数。
6. 提供RouterComponent、RouterTask、RouterDelegate三种强大的组件间调用服务组件。
7. 支持[生成HTML文档]()和导入RAP，方便查询。
8. 支持[服务降级]()，支持通过服务降级Mock数据，可以实现[单Module运行调试]()，提高开发效率。
9. 各个组件均支持`自动生成构造器`，避免拼接参数容易写错问题。
10. 提供 [IDEA 插件]()，支持快捷跳转到目标类，支持 Java 和 Kotlin。
11. 使用Gradle插件注册Module模块，简化配置。
12. RouterComponent支持获取Fragment。
13. 支持从外部浏览器和[其它APP打开内部Activity]()。

 

> GRouter已经在拥有434个Activity、28个Module的千万用户级别APP稳定使用，大家可以放心使用。GRouter 会一直致力于组件化解决方案，如果你有更好的建议，可以提Issues或私聊联系我。如果你觉得还不错，欢迎[ star 该 Github 项目](https://github.com/taoweiji/grouter-android)，我们会有持续的优化迭代，感谢你的支持！



### 使用教程

配置 gradle.properties

```properties
# 如果是多Module，请填写填写BaseModule的路径{base-module-name}/src/main/java
GROUTER_SOURCE_PATH =app/src/main/java 
# 请填写自己项目独特的scheme
GROUTER_SCHEME =grouter
```

配置根目录 build.gradle

```groovy
buildscript {
    repositories {
        jcenter()
      	maven { url 'https://dl.bintray.com/grouter/maven' }
    }
    dependencies {
        classpath 'com.grouter:grouter-gradle-plugin:1.0.0'
    }
}
```

在每一个Module的 build.gradle 增加

```groovy
// 需要注意顺序，必须放在其它 'apply plugin: XXX' 后面
apply plugin: 'grouter' 
```

在 MyApplication 进行初始化

```kotlin
// 需要根据项目使用的json序列化框架实现序列化，下面的fastjson的序列化方案
GRouter.setSerialization(object : GRouter.Serialization {
    override fun serializable(any: Any): String {
        return JSON.toJSONString(any)
    }
    override fun <T> deserializeObject(json: String, clazz: Class<T>): T? {
        return JSON.parseObject(json, clazz)
    }
    override fun <T> deserializeList(json: String, clazz: Class<T>): List<T>? {
        return JSON.parseArray(json, clazz)
    }
})
GRouter.getInstance().init(this, BuildConfig.BUILD_TYPE, null)
```



### @RouterActivity

增加注解，然后点击顶部菜单 `Build` 的 `Make Module XXX` 就会在 GActivityCenter 中生成构造器方法。

```kotlin
@RouterActivity("user")
class UserActivity : AppCompatActivity() {
    @RouterField
    var uid: Int? = null
    @RouterField
    var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GRouter.inject(this)
        Log.e("uid", uid.toString())
    }
}

```

支持三种方式跳转，如果是在当前工程建议使用第三种方式跳转

```kotlin
// 方式一
GRouter.getInstance().startActivity(context, "grouterdemo://activity/user?uid=1")
// 方式二
GRouter.getInstance().activityBuilder("user").put("uid",1).start(context)
// 方式三：推荐
GActivityCenter.UserActivity().user(User(1,"Wiki")).start(context)

```

> RouterField参数支持类型：int、long、float、double、boolean、Integer、Long、Double、Float、Boolean、String、int[]、long[]、float[]、double[]、boolean[]、String[]、可序列化List、可序列化Object

[详细文档]()

### @RouterTask

增加注解，然后点击顶部菜单 `Build` 的 `Make Module XXX` 就会在 GTaskCenter 中生成构造器方法。

```kotlin
@RouterTask(path = "getUser")
public class GetUserTask extends GRouterTask {
    @RouterField
    public int uid;
    @RouterField
    public String name;
    @Override
    public Object process() {
      	// do something
        return new User(uid, "Wiki");
    }
}

```

支持三种方式调用

```kotlin
// 方式一：通过url创建
val response = GRouter.getInstance().taskBuilder("grouter://task/getUser?uid=1").execute()
// 方式二：通过构造器创建
val response = GRouter.getInstance().taskBuilder("getUser").put("uid",1).execute()
// 方式三：通过自动构造器
val response = GTaskCenter.GetUserTask().uid(1).execute()

// 支持直接获取返回的对象
val user = response.value(User::class.java)
// 支持直接获取返回对象的某个字段
val userName = response.map()["name"]
// 支持直接获取返回的json序列化
val text = response.string()

```

> 方式一，RouterField参数支持类型：int、long、float、double、boolean、Integer、Long、Double、Float、Boolean、String、int[]、long[]、float[]、double[]、boolean[]、String[]、可序列化List、可序列化Object。
>
> 如果是方式二和方式三，支持任意类型参数。

[详细文档]()

### @RouterComponent

在 BaseModule 增加接口

```kotlin
interface UserService {
    fun getUser(uid: Int): User?
}

```

在 Module 中编写实现类，然后点击顶部菜单 `Build` 的 `Make Module XXX` 就会在 GComponentCenter 中生成构造器方法。

```kotlin
@RouterComponent(value = "userService",protocol = AccountService::class)
class UserServiceImpl : UserService {
    constructor() {}
  	@RouterComponentConstructor
    constructor(context: Context) {}
    override fun getUser(uid: Int): User? {
        return null
    }
}

```

有两种方式调用

```kotlin
// 不推荐
val userService = GRouter.getInstance().getComponent("userService") as UserService
// 不推荐
val userService = GRouter.getInstance().getComponent("userService",UserService::class.java)
// 推荐
val userService = GComponentCenter.UserServiceImpl()

val user = userService.getUser(1)

```

[详细文档]()

### @RouterDelegate

在Module中创建服务类，然后点击顶部菜单 `Build` 的 `Make Module XXX` 就在 GDelegateCenter 中生成构造器方法。

```kotlin
@RouterDelegate
class AccountService {
  	constructor() {}
    @RouterDelegateConstructor
    constructor(context: Context) {}
    @RouterDelegateMethod
    fun login(username: String, password: String): User? {
      	// do something
        return null
    }
}

```

在其它无依赖的Module或者BaseModule都可用直接使用生成的代理类调用

```kotlin
val accountServiceDelegate = GDelegateCenter.AccountService(context)
val user = accountServiceDelegate.login("","")

```

[详细文档]()

### 异常处理（一定要注意）

当开发过程中由于合并错误，或者忘记了下沉接口或参数类到BaseModule导致出现编译异常，请在命令行执行以下命令解决错误，也可以在Gradle面板找到 `grouter/GRouterFixRelease` 执行，其它异常问题浏览。

```
./gradlew GRouterFixRelease

```

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

## License

```
Copyright 2019 taoweiji

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```

