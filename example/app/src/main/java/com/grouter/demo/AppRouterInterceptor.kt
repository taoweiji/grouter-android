package com.grouter.demo

import com.grouter.*
import com.grouter.demo.activity.LoginActivity

import java.lang.Exception

@Suppress("unused")
@RouterInterceptor
class AppRouterInterceptor : GRouterInterceptor() {
    override fun process(request: ActivityRequest): Boolean {
        // 修改跳转的目标Activity
//        request.onContinue(Activity::class.java)
        // 拦截中断访问
//        request.onInterrupt(Exception("无权访问"))
        request.data
        request.extras
        request.activityClass
        request.context
        request.requestCode
        request.intent
//        request.onContinue(LoginActivity::class.java)

        return super.process(request)
    }



    override fun process(request: TaskRequest): Boolean {
//        request.onContinue(null)
//        request.onInterrupt(Exception("无权访问"))
        request.taskClass
        request.uri
        request.params
        return super.process(request)
    }

    override fun process(request: ComponentRequest): Boolean {
        request.protocol
        return super.process(request)
    }

    override fun onError(request: ActivityRequest, exception: Exception): Boolean {
        // 修改跳转的目标Activity
//        request.onContinue(Activity::class.java)
        // 拦截中断访问
//        request.onInterrupt(Exception("无权访问"))
        request.data
        request.extras
        request.activityClass
        request.context
        request.requestCode
        request.intent
        return super.onError(request, exception)
    }

    override fun onError(request: TaskRequest, exception: Exception): Boolean {
//        request.onContinue(null)
//        request.onInterrupt(Exception("无权访问"))
        request.taskClass
        request.uri
        request.params
        return super.onError(request, exception)
    }

    override fun onError(request: ComponentRequest, exception: Exception): Boolean {
        exception.printStackTrace()
        return super.onError(request, exception)
    }

}
