package com.grouter.demo

import android.app.Application
import android.util.Log

import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grouter.GRouter
import com.grouter.GRouterLogger
import java.lang.Exception

/**
 * Created by Wiki on 19/7/30.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()


//        GRouter.setSerialization(object : GRouter.Serialization {
//            override fun serialize(any: Any): String {
//                return JSON.toJSONString(any)
//            }
//
//            override fun <T> deserializeObject(json: String, clazz: Class<T>): T? {
//                return JSON.parseObject(json, clazz)
//            }
//
//            override fun <T> deserializeList(json: String, clazz: Class<T>): List<T>? {
//                return JSON.parseArray(json, clazz)
//            }
//        })
        GRouter.setSerialization(object :GRouter.Serialization{
            var gson = Gson()
            override fun serialize(any: Any?): String {
                return gson.toJson(any)
            }

            override fun <T : Any?> deserializeObject(json: String?, clazz: Class<T>?): T {
                return gson.fromJson(json,clazz)
            }

            override fun <T : Any?> deserializeList(json: String?, clazz: Class<T>?): MutableList<T> {
                return gson.fromJson(json, TypeToken.get(clazz).type)
            }
        })


        GRouter.getInstance().init(this, BuildConfig.BUILD_TYPE, null)

        // TODO GSON解析

        //        GRouter.getInstance().openAppCheck();

        //        GRouter.getInstance().registerMultiProject(new MainProjectGRouterInitializer());

        GRouter.getInstance().init(this, BuildConfig.BUILD_TYPE, null)

        GRouterLogger.setLogger(object : GRouterLogger() {
            override fun logger(tag: String, message: String, throwable: Throwable?) {
                Log.e(tag, message)
                throwable?.printStackTrace()
            }

            override fun handleException(e: Exception) {
                e.printStackTrace()
            }
        })

//        GRouter.getInstance().registerMultiProject(WaimaiRouterInitializer())
//        GRouter.getInstance().registerMultiProject(MovieRouterInitializer())

//        GRouter.getInstance().activityBuilder("movie","movieDetail").start(context)
//        GRouter.getInstance().taskBuilder("movie","getMovie").put("mid",1).execute()
    }
}
