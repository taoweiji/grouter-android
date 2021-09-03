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

