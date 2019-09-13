package com.t.example_shell_webview

import android.app.Application
import android.content.Context
import com.alibaba.fastjson.JSON
import com.grouter.GComponentCenter
import com.grouter.GRouter
import com.grouter.demo.base.model.User
import com.grouter.demo.base.service.UserService
import com.t.example_shell_base.BuildConfig
import org.mockito.Mockito
import org.mockito.Mockito.mock

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
        System.setProperty("org.mockito.android.target",getDir("target", Context.MODE_PRIVATE).absolutePath)
    }
}