package mock

import android.app.Application
import com.alibaba.fastjson.JSON
import com.grouter.GRouter
import com.grouter.demo.BuildConfig

open class MockBaseApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        GRouter.setSerialization(object : GRouter.Serialization {
            override fun serialize(any: Any): String {
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

        // TODO GSON解析

        //        GRouter.getInstance().openAppCheck();

        //        GRouter.getInstance().registerMultiProject(new MainProjectGRouterInitializer());

        GRouter.getInstance().init(this, BuildConfig.BUILD_TYPE, null)
    }
}