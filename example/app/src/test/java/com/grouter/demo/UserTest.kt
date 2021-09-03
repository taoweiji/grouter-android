package com.grouter.demo

import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grouter.GRouter

import org.junit.Test

class UserTest {

    @Test
    fun a() {
        val text = "[\"abc\", \"def\", \"ghi\"]"
        GRouter.setSerialization(object : GRouter.Serialization{
            var gson = Gson()
            override fun serialize(any: Any?): String {
                return gson.toJson(any)
            }

            override fun <T : Any?> deserializeObject(json: String?, clazz: Class<T>?): T {
                return gson.fromJson(json,clazz)
            }

            override fun <T : Any?> deserializeList(json: String?, clazz: Class<T>?): MutableList<T> {
                return gson.fromJson(json, TypeToken.getParameterized(List::class.java,clazz).type)
            }
        })

        val result = GRouter.serialization.deserializeList(text,String::class.java)
        println(JSON.toJSONString(result))
    }
}
//GRouter.setSerialization(object : GRouter.Serialization{
//    var gson = Gson()
//    override fun serialize(any: Any?): String {
//        return gson.toJson(any)
//    }
//
//    override fun <T : Any?> deserializeObject(json: String?, clazz: Class<T>?): T {
//        return gson.fromJson(json,clazz)
//    }
//
//    override fun <T : Any?> deserializeList(json: String?, clazz: Class<T>?): MutableList<T> {
//        return gson.fromJson(json, TypeToken.getParameterized(List::class.java,clazz).type)
//    }
//})