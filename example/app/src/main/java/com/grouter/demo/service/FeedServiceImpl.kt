package com.grouter.demo.service

import android.app.Activity
import android.content.Context

import com.grouter.RouterComponent
import com.grouter.RouterComponentConstructor
import com.grouter.demo.base.service.FeedService
import com.grouter.demo.base.model.User

@RouterComponent(protocol = FeedService::class,name = "动态服务", description = "本地缓存和网络请求")
class FeedServiceImpl @RouterComponentConstructor constructor() : FeedService {
    private var context: Context? = null
    @RouterComponentConstructor
    constructor(context: Context, uid: Int, sums: IntArray) : this() {
        this.context = context
    }

    @RouterComponentConstructor
    constructor(activity: Activity?) : this() {
        this.context = activity
    }

    override fun getUser(uid: Int): User {
        return User("Wiki")
    }

    override fun listUser(): List<User> {
        return listOf(User("Wiki"))
    }

}
