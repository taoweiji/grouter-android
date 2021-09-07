## GRouter - Android 结构化路由框架

[![Maven Central](https://img.shields.io/maven-central/v/io.github.taoweiji.grouter/grouter)](https://search.maven.org/search?q=io.github.taoweiji.grouter)

相对于基于字符串的路由框架（ARouter）而言，结构化路由框架具备面向对象、中心化的特点，如果我们需要跳转到一个Activity，ARouter 得先去查找这个页面的路径是什么、接收什么参数、参数的格式和用途是什么。而结构化路由框架则会自动生成一个中心类，把所有的页面调用都集成进去，有什么页面都清晰明了，每一个Activity都会生成一个Builder类，有什么参数，参数的作用都会注释清晰，不用担心会传错参数。解决Activity参数调整，字符串路由框架容易出现严重的调用异常问题。


- 其它路由框架跳转Activity，需要找到Activity的路径，拼接成url跳转，而 GRouter 会生成中心化调用类，为每一个Activity生成调用方法；
- 其它路由框架跳转Activity传参时需要知道参数名称和类型，而 GRouter 为每一个Activity生成一个Builder类，生成相应的参数方法，无需担心参数名称和类型写错，也不用担心后续 Activity 调整后导致url跳转异常。


### 特点

2. 支持[Flutter](https://github.com/taoweiji/grouter-android/wiki/支持多工程项目&混合工程#flutter)、[Hybrid H5](https://github.com/taoweiji/grouter-android/wiki/支持多工程项目&混合工程#hybrid-h5)混合项目，可以通过URL调用原生模块获取服务数据、跳转Activity。
3. 支持多Module项目、[多工程项目](https://github.com/taoweiji/grouter-android/wiki/支持多工程项目&混合工程)；多工程项目支持多scheme。
4. Activity 跳转支持设置默认转场动画，支持设置单次转场动画，支持指定 Flag，支持[多级跳转](https://github.com/taoweiji/grouter-android/wiki/RouterActivity-详解#多级跳转)。
5. Activity、Fragment、Task支持[参数注入](https://github.com/taoweiji/grouter-android/wiki)，无需手动解析参数。
6. 提供RouterComponent、RouterTask、RouterDelegate三种强大的组件间调用服务组件。
7. 支持[生成HTML文档](https://github.com/taoweiji/grouter-android/wiki)和导入RAP，方便查询。
8. 支持[服务降级](https://github.com/taoweiji/grouter-android/wiki/服务降级)，支持通过服务降级Mock数据，可以实现[单Module运行调试](https://github.com/taoweiji/grouter-android/wiki/RouterComponent-详解#服务降级)，提高开发效率。
9. 各个组件均支持`自动生成构造器`，避免拼接参数容易写错问题。
10. 提供 [IDEA 插件](https://github.com/taoweiji/grouter-android/wiki#IDEA插件)，支持快捷跳转到目标类，支持 Java 和 Kotlin。
11. 使用Gradle插件注册Module模块，简化配置。
13. RouterComponent支持获取Fragment。
14. 支持从外部浏览器和[其它APP打开内部Activity](https://github.com/taoweiji/grouter-android/wiki/RouterActivity-详解#支持外部app浏览器打开内部-activity)。
15. 内置5组页面Activity过场动画，并支持5.0的Activity过场动画。

> GRouter已经在拥有434个Activity、28个Module的千万用户级别APP稳定使用。GRouter 会一直致力于组件化解决方案，如果你有更好的建议，可以提Issues或私聊联系我。


### 示例下载
[示例APP下载](https://github.com/taoweiji/grouter-android/releases/download/1.2.2/app-release.apk)
![grouter](https://user-images.githubusercontent.com/3044176/132285526-dbf7f8bc-a2f5-4454-a4f5-dd0202f8dabb.png)

### 文档

[查看文档](https://github.com/taoweiji/grouter-android/wiki)


### 示例
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







## License

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

