[![Download](https://api.bintray.com/packages/grouter/maven/grouter/images/download.svg)](https://bintray.com/grouter/maven/grouter/_latestVersion)

## GRouter - 为Android组件化开发而生的路由框架

组件化开发架构中，`页面跳转`、`组件间调用`是至关重要的两大问题，目前Github上已经有多个开源框架，基本都是通过拼接参数来实现页面跳转、获取服务实例。

GRouter 兼容拼接参数方式同时，区别于其他组件化方案推出了`安全构造器`方案，把工程内的Activity和服务类的构造器生成在GActivityCenter、GComponentCenter、GTaskCenter、GDelegateCenter类中，方便我们安全地调用，避免了拼接参数容易写错，或者目标类被修改带来的运行错误。

### 特点

1. 支持[AndroidX](https://github.com/taoweiji/grouter-android/wiki#使用教程)、支持 Java 和 Kotlin。
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
13. 支持从外部浏览器和[其它APP打开内部Activity](https://github.com/taoweiji/grouter-android/wiki/RouterActivity-详解#支持外部app浏览器打开内部-activity)。

 

> GRouter已经在拥有434个Activity、28个Module的千万用户级别APP稳定使用，大家可以放心使用。GRouter 会一直致力于组件化解决方案，如果你有更好的建议，可以提Issues或私聊联系我。如果你觉得还不错，欢迎[ star 该 Github 项目](https://github.com/taoweiji/grouter-android)，我们会有持续的优化迭代，感谢你的支持！

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

