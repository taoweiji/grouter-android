
GRouter 提供三种组件间通信组件，RouterComponent、RouterTask和RouterDelegate，各个组件都有不同的特点有不同的应用场景。



|               | RouterComponent                                              | RouterTask                                | RouterDelegate                                               |
| ------------- | ------------------------------------------------------------ | ----------------------------------------- | ------------------------------------------------------------ |
| 跨Project调用 | 支持                                                         | 支持                                      | 不支持                                                       |
| 描述          | 接口下沉式组件间服务                                         | 非下沉式组件间单任务服务                  | 代理式服务                                                   |
| 缺点          | 需要在BaseModule下沉接口，适合在当前Project中使用，跨Project成本较大 | 每个Task只能执行一种任务                  | 整体使用反射实现，与实现类关联性很低，只适合在当前Project使用 |
| 优点          |                                                              | 依赖性很低，特别适合在跨Project项目中使用 |                                                              |
|               |                                                              |                                           |                                                              |



### RouterComponent（需要下沉接口，多工程）
通常用于频繁使用的组件，比如获取当前的登录信息，获取单个用户信息等，使用频率很高，在各个Module都有不同程度的调用，由于是接口实现方式实现，代码调用方便。

### RouterDelegate（需要下沉Class，最简单，当前工程）
RouterDelegate 的使用极其简单，原理是在BaseModule生成对应的壳类，通过反射代理的方式调用原来的类，虽然反射并没有降低太多的性能，但是使用时候需要更多的注意，因为需要代理的方法依赖的Class（eg. User.class）必须在BaseModule也存在，否则会生成代码出错，一旦出错就需要使用Gradle插件`GRouterFixRelease`命令解决错误。

### RouterTask（多工程、混合开发，功能强大）

RouterTask的使用非常广泛，无需下沉接口，支持URL调用，相当于远程服务器API一样，可以在当前的Project使用，也可以解决不同的Project的服务调用问题，而且还支持 `Hybrid H5`和`Flutter`混合开发调用服务，支持转换成String、Map、反序列化对象和List，方便调用者在无依赖情况下使用，应用范围比RouterComponent和RouterDelegate大很多。





